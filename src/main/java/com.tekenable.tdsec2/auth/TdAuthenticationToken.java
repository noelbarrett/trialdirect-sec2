package com.tekenable.tdsec2.auth;

import com.tekenable.tdsec2.model.TdLoginValidationResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.util.Collections;

/**
 * Created by nbarrett on 23/06/2016.
 */
public class TdAuthenticationToken extends AbstractAuthenticationToken {

    private static final GrantedAuthority TD_USER_GA = new GrantedAuthorityImpl("tduser");

    private final String cookieUuid;

    private final TdLoginValidationResponse tdLoginValidationResponse;

    /**
     * Constructor.
     *
     * @param tdLoginValidationResponse roles granted
     * @param cookieUuid   cookieUuid issued in email link
     */
    public TdAuthenticationToken(TdLoginValidationResponse tdLoginValidationResponse, String cookieUuid) {
        super(Collections.singletonList(TD_USER_GA));

        this.tdLoginValidationResponse = tdLoginValidationResponse;
        this.cookieUuid = cookieUuid;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return tdLoginValidationResponse.getEmail();
    }

    public String getCookieUuid() {
        return cookieUuid;
    }

    public String getEmail() {
        return tdLoginValidationResponse.getEmail();
    }

    public String getName() {
        return tdLoginValidationResponse.getName();
    }


}
