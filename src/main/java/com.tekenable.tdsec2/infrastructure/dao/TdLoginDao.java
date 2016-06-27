package com.tekenable.tdsec2.infrastructure.dao;

import com.tekenable.tdsec2.infrastructure.GenericDao;
import com.tekenable.tdsec2.model.TdLogin;

/**
 * Created by nbarrett on 22/06/2016.
 */
public interface TdLoginDao extends GenericDao<TdLogin> {

    TdLogin retrieveByCookieUuid(String cookieUuid);

    TdLogin save(TdLogin entity);

    TdLogin findMostRecentForUserId(Long userPk);
}
