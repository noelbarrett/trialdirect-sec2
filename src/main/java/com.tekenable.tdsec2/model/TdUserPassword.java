/*
 * Copyright © 2015 Firecrest Clinical, Ltd.
 * All rights reserved.
 */
package com.tekenable.tdsec2.model;

import com.firecrestclinical.commons.security.CryptographyUtils;
import com.google.common.annotations.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.envers.Audited;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Arrays;

/**
 * Trial Direct user Password entity.
 */
@Entity
@Table(name = "tduser_password")
@Audited
@DefaultAnnotation(Nonnull.class)
public class TdUserPassword extends EntityBean {

    public static final int DEFAULT_ITERATIONS = 100000;

    @Column(name = "hashed_password", length = 64, columnDefinition = "BINARY(64)", nullable = false)
    @Basic(fetch = FetchType.EAGER)
    private byte[] hashedPassword;

    @Column(name = "salt", length = 64, columnDefinition = "BINARY(64)", nullable = false)
    @Basic(fetch = FetchType.EAGER)
    private byte[] salt;

    @Column(name = "iterations", nullable = false)
    private int iterations;

    @Transient
    private String password;

    /**
     * constr.
     */
    public TdUserPassword() {

    }

    /**
     * constr.
     * @param clearTextPassword  - clear text password
     */
    public TdUserPassword(String clearTextPassword) {
        setPassword(clearTextPassword);
    }

    /**
     * creates password hash.
     *
     * @param clearTextPassword -- password to hash
     */
    public void setPassword(String clearTextPassword) {

        this.salt = CryptographyUtils.generateSecureRandomSalt();
        this.iterations = DEFAULT_ITERATIONS;
        this.hashedPassword = CryptographyUtils.generatePBKDF2EncodedKeyFromString(clearTextPassword,
                this.salt, this.iterations);
        this.password = clearTextPassword;
    }

    /**
     * validates user's password.
     *
     * @param clearTextPassword -- password to hash
     * @return true if password correct.
     */
    public boolean validatePassword(String clearTextPassword) {
        byte[] hashedPasswordAttempt = CryptographyUtils.generatePBKDF2EncodedKeyFromString(clearTextPassword,
                this.salt, this.iterations);
        return Arrays.equals(hashedPasswordAttempt, this.hashedPassword);
    }

    @VisibleForTesting
    public byte[] getHashedPassword() {
        return ArrayUtils.clone(hashedPassword);
    }

    @VisibleForTesting
    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = ArrayUtils.clone(hashedPassword);
    }

    @VisibleForTesting
    public int getIterations() {
        return iterations;
    }

    @VisibleForTesting
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @VisibleForTesting
    public byte[] getSalt() {
        return ArrayUtils.clone(salt);
    }

    @VisibleForTesting
    public void setSalt(byte[] salt) {
        this.salt = ArrayUtils.clone(salt);
    }

    public String getPassword() {
        return password;
    }
}
