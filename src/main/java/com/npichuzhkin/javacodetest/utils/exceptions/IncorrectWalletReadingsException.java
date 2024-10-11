package com.npichuzhkin.javacodetest.utils.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectWalletReadingsException extends WalletException {
    public IncorrectWalletReadingsException(String message){
        super(message);
        this.setMessage(message);
        this.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
