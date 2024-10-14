package com.npichuzhkin.javacodetest.utils.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends WalletException {
    public InsufficientFundsException(String message) {
        super(message);
        this.setMessage(message);
        this.setHttpStatus(HttpStatus.PAYMENT_REQUIRED);
    }
}
