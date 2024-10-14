package com.npichuzhkin.javacodetest.utils.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WalletErrorResponse extends Response {
    private String walletErrorMessage;
    private String walletErrorTime;

    public WalletErrorResponse(String measurementErrorMessage){
        this.walletErrorMessage = measurementErrorMessage;
        byte utc = 3;
        LocalDateTime time = LocalDateTime.now().plusHours(utc);
        this.walletErrorTime = time.getDayOfMonth() + "." + time.getMonthValue() + "." +  time.getYear() + " "
                + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + "." + time.getNano();
    }
}
