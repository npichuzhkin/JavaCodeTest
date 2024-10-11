package com.npichuzhkin.javacodetest.utils.exceptions;

import org.springframework.http.HttpStatus;

public class NoSuchOperationException extends WalletException {
    public NoSuchOperationException(String message){
        super(message);
        this.setMessage(message);
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
