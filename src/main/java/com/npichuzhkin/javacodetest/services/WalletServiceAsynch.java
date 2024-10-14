package com.npichuzhkin.javacodetest.services;

import com.npichuzhkin.javacodetest.utils.enumerates.OperationTypes;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public interface WalletServiceAsynch {

    @Async
    FutureTask<Long> getAmount(UUID id) throws NoSuchWalletException;

    @Async
    Future<Void> initWalletOperation(UUID id, OperationTypes operationType, long amount);
}
