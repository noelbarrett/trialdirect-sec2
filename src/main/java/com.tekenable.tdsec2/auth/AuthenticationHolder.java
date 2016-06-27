package com.tekenable.tdsec2.auth;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by nbarrett on 23/06/2016.
 */
@Component
@Lazy
public class AuthenticationHolder {

    Log logger = LogFactoryImpl.getLog(AuthenticationHolder.class);

    public AuthenticationHolder() {
    }

    /**
     *
     * @return
     */
    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.checkAuthenticationExists(authentication);
        return authentication;
    }

    /**
     *
     * @return
     */
    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    /**
     *
     * @param auth
     */
    public void setAuthentication(Authentication auth) {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    protected void checkAuthenticationExists(Authentication authentication) {
        if(authentication == null) {
            logger.error("No \'Authentication\' instance created.  This has to be programming error or incorrectly setup test");
            throw new IllegalStateException("No \'Authentication\' instance created.  This has to be programming error or incorrectly setup test");
        }
    }

    @VisibleForTesting
    public static AuthenticationHolder mockWith(final Authentication auth) {
        return new AuthenticationHolder() {

            private Authentication authentication = auth;

            public Authentication getAuthentication() {
                this.checkAuthenticationExists(this.authentication);
                return this.authentication;
            }

            public void setAuthentication(Authentication authx) {
                this.authentication = authx;
            }
        };
    }
}
