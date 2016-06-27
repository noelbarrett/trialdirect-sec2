package com.tekenable.tdsec2.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Created by nbarrett on 22/06/2016.
 */
public final class DateUtils {

    public static final String DATE_DD_MM_YYYY_PATTERN = "dd/MM/yyyy";
    public static final String DATE_MMM_PATTERN = "MMM";
    public static final String DATE_YYYY_PATTERN = "yyyy";
    public static final String DATE_DD_PATTERN = "dd";
    public static final String DATE_DD_MMM_YYYY_PATTERN = "dd/MMM/yyyy";
    public static final String DATE_DD_MMM_YYYY_HHMMSS_PATTERN = "dd/MMM/yyyy HH:mm:ss";
    public static final String DATE_DDMMYY_PATTERN = "dd/MM/yy";
    public static final String DATE_DDMMYYYY_PATTERN = "dd-MM-yyyy";
    public static final String DATE_DDMMMYY_PATTERN = "dd-MMM-yy";
    public static final String DATE_DDMMMYYYY_PATTERN = "dd-MMM-yyyy";
    public static final String DATE_DDMMYYHHMMSS_PATTERN = "dd-MM-yyyy hh:mm:ss";
    public static final String DATE_DDMMMYYHHMMSS_PATTERN = "dd-MMM-yyyy HH:mm:ss";
    public static final String DATE_YYYYMMDDHHMMSS_PATTERN = "yyyy-MM-dd hh:mm:ss";
    public static final String DATE_YYYYMMDDHHMMSS_24HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_YYYYMMDDHHMMSS_24HOUR_PATTERN_NO_BLANK = "yyyy-MM-dd:HH:mm:ss";
    public static final String DATE_YYYYMMDDHHMMSS_24HOUR_PATTERN_NO_BLANK_FILENAME_FRIENDLY = "yyyy-MM-dd-HH-mm-ss";
    public static final String DATE_YYYYMMMDD_PATTERN = "yyyy MMM dd";
    public static final String DATE_YYYYMMDDHHMMSS_PATTERN_NO_SPACE = "yyyyMMddHHmmss";
    public static final String XML_DATE = "yyyy-MM-dd";
    public static final String XML_DATE_TIME = "yyyy-MM-dd\'T\'HH:mm:ss";
    public static final String XML_DATE_TIME_MS = "yyyy-MM-dd\'T\'HH:mm:ss.SSS";
    public static final String XML_DATE_TIME_WITH_ZONE = "yyyy-MM-dd\'T\'HH:mm:ss.SSSZ";
    public static final Date FAR_FUTURE;
    private static final Date ALMOST_FAR_FUTURE;

    private DateUtils() {
    }

    public static boolean isFarFuture(@Nullable Date d) {
        return d != null && ALMOST_FAR_FUTURE.before(d);
    }

    public static DateFormat xmlDateGmtFormat() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        f.setTimeZone(TimeZone.getTimeZone("GMT"));
        return f;
    }

    public static DateFormat xmlDateTimeGmtFormat() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss", Locale.ENGLISH);
        f.setTimeZone(TimeZone.getTimeZone("GMT"));
        return f;
    }

    public static Date getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getDefault());
        return cal.getTime();
    }

    public static Date midnightTodayGmt() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTime();
    }

    public static Calendar getCalendar() {
        return new GregorianCalendar();
    }

    public static Date addMinutesToCurrentDate(int minutes) {
        Calendar cal = getCalendar();
        cal.add(12, minutes);
        return cal.getTime();
    }

    /** @deprecated */
    @Deprecated
    public static Date addDaysToCurrentDate(int minutes) {
        return addMinutesToCurrentDate(minutes);
    }

    public static Date addDaysToNow(int days) {
        Calendar cal = getCalendar();
        cal.add(5, days);
        return cal.getTime();
    }

    public static Date add(Date date, long duration, TimeUnit unit) {
        return new Date(date.getTime() + unit.toMillis(duration));
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String formatDate = "";
        if(date != null) {
            formatDate = sdf.format(date);
        }

        return formatDate;
    }

    public static String fastFormatDate(Date date, String pattern) {
        FastDateFormat fdf = FastDateFormat.getInstance(pattern);
        String formatDate = "";
        if(date != null) {
            formatDate = fdf.format(date);
        }

        return formatDate;
    }

    public static String formatDateWhenNotNull(Date date, String pattern) {
        return date == null?"":formatDate(date, pattern);
    }

    public static String convertDateToString(String strDate, String patternFormat) {
        Date date = convertStringToDate(strDate, patternFormat);
        String formatDate = formatDate(date, "dd-MM-yyyy");
        return formatDate;
    }

    public static Date convertStringToDate(String strDate, String patternFormat) {
        Date myDate = null;
        if(StringUtils.isNotBlank(patternFormat)) {
            SimpleDateFormat myDateFormat = new SimpleDateFormat(patternFormat);
            if(StringUtils.isNotBlank(strDate)) {
                try {
                    myDate = myDateFormat.parse(strDate);
                } catch (ParseException var5) {
                    return null;
                }
            }
        }

        return myDate;
    }

    public static Date convertStringToDate(String strDate, DateFormat patternFormat) {
        Date myDate = null;
        if(StringUtils.isNotBlank(strDate)) {
            try {
                myDate = patternFormat.parse(strDate);
            } catch (ParseException var4) {
                return null;
            }
        }

        return myDate;
    }

    public static boolean isValidDateStr(String date, int format) {
        try {
            DateFormat e = DateFormat.getDateInstance(format);
            e.setLenient(false);
            e.parse(date);
            return true;
        } catch (ParseException var3) {
            return false;
        } catch (IllegalArgumentException var4) {
            return false;
        }
    }

    public static String compareDate(Date firstDate, Date secondDate) {
        String relation = "";
        if(firstDate.equals(secondDate)) {
            relation = "sameDate";
        } else if(firstDate.before(secondDate)) {
            relation = "before";
        } else {
            relation = "after";
        }

        return relation;
    }

    public static Date defensiveCopy(Date date) {
        return date == null?null:new Date(date.getTime());
    }

    public static Date trimMilliseconds(Date date) {
        long seconds = date.getTime() / 1000L;
        return new Date(seconds * 1000L);
    }

    public static Long trimMilliseconds(Long milliseconds) {
        long seconds = milliseconds.longValue() / 1000L;
        return Long.valueOf(seconds * 1000L);
    }

    public static Date trimTime(Date date, TimeZone tz) {
        Calendar inputCal = Calendar.getInstance();
        inputCal.setTime(date);
        Calendar resultCal = Calendar.getInstance(tz);
        resultCal.clear();
        resultCal.set(inputCal.get(1), inputCal.get(2), inputCal.get(5));
        return resultCal.getTime();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }

    public static String currentDateYYYYMMDDHHMMSS24HOURPATTERN() {
        return formatDate(getCurrentTime(), "yyyy-MM-dd:HH:mm:ss");
    }

    public static String currentDateYYYYMMDDHHMMSS24HOURPATTERNNoBlanksFileNameFriendly() {
        return formatDate(getCurrentTime(), "yyyy-MM-dd-HH-mm-ss");
    }

    public static Timestamp buildTimestamp(String value, String format) {
        if(value != null && value.trim().length() != 0 && !value.equals("0000-00-00")) {
            try {
                return new Timestamp((new SimpleDateFormat(format)).parse(value).getTime());
            } catch (Exception var3) {
                throw new IllegalStateException("Could not parse " + value + ".");
            }
        } else {
            return null;
        }
    }

    public static Timestamp buildXMLDateTimestamp(String value) {
        return buildTimestamp(value, "yyyy-MM-dd");
    }

    public static int getDayCountFromDate(Date date) {
        return Days.daysBetween(new DateTime(date), new DateTime(getCalendar().getTime())).getDays();
    }

    static {
        try {
            FAR_FUTURE = xmlDateTimeGmtFormat().parse("9999-12-31T23:59:59");
            ALMOST_FAR_FUTURE = (new SimpleDateFormat("yyyy")).parse("9000");
        } catch (ParseException var1) {
            throw new Error(var1);
        }
    }

}
