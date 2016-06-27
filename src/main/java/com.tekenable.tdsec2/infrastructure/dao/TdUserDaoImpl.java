/*
 * Copyright © 2015 Firecrest Clinical, Ltd.
 * All rights reserved.
 */
package com.tekenable.tdsec2.infrastructure.dao;

import com.firecrestclinical.commons.security.CryptographyFactory;
import com.google.common.annotations.VisibleForTesting;
import com.tekenable.tdsec2.infrastructure.GenericDaoImpl;
import com.tekenable.tdsec2.infrastructure.TdUserEntityNotFoundException;
import com.tekenable.tdsec2.model.TdUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TdUser dao.
 */
@Repository
@Transactional
public class TdUserDaoImpl extends GenericDaoImpl<TdUser> implements TdUserDao {

    @Autowired
    private CryptographyFactory cryptographyFactory;

    /**
     * constr.
     */
    public TdUserDaoImpl() {

    }

    /**
     *
     * @param pk
     * @return
     */
//    public TdUser findByPK(Long pk) {
//        return super.em.find(TdUser.class, pk);
//    }

    /**
     * retrieves TdUser by email.
     *
     * @param emailHash - email hash
     * @return user or not found
     */
    //public TdUser retrieveByEmailHash(byte[] emailHash) {
    public TdUser retrieveByEmailHash(String emailHash) {

//        TypeSafeQuery<TdUser> namedQuery = getNamedQuery(TdUser.class, "tduser.retrieveByEmailHash");
//        namedQuery.setBinary("emailHash", emailHash);
        Query query = getCurrentSession().getNamedQuery("tduser.retrieveByEmailHash");

        query.setParameter("emailHash", emailHash);

        List<TdUser> users = query.list();

        if (users != null && users.size() == 1) {
            return users.get(0);
        }

        return null;
    }

    /**
     * Finds all users belonging to the given study.
     * @param realmId Realm the study belongs to.
     * @param goid Study identifier.
     * @return The list of users.
     */
//    public List<TdUser> findByStudy(String realmId, String goid) {
//        TypeSafeQuery<TdUser> query = getNamedQuery(TdUser.class, "patient.findByStudy");
//        query.setString("realmId", realmId);
//        query.setString("goid", goid);
//        query.setEnum("status", StudyMembershipStatus.ACTIVE);
//        return query.list();
//    }

    /**
     * Retrieves a user based on their email address (returning {@code null} if not found).
     * @param email The email address
     * @return The TdUser or {@code null}.
     */
    public TdUser retrieveByEmail(String email) {
       // return retrieveByEmailHash(cryptographyFactory.generateHmacSha512HashFromString(email));
        return retrieveByEmailHash(email);
    }

    /**
     * Retrieves a user based on their email address (throwing an exception if not found).
     * @param email The email address
     * @return The TdUser.
     * @throws TdUserEntityNotFoundException user not found.
     */
    public TdUser findByEmail(String email) {

        TdUser tdUser = retrieveByEmail(email);

        if( tdUser == null ) {
            throw new TdUserEntityNotFoundException();
//                    TdUser.class,
//                    ImmutableMap.of("email", email)
//            );
        }
        return tdUser;
    }

    /**
     * Retrieve TdUser by id.
     * @param id The user's id
     * @return The user record, or null (if not found)
     */
    public TdUser retrieveByUserId(String id) {

        //TypeSafeQuery<TdUser> query = getNamedQuery(TdUser.class, "tduser.findById");
        Query query = getCurrentSession().getNamedQuery("tduser.findById");
        query.setParameter("id", id);

        return (TdUser)query.uniqueResult();
    }

    /**
     * Retrieve user by id (or throw exception).
     * @param id The user's id
     * @return The TdUser record.
     * @throws TdUserEntityNotFoundException user not found.
     */
    public TdUser findByUserId(String id) {

        TdUser tdUser = retrieveByUserId(id);

        if( tdUser == null ) {
            throw new TdUserEntityNotFoundException();
//                    TdUser.class,
//                    ImmutableMap.of("id", id)
//            );
        }
        return tdUser;
    }

    /**
     * calls encrypt() method before save.
     * @param entity The object to save.
     */
    public void saveOrUpdate(TdUser entity) {
       // entity.encrypt();
        //super.saveOrUpdate(entity);

        if (entity.getPk() == null)
            super.create(entity);
        else
            super.update(entity);
    }

    @VisibleForTesting
    public void setCryptographyFactory(CryptographyFactory cryptographyFactory) {
        this.cryptographyFactory = cryptographyFactory;
    }
}
