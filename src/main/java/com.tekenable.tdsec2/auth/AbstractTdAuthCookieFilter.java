package com.tekenable.tdsec2.auth;

import com.google.common.annotations.VisibleForTesting;
import com.tekenable.tdsec2.model.TdLoginValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nbarrett on 22/06/2016.
 */
public abstract class AbstractTdAuthCookieFilter extends OncePerRequestFilter {

    private static final String TD_AUTH_COOKIE = "TdAuth";

    @Autowired
    private AuthenticationHolder authenticationHolder;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Cookie authCookie = null;

        if( request.getCookies() != null ) {

            for (Cookie cookie : request.getCookies()) {

                if (TD_AUTH_COOKIE.equals(cookie.getName())) {
                    authCookie = cookie;
                    break;
                }
            }

            //TODO: add back in the isAuthenticated check
            if (!authenticationHolder.isAuthenticated() && authCookie != null) {

                TdLoginValidationResponse plvr = validateCookie("realm", authCookie.getValue());

                if (plvr == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

               TdAuthenticationToken token = new TdAuthenticationToken(plvr, authCookie.getValue());
               authenticationHolder.setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Check the validity of a cookie, and return information about the login/patient associated with it.
     * @param realmId The realm the patient is being validated for.
     * @param cookieValue The value of the cookie aka the id of the login session.
     * @return Information about the login session and the patient.
     */
    protected abstract TdLoginValidationResponse validateCookie(String realmId, String cookieValue);

//    /**
//     * Determines the realm that the request relates to.
//     * (Can be overridden to change behaviour).
//     * Defaults to SP0012's practice of putting realm as first section of URL.
//     * If overriding on a system where it is an error to have no realm, then throw a runtime exception.
//     * If null is returned, then the filter chain is continued, but {@code null} is put in the {@link AuthCookieHolder}.
//     *
//     * @param request The request made.
//     * @return The realm id.
//     */
//    private String extractRealm(HttpServletRequest request) {
//        return StringUtils.substringBetween(request.getPathInfo(), "/");
//    }


    @VisibleForTesting
    public void setAuthenticationHolder(AuthenticationHolder authenticationHolder) {
        this.authenticationHolder = authenticationHolder;
    }

    @Override
    public void destroy() {

    }
}
