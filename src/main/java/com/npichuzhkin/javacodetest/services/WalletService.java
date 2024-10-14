package com.npichuzhkin.javacodetest.services;

import com.npichuzhkin.javacodetest.dao.WalletDAO;
import com.npichuzhkin.javacodetest.models.Wallet;
import com.npichuzhkin.javacodetest.utils.enumerates.OperationTypes;
import com.npichuzhkin.javacodetest.utils.exceptions.InsufficientFundsException;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchOperationException;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.*;

@Service
public class WalletService implements WalletServiceAsynch {
    private final WalletDAO walletDAO;
    private final BlockingQueue<FutureTask<Void>> walletOperations;
    private final Executor executor;
    private final ExecutorService executorService;

    @Autowired
    public WalletService(WalletDAO walletDAO, Executor executor) {
        this.walletDAO = walletDAO;
        this.executor = executor;
        this.executorService = Executors.newSingleThreadExecutor();
        this.walletOperations = new ArrayBlockingQueue<>(990);

        startProcessingQueue();
    }

    private void startProcessingQueue() {
        executorService.submit(() -> {
            while (true) {
                try {
                    FutureTask<Void> task = walletOperations.take();
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Async
    public FutureTask<Long> getAmount(UUID id){
        FutureTask<Long> task = new FutureTask<>(() -> walletDAO.findById(id).getAmount());
        executor.execute(task);
        return task;
    }

    @Async
    public Future<Void> initWalletOperation(UUID id, OperationTypes operationType, long amount) {
        FutureTask<Void> task = new FutureTask<>(() -> {
                switch (operationType) {
                    case DEPOSIT:
                        deposit(id, amount);
                        break;
                    case WITHDRAW:
                        withdraw(id, amount);
                        break;
                    default:
                        throw new NoSuchOperationException("There is no operation with this type. Type should be DEPOSIT or WITHDRAW");
                }
                return null;
        });

        try {
            walletOperations.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return task;
    }

    private void deposit(UUID id, long amount) throws NoSuchWalletException {
        Wallet wallet = walletDAO.findById(id);
        wallet.setAmount(wallet.getAmount() + amount);
        walletDAO.update(wallet);
    }

    private void withdraw(UUID id, long amount) throws NoSuchWalletException, InsufficientFundsException {
        Wallet wallet = walletDAO.findById(id);
        if (wallet.getAmount() < amount) throw new InsufficientFundsException("There are not enough funds in this account");
        wallet.setAmount(wallet.getAmount() - amount);
        walletDAO.update(wallet);
    }
}