package com.tekenable.tdsec2.infrastructure;

import java.util.Map;

/**
 * Created by nbarrett on 22/06/2016.
 */
public interface GenericDao<T> {
    /**
     * Method that returns the number of entries from a table that meet some
     * criteria (where clause params)
     *
     * @param params
     *            sql parameters
     * @return the number of records meeting the criteria
     */
    long countAll(Map<String, Object> params);

    T create(T t);

    void delete(Object id);

    T find(Object id);

    T update(T t);
}
