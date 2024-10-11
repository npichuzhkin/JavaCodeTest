package com.npichuzhkin.javacodetest.controllers;

import com.npichuzhkin.javacodetest.dto.WalletDTO;
import com.npichuzhkin.javacodetest.services.WalletService;
import com.npichuzhkin.javacodetest.utils.builders.ErrorTextBuilder;
import com.npichuzhkin.javacodetest.utils.enumerates.OperationTypes;
import com.npichuzhkin.javacodetest.utils.exceptions.IncorrectWalletReadingsException;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchOperationException;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;
import com.npichuzhkin.javacodetest.utils.exceptions.WalletException;
import com.npichuzhkin.javacodetest.utils.responses.WalletErrorResponse;
import com.npichuzhkin.javacodetest.utils.responses.WalletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("api/v1")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @RequestMapping(value = "/wallet", method = RequestMethod.POST)
    @ResponseBody
    public String changeAmount(@RequestBody @Valid WalletDTO walletDTO,
                                                   BindingResult bindingResult) throws IncorrectWalletReadingsException, NoSuchOperationException, NoSuchWalletException {
        if (bindingResult.hasErrors()){
            String errorMessage = ErrorTextBuilder.build(bindingResult);
            throw new IncorrectWalletReadingsException(errorMessage);
        }

        OperationTypes operationType = Enum.valueOf(OperationTypes.class, walletDTO.getOperationType());
        UUID id = UUID.fromString(walletDTO.getId());
        walletService.performOperation(id, operationType, walletDTO.getAmount());
        return "OK";
    }

    @RequestMapping(value = "/wallets/{id}", method = RequestMethod.GET)
    public ResponseEntity<WalletResponse> showBalance(@PathVariable("id") String id) throws NoSuchWalletException {
        UUID walletId = UUID.fromString(id);
        long balance = walletService.getAmount(walletId);
        WalletResponse walletResponse = new WalletResponse(id, balance);
        return new ResponseEntity<>(walletResponse, HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<WalletErrorResponse> handleException(WalletException e){
        WalletErrorResponse walletErrorResponse = new WalletErrorResponse(e.getMessage());
        return new ResponseEntity<>(walletErrorResponse, e.getHttpStatus());
    }
}
