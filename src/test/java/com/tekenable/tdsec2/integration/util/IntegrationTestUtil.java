package com.tekenable.tdsec2.integration.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by nbarrett on 22/06/2016.
 */
public class IntegrationTestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static final String CORRECT_PASSWORD = "password";
    public static final String CORRECT_USERNAME = "user";

    public static final String INCORRECT_PASSWORD = "password1";
    public static final String INCORRECT_USERNAME = "user1";

    public static final String REQUEST_PARAMETER_PASSWORD = "password";
    public static final String REQUEST_PARAMETER_USERNAME = "userName";

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
