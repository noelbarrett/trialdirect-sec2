package com.tekenable.tdsec2.model;

/**
 * Created by nbarrett on 02/06/2016.
 * Track TD login attempts.
 *
 */
public enum TdLoginAttemptStatus {

    SUCCESS,
    BAD_USERNAME,
    BAD_PASSWORD,
    EXCEPTION
}
