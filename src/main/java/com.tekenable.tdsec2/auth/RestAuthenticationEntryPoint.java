package com.tekenable.tdsec2.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nbarrett on 21/06/2016.
 *
 * Spring Security handles this automatic triggering of the authentication process with the concept of an Entry Point –
 * this is a required part of the configuration, and can be injected via the entry-point-ref attribute of the <http>
 *     element. Keeping in mind that this functionality doesn’t make sense in the context of the REST Service, the new
 *     custom entry point is defined to simply return 401 whenever it is triggered:
 *
 */
public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
//            throws IOException, ServletException {
//
//        httpServletResponse.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
//
//    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.addHeader("Access-Control-Allow-Origin", "null");
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status " + HttpServletResponse.SC_UNAUTHORIZED + " - " + authException.getMessage());
    }
}
