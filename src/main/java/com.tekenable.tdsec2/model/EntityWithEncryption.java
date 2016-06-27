/*
 * Copyright © 2015 Firecrest Clinical, Ltd.
 * All rights reserved.
 */
package com.tekenable.tdsec2.model;

import com.firecrestclinical.commons.security.AesCryptography;
import com.firecrestclinical.commons.security.CryptographyFactory;
import com.firecrestclinical.commons.security.CryptographyUtils;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Entity with encryption bean.
 *
 * @param <T> - type of encrypted object.
 */
@MappedSuperclass
@Configurable
public abstract class EntityWithEncryption<T> extends EntityBean {

    @Column(name = "encryption_iv", nullable = false)
    private byte[] encryptionIv;

    @Column(name = "encryption_key", nullable = false)
    private byte[] encryptionKey;

    @Column(name = "encryption_data", nullable = false)
    @Lob
    private byte[] encryptionData;

    @Transient
    private final Class<T> clazz;

    @Transient
    private T securedData;

    @Autowired
    @Transient
    private CryptographyFactory cryptographyFactory;

//    @Autowired
//    @Transient
//    private JsonFilterHttpMessageConverter2<T> jsonMessageConverter;

    /**
     * type class for secured object.
     *
     * @param clazz - class type.
     */
    protected EntityWithEncryption(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * factory method for creating new instance of object type T.
     * @return object of type T
     */
    protected abstract T createSecuredData();

    /**
     * encrypt sensitive data.
     */
    public void encrypt() {

        AesCryptography aesCrypto = cryptographyFactory.createAes();

        if (encryptionKey == null) {
            byte[] key = CryptographyUtils.generateAesKey().getEncoded();
            this.encryptionIv = CryptographyUtils.generateSecureRandomIV().getIV();
            aesCrypto.provide(key, encryptionIv);
            this.encryptionKey = aesCrypto.getWrappedSecretKey();
        }

        aesCrypto.provideWrapped(encryptionKey, encryptionIv);
        byte[] bytesToEncrypt = null;

//        try {
//            bytesToEncrypt = jsonMessageConverter.write(getSecuredData()).getBytes(Charsets.UTF_8);
//        } catch (IOException e) {
//            throw new IllegalArgumentException("Cannot serialize object!", e);
//        }
        this.encryptionData = aesCrypto.setBytes(bytesToEncrypt).encrypt().getResult();
    }

    /**
     * decrypt objects.
     *
     * @return - decrypted object
     */
    protected T decrypt() {
        AesCryptography aesCrypto = cryptographyFactory.createAes();
        aesCrypto.provideWrapped(encryptionKey, encryptionIv);
        byte[] decryptedData = aesCrypto.setBytes(encryptionData).decrypt().getResult();

//        try {
//            return jsonMessageConverter.read(clazz, new String(decryptedData, Charsets.UTF_8));
//        } catch (IOException e) {
//            throw new IllegalArgumentException("Cannot deserialize object!", e);
//        }

        return null;
    }

    /**
     * PBKDF2 hash method.
     *
     * @param clearText - String to hash
     * @return - byte[] with hash
     */
    protected byte[] hash(String clearText) {
        if( clearText == null ) {
            return null;
        }
        return cryptographyFactory.generateHmacSha512HashFromString(clearText);
    }

    /**
     * returns decrypted object.
     *
     * @return decrypted object.
     */
    public T getSecuredData() {
        if (securedData == null ) {
            if (encryptionData != null) {
                securedData = decrypt();
            } else {
                securedData = createSecuredData();
            }
        }
        return securedData;
    }

    public void setSecuredData(T securedData) {
        this.securedData = securedData;
    }

    @VisibleForTesting
    public byte[] getEncryptionData() {
        return ArrayUtils.clone(encryptionData);
    }

    @VisibleForTesting
    public void setEncryptionData(byte[] encryptionData) {
        this.encryptionData = ArrayUtils.clone(encryptionData);
    }

    @VisibleForTesting
    public byte[] getEncryptionIv() {
        return ArrayUtils.clone(encryptionIv);
    }

    @VisibleForTesting
    public void setEncryptionIv(byte[] encryptionIv) {
        this.encryptionIv = ArrayUtils.clone(encryptionIv);
    }

    @VisibleForTesting
    public byte[] getEncryptionKey() {
        return ArrayUtils.clone(encryptionKey);
    }

    @VisibleForTesting
    public void setEncryptionKey(byte[] encryptionKey) {
        this.encryptionKey = ArrayUtils.clone(encryptionKey);
    }

    @VisibleForTesting
    public Class<T> getClazz() {
        return clazz;
    }
}
