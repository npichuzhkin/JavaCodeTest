package com.npichuzhkin.javacodetest.services;

import com.npichuzhkin.javacodetest.dao.WalletDAO;
import com.npichuzhkin.javacodetest.models.Wallet;
import com.npichuzhkin.javacodetest.utils.enumerates.OperationTypes;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchOperationException;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletService {
    private final WalletDAO walletDAO;

    @Autowired
    public WalletService(WalletDAO walletDAO) {
        this.walletDAO = walletDAO;
    }

    public long getAmount(UUID id) throws NoSuchWalletException {
        return walletDAO.getAmountById(id);
    }

    public void performOperation(UUID id, OperationTypes operationType, long amount) throws NoSuchOperationException, NoSuchWalletException {
        switch (operationType){
            case DEPOSIT:
                deposit(id, amount);
                break;
            case WITHDRAW:
                withdraw(id, amount);
                break;
            default: throw new NoSuchOperationException("There is no operation with this type");
        }
    }

    private void deposit(UUID id, long amount) throws NoSuchWalletException {
        Wallet wallet = walletDAO.findById(id);
        wallet.setAmount(wallet.getAmount() + amount);
        walletDAO.update(wallet);
    }

    private void withdraw(UUID id, long amount) throws NoSuchWalletException {
        Wallet wallet = walletDAO.findById(id);
        wallet.setAmount(wallet.getAmount() - amount);
        walletDAO.update(wallet);
    }
}
