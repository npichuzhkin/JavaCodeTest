package com.npichuzhkin.javacodetest.dao;

import com.npichuzhkin.javacodetest.models.Wallet;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;

import java.util.UUID;

public interface DAO {
    Wallet findById(UUID id) throws NoSuchWalletException;
    void update(Wallet wallet);
}
