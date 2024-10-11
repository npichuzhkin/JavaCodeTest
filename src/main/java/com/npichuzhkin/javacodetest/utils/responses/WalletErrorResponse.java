package com.npichuzhkin.javacodetest.utils.responses;

import lombok.Data;

@Data
public class WalletErrorResponse {
    private String walletErrorMessage;
    private long walletErrorTime;

    public WalletErrorResponse(String measurementErrorMessage){
        this.walletErrorMessage = measurementErrorMessage;
        this.walletErrorTime = System.currentTimeMillis();
    }
}
