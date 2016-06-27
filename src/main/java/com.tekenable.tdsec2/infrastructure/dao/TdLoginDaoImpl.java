/*
 * Copyright © 2015 Firecrest Clinical, Ltd.
 * All rights reserved.
 */
package com.tekenable.tdsec2.infrastructure.dao;


import com.tekenable.tdsec2.infrastructure.GenericDaoImpl;
import com.tekenable.tdsec2.model.TdLogin;
import org.springframework.stereotype.Repository;

import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Td login dao.
 */
@Repository
@Transactional
public class TdLoginDaoImpl extends GenericDaoImpl<TdLogin> implements TdLoginDao {

    /**
     * constr.
     */
    public TdLoginDaoImpl(){}


    /**
     * retrieves login session by cookie uuid.
     * @param cookieUuid    - cookie uuid
     * @return  patient login session entity or null if not found (or expired)
     */
    public TdLogin retrieveByCookieUuid(String cookieUuid) {

//        Query query = session.getNamedQuery("findStockByStockCode")
//                .setString("stockCode", "7277");

        Query query = getCurrentSession().getNamedQuery("tdLogin.retrieveByCookieId");
        query.setParameter("id", cookieUuid);

        List<TdLogin> users = query.list();

        if (users != null && users.size() == 1) {
            return users.get(0);
        }

        return null;

    }

    public TdLogin findMostRecentForUserId(Long userPk) {
        Query query = getCurrentSession().getNamedQuery(TdLogin.FIND_MOST_RECENT_FOR_USER);
        query.setLong("tdUserId", userPk);

        TdLogin tdLogin = (TdLogin) query.list().get(0);

        return tdLogin;
    }

    /**
     * calls encrypt() method before save.
     * @param entity The object to save.
     */
    public TdLogin save(TdLogin entity) {
        //TODO: in future re add encryption
        //entity.getTdUser().encrypt();
        //entity.getAttempt().encrypt();

        TdLogin result = null;

        //TODO: Check if we need to use create or update
        if (entity.getPk() == null)
            result = super.create(entity);
        else
            result = super.update(entity);

        return result;
    }

}
