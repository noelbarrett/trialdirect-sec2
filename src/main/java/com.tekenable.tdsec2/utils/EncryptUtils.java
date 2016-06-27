package com.tekenable.tdsec2.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.tekenable.tdsec2.dto.TdRegisterUserRequest;
import com.tekenable.tdsec2.model.TdUser;
import org.springframework.security.crypto.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by nbarrett on 27/06/2016.
 */
public class EncryptUtils {

    public static final String key = "xfp38533tsa16953"; // 128 bit key
    public static final String initVector = "asdfsdfdsf765321"; // 16 bytes IV


    /**
     * encrypt sensitive data.
     */
    public static byte[] encrypt(TdUser tdUser) {

        byte[] bytesToEncrypt;
        byte[] result = null;

        try {
            //bytesToEncrypt = jsonMessageConverter.write(getSecuredData()).getBytes(Charsets.UTF_8);
            bytesToEncrypt = EncryptUtils.convertSecuredDataToJson(tdUser.getSecuredData()).getBytes(Charsets.UTF_8);

            String encryptedData = EncryptUtils.encrypt(EncryptUtils.key, EncryptUtils.initVector, bytesToEncrypt);
            result = encryptedData.getBytes();

        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot serialize object!", e);
        }

        return result;
    }


    /**
     *
     * @param securedData
     * @return
     */
    public static String convertSecuredDataToJson(TdUser.SecuredData securedData){

        JsonGenerator jsonGenerator = null;
        ObjectMapper objectMapper = null;

        objectMapper = new ObjectMapper();
        StringWriter sw = new StringWriter();

        try{

            jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(sw); //System.out, JsonEncoding.UTF8);
            //jsonGenerator.writeObject(securedData);
            objectMapper.writeValue(sw, securedData);

            // objectMapper.writeValueUsingView(jsonGenerator, object, String.class);

        }catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                if (jsonGenerator != null) {
                    jsonGenerator.flush();
                    jsonGenerator.close();
                }

            } catch (IOException ioEx) {}
        }

        return sw.toString();
    }

    /**
     *
     * @param jsonString
     * @return
     */
    public static TdUser.SecuredData convertJsonToSecuredData(String jsonString) {

        TdUser.SecuredData securedData = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            securedData = objectMapper.readValue(jsonString, TdUser.SecuredData.class);
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        return securedData;
    }

    /**
     *
     * @param key
     * @param initVector
     * @param value
     * @return
     */
    public static String encrypt(String key, String initVector, byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value);
            System.out.println("encrypted string: "
                    + Base64.encode(encrypted));

            return new String(Base64.encode(encrypted));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param key
     * @param initVector
     * @param encrypted
     * @return
     */
    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted.getBytes()));

            return new String(original);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {

        TdRegisterUserRequest tdRegisterUserRequest = new TdRegisterUserRequest();
        tdRegisterUserRequest.setTitle("Mr.");
        tdRegisterUserRequest.setFirstName("N");
        tdRegisterUserRequest.setLastName("Barr");
        tdRegisterUserRequest.setEmail("x@abc.com");
        tdRegisterUserRequest.setConfirmEmail("x@abc.com");
        tdRegisterUserRequest.setPassword("pass");
        tdRegisterUserRequest.setConfirmPassword("pass");
        tdRegisterUserRequest.setNpiNumber("X1235");
        tdRegisterUserRequest.setPracticeName("Practice xxxx");
        tdRegisterUserRequest.setZipCode("xxxzzzz");
        tdRegisterUserRequest.setIbanNumber("IBAN124323445");
        tdRegisterUserRequest.setSwiftCode("BIC2343");

        TdUser tdUser = new TdUser(tdRegisterUserRequest);

        byte[] encryptedData = EncryptUtils.encrypt(tdUser);
        System.out.println("encryptedData:" + new String(encryptedData));

        String decData = EncryptUtils.decrypt(EncryptUtils.key, EncryptUtils.initVector, new String(encryptedData));

        System.out.println("Decrypted data: " +decData);


    }
}
