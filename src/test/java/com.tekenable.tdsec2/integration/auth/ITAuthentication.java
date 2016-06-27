package com.tekenable.tdsec2.integration.auth;

import com.tekenable.tdsec2.integration.RestTestResourceTemplate;
import com.tekenable.tdsec2.integration.util.IntegrationTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by nbarrett on 22/06/2016.
 */
public class ITAuthentication extends RestTestResourceTemplate {

    private static final String LOGIN_URL = "/loginForm"; ///api/login
    private static  final String LOGOUT_URL = "/rest/global/logins/current"; ///api/logout

    private MockMvc mockMvc;

    @Before
    public void setUp() {
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .addFilters(springSecurityFilterChain)
//                .build();
    }

    @Test
    public void loginWithCorrectCredentials() throws Exception {

        Map<String, String> params = new HashMap<String, String>(2);
        params.put(IntegrationTestUtil.REQUEST_PARAMETER_USERNAME, IntegrationTestUtil.CORRECT_USERNAME);
        params.put(IntegrationTestUtil.REQUEST_PARAMETER_PASSWORD, IntegrationTestUtil.CORRECT_PASSWORD);

        String output = this.createTextItems("loginForm", params);
        System.out.println(output);
        //assertTrue(RestTestResourceTemplate.REST_TEST_DESC, this.getStatus().is2xxSuccessful());

//        mockMvc.perform(post(LOGIN_URL)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param(IntegrationTestUtil.REQUEST_PARAMETER_USERNAME, IntegrationTestUtil.CORRECT_USERNAME)
//                .param(IntegrationTestUtil.REQUEST_PARAMETER_PASSWORD, IntegrationTestUtil.CORRECT_PASSWORD)
//        )
//                .andExpect(status().isOk());




    }

}
