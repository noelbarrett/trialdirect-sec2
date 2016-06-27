package com.tekenable.tdsec2.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by nbarrett on 22/06/2016.
 */
public class CustomTokenAuthFilter extends OncePerRequestFilter {


    Log log = LogFactoryImpl.getFactory().getInstance(CustomTokenAuthFilter.class);


    /**
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//        AuthCookieHolder.clearAuthCookie();
        boolean mayAuthenticate = this.mayAuthenticateRequest(request);
//
//        request.setAttribute(IGNORED_FOR_AUTH_FILTER, Boolean.valueOf(!mayAuthenticate));
        boolean continueChain = true;

        if(!this.isAlreadyAuthenticated()) {
            //continueChain = this.attemptToAuthenticate(request, response, mayAuthenticate);
        }

        if(continueChain) {
            filterChain.doFilter(request, response);
        }


    }

    /**
     *
     * @param request
     * @param response
     * @param mayAuthenticate
     * @return
     * @throws IOException
     * @throws ServletException
     */
//    private boolean attemptToAuthenticate(HttpServletRequest request, HttpServletResponse response, boolean mayAuthenticate)
//            throws IOException, ServletException {
//
//        boolean continueChain = true;
//
//        try {
//            AuthCookie e = this.parseCookie(realm, request);
//            SessionCookie sessionCookie = this.sessionCookieCreator.parseCookie(realm, request.getCookies());
//            continueChain = this.handleSuccessfulCookieAuthentication(request, response, e, sessionCookie);
//        } catch (AuthCookieParseFailException var8) {
//            continueChain = this.handleBadCookie(request, response, var8);
//        } catch (AuthCookieExpiredException var9) {
//            if(mayAuthenticate) {
//                continueChain = this.handleExpiredCookie(request, response, var9);
//            }
//        }
//
//        return continueChain;
//    }

//    @NonNull
//    protected AuthCookie parseCookie(String realm, HttpServletRequest request){
//            //throws AuthCookieParseFailException, AuthCookieExpiredException {
//
//        //return this.cookieCreator.parseCookie(realm, request.getCookies());
//        return new AuthCookie();
//    }




//    private boolean handleSuccessfulCookieAuthentication(
//            HttpServletRequest request, HttpServletResponse response, @Nonnull AuthCookie oldCookie, SessionCookie sessionCookie) {
//
//        if(this.mayAuthenticateRequest(request)) {
//
//            AuthCookie cookie = this.extendCookieLife(oldCookie, request, response);
//            Authentication auth = this.authenticationManager.authenticate(new AuthCookieAuthenticationToken(cookie));
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//
//            AuthCookieHolder.setAuthCookie(cookie);
//            SessionCookieHolder.setSessionCookie(sessionCookie);
//        }
//
//        return true;
//    }

    /**
     *
     * @return
     */
    private boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    protected boolean mayAuthenticateRequest(HttpServletRequest request) {
        //return this.urlValidationPattern.matcher(this.requestPath(request)).matches();
        return true;
    }

}
