package com.npichuzhkin.javacodetest.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class WalletException extends Exception {

    private String walletErrorMessage;
    private HttpStatus httpStatus;

    void setMessage(String message){
        this.walletErrorMessage = message;
    }
    void setHttpStatus(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }
    public WalletException (String message){
        super(message);
    }
}
