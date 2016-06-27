/*
 * Copyright © 2015 Firecrest Clinical, Ltd.
 * All rights reserved.
 */
package com.tekenable.tdsec2.infrastructure;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.tekenable.tdsec2.model.EntityBean;

/**
 * Indicates that an entity could not be found in the database.
 */
public class TdUserEntityNotFoundException extends RuntimeException {

    private static final Joiner.MapJoiner KEY_JOINER = Joiner.on(',').withKeyValueSeparator("=");


    public TdUserEntityNotFoundException() {
        super();
    }
    /**
     * Constructor.
     * @param entityType The type of entity that could not be found.
     * @param keys Map from key name to key value.
     */
    public TdUserEntityNotFoundException(
            Class<? extends EntityBean> entityType, ImmutableMap<String, ?> keys
    ) {
        super("Entity not found. type:" + entityType.getSimpleName() + " keys:" + KEY_JOINER.join(keys));
    }

    /**
     * Constructor with cause exception.
     * @param entityType The type of entity that could not be found.
     * @param keys Map from key name to key value.
     * @param cause The exception that caused this exception.
     */
    public TdUserEntityNotFoundException(
            Class<? extends EntityBean> entityType, ImmutableMap<String, ?> keys, Throwable cause
    ) {
        super("Entity not found. type:" + entityType.getSimpleName() + " keys:" + KEY_JOINER.join(keys), cause);
    }
}
