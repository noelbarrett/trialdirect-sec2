package com.tekenable.tdsec2.auth;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Immutable;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by nbarrett on 22/06/2016.
 */
@Immutable
public final class AuthCookie implements Serializable {

    public static final String COOKIE_NAME_PREFIX = "Auth";

    private final long expiration;
    private final String securityRole;

    private final String hash;

    protected AuthCookie(Date expiration, String securityRole, String hash) {

        this.expiration = expiration.getTime();
        this.securityRole = securityRole;
        this.hash = hash;
    }

    public long getExpirationTime() {
        return this.expiration;
    }

    public Date getExpiration() {
        return new Date(this.expiration);
    }

    public String getSecurityRole() {
        return this.securityRole;
    }

    public String getHash() {
        return this.hash;
    }

    public String cookieName() {
        return "Auth";
    }

    /**
     *
     * @return
     */
    public String cookieValue() {
        try {
            return ('/' + Long.toHexString(this.expiration) + '/' + this.securityRole + '/' + this.getHash());
        } catch (Exception var2) {
            var2.printStackTrace();
            //throw var2;
        }

        return null;
    }

    public String toString() {
        return this.cookieName() + " : \"" + this.cookieValue() + '\"';
    }


    public Cookie toHttpCookie(@edu.umd.cs.findbugs.annotations.NonNull String domain) {

        Cookie c = new Cookie(this.cookieName(), this.cookieValue());
        initialiseCookieProperties(c, domain);
        return c;
    }

    public static void initialiseCookieProperties(Cookie c, String domain) {
        if(domain.endsWith(".firecrestclinical.com")) {
            c.setDomain(".firecrestclinical.com");
            c.setSecure(true);
        } else {
            c.setSecure(false);
        }

        c.setPath("/");
        c.setMaxAge(-1);
    }


    public int hashCode() {
        return (new HashCodeBuilder()).append(this.expiration).append(this.securityRole).append(this.getHash()).toHashCode();
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(!(obj instanceof AuthCookie)) {
            return false;
        } else {
            AuthCookie c = (AuthCookie)obj;
            return (new EqualsBuilder()).append(this.expiration, c.expiration).append(this.securityRole, c.securityRole).append(this.getHash(), c.getHash()).isEquals();
        }
    }

}
