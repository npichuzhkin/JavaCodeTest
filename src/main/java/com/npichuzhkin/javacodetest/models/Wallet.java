package com.npichuzhkin.javacodetest.models;

import lombok.Data;


import java.util.UUID;

@Data
public class Wallet {
    private UUID id;
    private long amount;
}
