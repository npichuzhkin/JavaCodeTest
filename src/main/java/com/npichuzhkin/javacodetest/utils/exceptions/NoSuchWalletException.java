package com.npichuzhkin.javacodetest.utils.exceptions;

import org.springframework.http.HttpStatus;

public class NoSuchWalletException extends WalletException {
    public NoSuchWalletException(String message){
        super(message);
        this.setMessage(message);
        this.setHttpStatus(HttpStatus.NOT_FOUND);
    }
}
