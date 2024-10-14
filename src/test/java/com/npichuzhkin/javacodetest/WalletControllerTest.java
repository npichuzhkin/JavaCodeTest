package com.npichuzhkin.javacodetest;

import com.npichuzhkin.javacodetest.controllers.WalletController;
import com.npichuzhkin.javacodetest.dto.WalletDTO;
import com.npichuzhkin.javacodetest.services.WalletServiceAsynch;
import com.npichuzhkin.javacodetest.utils.enumerates.OperationTypes;
import com.npichuzhkin.javacodetest.utils.exceptions.NoSuchWalletException;
import com.npichuzhkin.javacodetest.utils.responses.Response;
import com.npichuzhkin.javacodetest.utils.responses.WalletErrorResponse;
import com.npichuzhkin.javacodetest.utils.responses.WalletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class WalletControllerTest {

    @Mock
    private WalletServiceAsynch walletService;

    @InjectMocks
    private WalletController walletController;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void showBalanceReturnsBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        String walletIdString = walletId.toString();
        long expectedBalance = 100L;

        FutureTask<Long> futureTask = new FutureTask<>(() -> expectedBalance);
        when(walletService.getAmount(walletId)).thenReturn(futureTask);

        new Thread(futureTask).start();

        ResponseEntity<Response> response = walletController.showBalance(walletIdString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof WalletResponse);
        assertEquals(expectedBalance, ((WalletResponse) response.getBody()).getBalance());

        verify(walletService, times(1)).getAmount(walletId);
    }

    @Test
    public void showBalanceReturnsNotFound() throws Exception {
        UUID walletId = UUID.randomUUID();
        String walletIdString = walletId.toString();
        FutureTask<Long> futureTask = mock(FutureTask.class);

        when(futureTask.get()).thenThrow(new ExecutionException(new NoSuchWalletException("No wallet with this ID was found")));
        when(walletService.getAmount(walletId)).thenReturn(futureTask);

        ResponseEntity<Response> response = walletController.showBalance(walletIdString);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof WalletErrorResponse);
        assertEquals("No wallet with this ID was found", ((WalletErrorResponse) response.getBody()).getWalletErrorMessage());

        verify(walletService, times(1)).getAmount(walletId);
    }

    @Test
    public void changeAmountReturnsOk() throws Exception {
        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setWalletId(UUID.randomUUID().toString());
        walletDTO.setOperationType("DEPOSIT");
        walletDTO.setAmount("100");

        when(bindingResult.hasErrors()).thenReturn(false);

        Future<Void> future = mock(Future.class);
        when(walletService.initWalletOperation(any(UUID.class), any(OperationTypes.class), anyLong())).thenReturn(future);

        ResponseEntity<WalletErrorResponse> response = walletController.changeAmount(walletDTO, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(walletService, times(1)).initWalletOperation(any(UUID.class), any(OperationTypes.class), anyLong());
    }
}
