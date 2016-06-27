package com.tekenable.tdsec2.utils;

/**
 * Created by nbarrett on 15/06/2016.
 * The trial direct runtime exception that can be thrown when a user makes a bad request, or a requrest
 * fails validation.
 *
 */
public class TdRunException extends RuntimeException implements ErrorCoded {

    private final ErrorCode errorCode;

    public TdRunException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDescription());
    }

    public TdRunException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public TdRunException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode.toString();
    }

    /**
     * Return the ErrorCode.
     * @return - ErrorCode
     */
    public ErrorCode getCode() {
        return errorCode;
    }

    /**
     * Error Code enum.
     */
    public enum ErrorCode {

        INTERNAL_ERROR("500", "Internal; Error"),
        ACCESS_DENIED("", "Access denied."),
        ACCESS_FORBIDDEN("", "Access forbidden."),
        USER_NOT_FOUND("", "User Not Found."),
        VALIDATION_ERROR_MANDATORY_FIELD_MISSING("", "A mandatory field was not provided: %s");


        // CHECKSTYLE:OFF

        private String code;
        private String description = "No description provided";
        // CHECKSTYLE:ON


        private ErrorCode() {
        }

        private ErrorCode(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
