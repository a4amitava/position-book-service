package com.jpmc.ib.exception;

/**
 * An Application exception will be thrown when the application encountered a error while processing/retrieving positions
 * This can be further enhanced with specific error code
 */
public class ApplicationException extends RuntimeException {

    private final String errorDescription;

    public ApplicationException(String errorDescription,
                                Throwable throwable) {
        super(errorDescription, throwable);
        this.errorDescription = errorDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
