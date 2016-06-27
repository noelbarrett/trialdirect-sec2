package com.tekenable.tdsec2.model;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by nbarrett on 03/06/2016.
 */

/**
 * Trial Direct user Successful Login Attempt.
 */
@Entity
@Table(name = "td_login")
@DefaultAnnotation(Nonnull.class)
@NamedQueries( {
        @NamedQuery(name = "tdLogin.retrieveByCookieId",
                query = "select l from TdLogin l "
                        + "where l.id=:id"),
        @NamedQuery(name = "tdLogin.findMostRecentForUserId",
            query = "select l from TdLogin l where l.tdUser.pk=:tdUserId order by l.pk desc ")
})
public class TdLogin extends EntityBean {

        public static final String FIND_MOST_RECENT_FOR_USER = "tdLogin.findMostRecentForUserId";

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "pk")
//    private Long pk;

    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "expiration_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(name = "tduser_login_status", nullable = false)
    @Enumerated
    private TdLoginStatus status = TdLoginStatus.LOGGED_OUT;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "attempt_fk")
    @Transient
    private TdLoginAttempt attempt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "td_user_fk")
    private TdUser tdUser;

    /**
     *
     */
    public TdLogin() {}

//    public Long getPk() {
//        return pk;
//    }
//
//    public void setPk(Long pk) {
//        this.pk = pk;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public TdLoginStatus getStatus() {
        return status;
    }

    public void setStatus(TdLoginStatus status) {
        this.status = status;
    }

    public TdLoginAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(TdLoginAttempt attempt) {
        this.attempt = attempt;
    }

    public TdUser getTdUser() {
        return tdUser;
    }

    public void setTdUser(TdUser tdUser) {
        this.tdUser = tdUser;
    }
}
