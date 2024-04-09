package com.playtomic.tests.wallet.domain.exception;

public class StripeServiceException extends RuntimeException {

    public StripeServiceException(String message) {
        super(message);
    }
}
