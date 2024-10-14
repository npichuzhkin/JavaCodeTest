package com.npichuzhkin.javacodetest.controllers;

import com.npichuzhkin.javacodetest.dto.WalletDTO;
import com.npichuzhkin.javacodetest.services.WalletServiceAsynch;
import com.npichuzhkin.javacodetest.utils.builders.ErrorTextBuilder;
import com.npichuzhkin.javacodetest.utils.enumerates.OperationTypes;
import com.npichuzhkin.javacodetest.utils.exceptions.*;
import com.npichuzhkin.javacodetest.utils.responses.Response;
import com.npichuzhkin.javacodetest.utils.responses.WalletErrorResponse;
import com.npichuzhkin.javacodetest.utils.responses.WalletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Controller
@RequestMapping("api/v1")
public class WalletController {

    private final WalletServiceAsynch walletService;

    @Autowired
    public WalletController(WalletServiceAsynch walletService) {
        this.walletService = walletService;
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<WalletErrorResponse> changeAmount(@RequestBody @Valid WalletDTO walletDTO,
                                                   BindingResult bindingResult) throws IncorrectWalletReadingsException, NoSuchOperationException, NoSuchWalletException, InsufficientFundsException, ExecutionException, InterruptedException {
        if (bindingResult.hasErrors()){
            String errorMessage = ErrorTextBuilder.build(bindingResult);
            throw new IncorrectWalletReadingsException(errorMessage);
        }

        OperationTypes operationType;

        try{
            operationType = Enum.valueOf(OperationTypes.class, walletDTO.getOperationType());
        } catch (Exception e){
            throw new NoSuchOperationException("There is no operation with this type. Type should be DEPOSIT or WITHDRAW");
        }

        UUID id = UUID.fromString(walletDTO.getWalletId());
        long amount = Long.parseLong(walletDTO.getAmount());
        try{
            Future<Void> task = walletService.initWalletOperation(id, operationType, amount);
            task.get();
        } catch (ExecutionException e){
            Throwable cause = getRootCause(e.getCause());
            if (cause instanceof WalletException) {
                WalletException we = (WalletException) cause;
                return new ResponseEntity<>(new WalletErrorResponse(we.getWalletErrorMessage()), we.getHttpStatus());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/wallets/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Response> showBalance(@PathVariable("id") String id) throws NoSuchWalletException, InterruptedException {
        UUID walletId = UUID.fromString(id);
        FutureTask<Long> balance = null;
        try{
            balance = walletService.getAmount(walletId);
            WalletResponse walletResponse = new WalletResponse(id, balance.get());
            return new ResponseEntity<>(walletResponse, HttpStatus.OK);
        } catch (ExecutionException e){
            Throwable cause = getRootCause(e.getCause());
            NoSuchWalletException we = (NoSuchWalletException) cause;
            System.out.println(cause);
            return new ResponseEntity<>(new WalletErrorResponse(we.getWalletErrorMessage()), we.getHttpStatus());
        }
    }

    @ExceptionHandler(WalletException.class)
    public ResponseEntity<WalletErrorResponse> handleException(WalletException e){
        WalletErrorResponse walletErrorResponse = new WalletErrorResponse(e.getWalletErrorMessage());
        return new ResponseEntity<>(walletErrorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WalletErrorResponse> handleException(HttpMessageNotReadableException e){
        WalletErrorResponse walletErrorResponse = new WalletErrorResponse(e.getMessage());
        return new ResponseEntity<>(walletErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        return (cause == null || cause == throwable) ? throwable : getRootCause(cause);
    }
}
