package com.tekenable.tdsec2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tekenable.tdsec2.dto.TdRegisterUserRequest;
import com.tekenable.tdsec2.utils.EncryptUtils;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.factory.annotation.Configurable;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by nbarrett on 07/06/2016.
 * This is the user table for tracking trial direct users.
 * Have named this TdUser to distinguish it from the User table which already exists and is used to save trial searches.
 *
 */


/*
 * Trial Direct User entity.
 */
@Entity
@Table(name = "td_user")
@Audited
@Configurable
@DefaultAnnotation(Nonnull.class)
@NamedQueries({
        @NamedQuery(name = "tduser.retrieveByEmailHash",
                query = "select tu from TdUser tu "
                        + "where tu.emailHash=:emailHash"
        ),
        @NamedQuery(name = "tduser.findById",
                query = "select tu from TdUser tu "
                        + "where tu.id=:id"
        )
})
public class TdUser implements Serializable {
        //extends EntityBean {
        //EntityWithEncryption<TdUser.SecuredData> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk")
    private Long pk;

    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "email_hash", nullable = false)
    private String emailHash;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "lat", nullable = false)
    private float lat;

    @Column(name = "lng", nullable = false)
    private float lng;

//    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, optional = true)
//    @JoinColumn(name = "password_fk")
    @Transient
    private TdUserPassword password; //Leaving this in for now for legacy reasons. In future we may be
    //storing the password in a seperate db.


    //TODO: this is taken from the EntityWithEncryption object to use a simple encryption method
    //where all registration details are encrypted into a single longblob in the db.
    //The contents of the securedData object will be encrypted and stored as a single blob.
    @Column(name = "encryption_data", nullable = false)
    @Lob
    private byte[] encryptionData;

//    @Transient
//    private T securedData;
    //TODO: remove this if bringing back in the EntityWithEncryption above
    @Transient
    private SecuredData securedData;


    //TODO: Adding this field for now as we dont have time to manage the above encrypted object
    //TODO: must revisit this later
    @Column(name="td_password", nullable = false)
    private String tdPassword;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "tdUser")
    @NotAudited
    private List<TdLogin> logins = new ArrayList<TdLogin>();


    public TdUser() {

        //super(SecuredData.class);
        super();
    }

    /**
     * Create the user using the registration details.
     * This will encrypt any personal data before storing the data as an encrypted blob.
     *
     * @param tdRegisterUserRequest
     */
    public TdUser(TdRegisterUserRequest tdRegisterUserRequest) {
        //super(SecuredData.class);
        super();

        //SecuredData securedData = createSecuredData();
        this.securedData = createSecuredData();

        securedData.setEmail(tdRegisterUserRequest.getEmail());
        securedData.setTitle(tdRegisterUserRequest.getTitle());
        securedData.setFirstName(tdRegisterUserRequest.getFirstName());
        securedData.setLastName(tdRegisterUserRequest.getLastName());
        securedData.setPracticeName(tdRegisterUserRequest.getPracticeName());
        securedData.setNpiNumber(tdRegisterUserRequest.getNpiNumber());
        securedData.setZipCode(tdRegisterUserRequest.getZipCode());

        securedData.setIbanNumber(tdRegisterUserRequest.getIbanNumber());
        securedData.setSwiftCode(tdRegisterUserRequest.getSwiftCode());
        //securedData.setPhone(tdRegisterUserRequest.gette);

        //Create a secure password
        this.password = new TdUserPassword(tdRegisterUserRequest.getPassword());
        this.tdPassword = tdRegisterUserRequest.getPassword();

        this.lat = tdRegisterUserRequest.getLat();
        this.lng = tdRegisterUserRequest.getLng();

        //Encrypts the email address and set the emailHash attribute
        encrypt();
    }

//    @Override
    protected SecuredData createSecuredData() {
        return new SecuredData();
    }

//    @Override
    public void encrypt() {

        encryptionData = EncryptUtils.encrypt(this);

        //super.encrypt();
        //emailHash = hash(getSecuredData().getEmail());
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    //@Override
    public void setSecuredData(SecuredData securedData) {

        //super.setSecuredData(securedData);
        this.securedData = securedData;
    }

    public SecuredData getSecuredData() {
        return securedData;

    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

//    @JsonIgnore
//    public byte[] getEmailHash() {
//        return ArrayUtils.clone(emailHash);
//    }
//
//    public void setEmailHash(byte[] emailHash) {
//        this.emailHash = ArrayUtils.clone(emailHash);
//    }


    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }

    @JsonIgnore
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    @JsonIgnore
    public TdUserPassword getPassword() {
        return password;
    }

    public void setPassword(TdUserPassword password) {
        this.password = password;

        //TODO: remove once full encryption is implemented
        if (password != null)
            this.tdPassword = password.getPassword();

    }

    @JsonIgnore
    public String getTdPassword() {
        return tdPassword;
    }
    @JsonIgnore
    public void setTdPassword(String tdPassword) {
        this.tdPassword = tdPassword;
    }

    @JsonIgnore
    public List<TdLogin> getLogins() {
        return logins;
    }

    public void setLogins(List<TdLogin> loginList) {
        this.logins = loginList;
    }


    /**
     * secured data for td user.
     */
    public static class SecuredData {

        private String email;

        private String title;
        private String firstName;
        private String lastName;
        private String zipCode;
        private String practiceName;
        private String npiNumber;

        private String ibanNumber;
        private String swiftCode;

        private String phone;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getPracticeName() {
            return practiceName;
        }

        public void setPracticeName(String practiceName) {
            this.practiceName = practiceName;
        }

        public String getNpiNumber() {
            return npiNumber;
        }

        public void setNpiNumber(String npiNumber) {
            this.npiNumber = npiNumber;
        }

        public String getIbanNumber() {
            return ibanNumber;
        }

        public void setIbanNumber(String ibanNumber) {
            this.ibanNumber = ibanNumber;
        }

        public String getSwiftCode() {
            return swiftCode;
        }

        public void setSwiftCode(String swiftCode) {
            this.swiftCode = swiftCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


    }
}
