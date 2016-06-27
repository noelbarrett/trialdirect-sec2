package com.tekenable.tdsec2.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by nbarrett on 22/06/2016.
 */
@Component
public class Clock {

    public static final Clock DEFAULT_INSTANCE = new Clock();

    public Clock() {
    }

    public Date now() {
        return new Date();
    }
}
