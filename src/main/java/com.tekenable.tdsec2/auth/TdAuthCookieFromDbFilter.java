package com.tekenable.tdsec2.auth;

import com.google.common.annotations.VisibleForTesting;
import com.tekenable.tdsec2.model.TdLoginValidationResponse;
import com.tekenable.tdsec2.service.TdAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nbarrett on 22/06/2016.
 */
public class TdAuthCookieFromDbFilter extends AbstractTdAuthCookieFilter {

    @Autowired
    private TdAuthService authService;

    @Override
    @Transactional
    protected TdLoginValidationResponse validateCookie(String realmId, String cookieValue) {
        return authService.verifyLogin(cookieValue, realmId);
    }

    @VisibleForTesting
    public void setAuthService(TdAuthService authService) {
        this.authService = authService;
    }
}