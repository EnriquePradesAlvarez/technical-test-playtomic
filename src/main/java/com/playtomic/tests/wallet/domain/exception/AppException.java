package com.playtomic.tests.wallet.domain.exception;

public class AppException extends RuntimeException {

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message) {
        super(message);
    }

}
