package com.tekenable.tdsec2.model;

import com.tekenable.tdsec2.utils.DateUtils;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by nbarrett on 02/06/2016.
 *
 * The purpose of this class it to mimic the patient portal PatientLoginAttempt
 */

/**
  * TdUser Login Attempt.
  */
@Table(name = "td_login_attempt")
@Entity
@DefaultAnnotation(Nonnull.class)
public class TdLoginAttempt extends EntityWithEncryption<TdLoginAttempt.SecuredData> {

    private static final long serialVersionUID = 5241991264245151253L;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "accept_language")
    private String acceptLanguage;

//    @Column(name = "exception", columnDefinition = "TEXT")
//    @Lob
    @Transient
    private String exception;

    @Column(name = "ip_address", length = 40, nullable = false)
    private String ipAddress;

    @Column(name = "date", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "attempt_status", nullable = false)
    @Enumerated
    private TdLoginAttemptStatus attemptStatus;


    /**
     * type class for secured object.
     */
    public TdLoginAttempt() {
        super(SecuredData.class);
    }

    // CSOFF: ParameterNumber
    private TdLoginAttempt(String userAgent, String acceptLanguage, String exception, String ipAddress,
                                Date date, TdLoginAttemptStatus attemptStatus,
                                String email) {
        super(SecuredData.class);
        this.setUserAgent(userAgent);
        this.setAcceptLanguage(acceptLanguage);
        this.setException(exception);
        this.setIpAddress(ipAddress);
        this.setDate(date);
        this.setAttemptStatus(attemptStatus);

        this.getSecuredData().setEmail(email);
        this.getSecuredData().setClientIpAddress(ipAddress);
    }
    // CSON: ParameterNumber

    @Override
    protected SecuredData createSecuredData() {
        return new SecuredData();
    }


    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TdLoginAttemptStatus getAttemptStatus() {
        return attemptStatus;
    }

    public void setAttemptStatus(TdLoginAttemptStatus attemptStatus) {
        this.attemptStatus = attemptStatus;
    }

    /**
     * patient login attempt encrypted data.
     */
    public static class SecuredData {

        private String email;

        private String clientIpAddress;

        public String getClientIpAddress() {
            return clientIpAddress;
        }

        public void setClientIpAddress(String clientIpAddress) {
            this.clientIpAddress = clientIpAddress;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    /**
     * builder class.
     */
    public static class Builder {
        private String userAgent;
        private String acceptLanguage;
        private String exception;
        private String ipAddress;
        private Date date;
        private TdLoginAttemptStatus attemptStatus;
        private String email;

        /**
         * .
         * @param agent .
         * @return  .
         */
        public Builder setUserAgent(String agent) {
            this.userAgent = agent;
            return this;
        }

        /**
         * .
         * @param lang .
         * @return  .
         */
        public Builder setAcceptLanguage(String lang) {
            this.acceptLanguage = lang;
            return this;
        }

        /**
         * .
         * @param exc .
         * @return .
         */
        public Builder setException(String exc) {
            this.exception = exc;
            return this;
        }

        /**
         * .
         * @param d .
         * @return .
         */
        public Builder setDate(Date d) {
            this.date = DateUtils.defensiveCopy(d);
            return this;
        }

        /**
         * .
         * @param status .
         * @return .
         */
        public Builder setAttemptStatus(TdLoginAttemptStatus status) {
            this.attemptStatus = status;
            return this;
        }


        /**
         * .
         * @param mail .
         * @return .
         */
        public Builder setEmail(String mail) {
            this.email = mail;
            return this;
        }

        /**
         * .
         * @param ip .
         * @return .
         */
        public Builder setIpAddress(String ip) {
            this.ipAddress = ip;
            return this;
        }

        /**
         * .
         * @return .
         */
        public static Builder newInstance() {
            return new Builder();
        }

        /**
         * builds object.
         * @return .
         */
        public TdLoginAttempt build() {
            return new TdLoginAttempt(userAgent, acceptLanguage, exception,
                    ipAddress, date, attemptStatus, email);
        }

    }
}
