package com.npichuzhkin.javacodetest.utils.responses;

import lombok.Data;

@Data
public class WalletResponse extends Response {
    private String walletId;
    private long balance;

    public WalletResponse(String id, long balance){
        this.walletId = id;
        this.balance = balance;
    }
}
