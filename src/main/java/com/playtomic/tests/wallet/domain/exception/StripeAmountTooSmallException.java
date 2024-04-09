package com.playtomic.tests.wallet.domain.exception;

public class StripeAmountTooSmallException extends StripeServiceException {

    public StripeAmountTooSmallException(String message) {super(message);}
}
