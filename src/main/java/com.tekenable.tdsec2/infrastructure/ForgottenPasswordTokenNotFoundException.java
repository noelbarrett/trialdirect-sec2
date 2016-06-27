/*
 * Copyright © 2015 Firecrest Clinical, Ltd.
 * All rights reserved.
 */
package com.tekenable.tdsec2.infrastructure;

/**
 * Forgotten Password Token not found exception.
 */
public class ForgottenPasswordTokenNotFoundException extends RuntimeException {

    /**
     * exception.
     */
    public ForgottenPasswordTokenNotFoundException() {

    }

    /**
     * exception.
     *
     * @param tokenId token Id
     */
    public ForgottenPasswordTokenNotFoundException(String tokenId) {
        super("Token ID: " + tokenId);
    }
}
