package com.tekenable.tdsec2.infrastructure.dao;

import com.tekenable.tdsec2.infrastructure.GenericDao;
import com.tekenable.tdsec2.model.TdUser;

/**
 * Created by nbarrett on 22/06/2016.
 */
public interface TdUserDao extends GenericDao<TdUser> {

    //TdUser findByPK(Long pk);

    //TdUser retrieveByEmailHash(byte[] emailHash);
    TdUser retrieveByEmailHash(String emailHash);

    TdUser retrieveByEmail(String email);

    TdUser findByEmail(String email);

    TdUser retrieveByUserId(String id);

    TdUser findByUserId(String id);
}
