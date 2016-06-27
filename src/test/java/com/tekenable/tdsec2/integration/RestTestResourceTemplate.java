package com.tekenable.tdsec2.integration;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by nbarrett on 15/06/2016.
 */
public class RestTestResourceTemplate {

    public static final String REST_TEST_DESC = "HTTP response should be equal 20x";

    //public final static String BASE_URL = "http://localhost:8080/trialdirect/api/";
    //Had to uncomment the line below to run this locally. NoelB 23/05/2016
    public final static String BASE_URL = "http://localhost:8080/"; //api/tdsec/";

    private HttpStatus status;
    private HttpHeaders responseHeaders;

    public HttpStatus getStatus() {
        return status;
    }

    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    protected int getNewItemId() {
        URI uri;
        try {
            uri = new URI(this.getResponseHeaders().get("location").get(0));
        } catch (URISyntaxException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }
        String[] segments = uri.getPath().split("/");
        String idStr = segments[segments.length-1];
        return Integer.parseInt(idStr);
    }

    /**
     * this methods reads all items present, regardless their membership
     * @param restPath
     * @return
     */
    public String getAllItems(String restPath) {
        return this.makeRestCall("GET", BASE_URL.concat(restPath), null);
    }

    /**
     * this method reads single items identified by given ID
     * @param restPath
     * @param id
     * @return
     */
    public String getSingleItemById(String restPath, int id) {
        return this.makeRestCall("GET", BASE_URL.concat(restPath).concat("/").concat(String.valueOf(id)), null);
    }

    /**
     * this method suites for artifacts having any text field
     * like Question, Answer, TherapeuticArea or Trial
     * it won't work for all other artifacts containing just ids and references
     * like QuestionnaireEntry or all selectors
     * @param restPath
     * @param itemName
     * @param itemText
     * @return
     */
    public String createTextItem(String restPath, String itemName, String itemText) {
        String payload = "{\"#NAME#\" : \"#VAL#\"}".replace("#NAME#", itemName).replace("#VAL#", itemText);
        return this.makeRestCall("POST", BASE_URL.concat(restPath), payload);
    }

    /**
     * Create a url from a Map of parameters
     * @param restPath
     * @param params
     * @return
     */
    public String createTextItems(String restPath, Map<String, String> params) {

        StringBuilder payloadSB = new StringBuilder();

        //The payload starts and ends with curly brackets.
        payloadSB.append("{");

        for (Map.Entry<String, String> entry : params.entrySet()) {

            String itemName = entry.getKey();
            String itemText = entry.getValue();

            String payload = "\"#NAME#\" : \"#VAL#\"".replace("#NAME#", itemName).replace("#VAL#", itemText);
            payloadSB.append(payload);
            payloadSB.append(",");
        }

        //Replace the last character with a curly bracket
        payloadSB.replace(payloadSB.length() -1, payloadSB.length() -1, "}");
        // payloadSB.append(",");

        return this.makeRestCall("POST", BASE_URL.concat(restPath), payloadSB.toString());
    }


    public String deleteItem(String restPath, int id) {
        return this.makeRestCall("DELETE", BASE_URL.concat(restPath)+"/"+id, null);
    }

    /**
     * same as above method, applicable only to text artifacts
     * @param restPath
     * @param id
     * @param itemName
     * @param itemText
     * @return
     */
    public String updateItemText(String restPath, int id, String itemName, String itemText) {
        String payload = "{\"#NAME#\" : \"#VAL#\"}".replace("#NAME#", itemName).replace("#VAL#", itemText);
        return this.makeRestCall("PUT", BASE_URL.concat(restPath)+"/"+id, payload);
    }

    /**
     *
     * @param taId
     * @param questionId
     * @return
     */
    public String createQuestionnaireEntry(int taId, int questionId) {
        String payload = "{\"question\": \""+BASE_URL+"/questions/#QID#{?projection}\",\n" +
                " \"therapeuticArea\": \""+BASE_URL+"/therapeuticareas/#TAID#\"\n" +
                "}";
        payload = payload.replace("#QID#", String.valueOf(questionId));
        payload = payload.replace("#TAID#", String.valueOf(taId));
        return this.makeRestCall("POST", BASE_URL.concat("questionnaireentries"), payload);
    }

    /*public void assingEntryAnswer(int entryId, int answerId) {
        String payload = "{\"_links\" : {\"answer\" : \"#ANSWER#\"}}".replace("#ANSWER#", "http://localhost:8080/api/answers/"+answerId);
        this.makeRestCall("PUT", BASE_URL.concat("questionnaireentries/") + entryId + "/answers", payload);
    }*/

    /**
     * partial updates do not work due to the bugs in Java7 virtual machine
     * and/or lack of support for PATCH rest method on the client side
     * Full support for this type of action is present in Java8.
     * This method is just a workaround saving all answers at once
     * but it is added here as a workaround for testing purposes
     */
    public void assingAnswersToEntry(int entryId, int[] answerIds) {
        String answer = "\"answer\" : \""+BASE_URL+"/answers/#ANS#\"";
        String allAnswers = null;
        for (Integer i : answerIds) {
            if (allAnswers==null)
                allAnswers = answer.replace("#ANS#", String.valueOf(i));
            else
                allAnswers = allAnswers + "," + answer.replace("#ANS#", String.valueOf(i));
        }
        String payload = "{\"_links\" : {#ANSWERS#}}".replace("#ANSWERS#", allAnswers);
        this.makeRestCall("PUT", BASE_URL.concat("questionnaireentries/") + entryId + "/answers", payload);
    }

    private String makeRestCall(String method, String restPath, String jsonPayload) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        HttpEntity entity;
        if ("GET".equalsIgnoreCase(method))
            entity = new HttpEntity(headers);
        else
            entity = new HttpEntity(jsonPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(restPath, HttpMethod.valueOf(method), entity, String.class);
        this.status = response.getStatusCode();
        this.responseHeaders = response.getHeaders();
        return response.toString();
    }


}

