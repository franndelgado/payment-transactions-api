package com.franndelgado.payment_transactions_api.serviceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.entity.Transaction;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.exceptions.TransactionIdNotFoundException;
import com.franndelgado.payment_transactions_api.exceptions.TransactionUserIdNotFoundException;
import com.franndelgado.payment_transactions_api.repository.TransactionRepository;
import com.franndelgado.payment_transactions_api.service.CurrencyService;
import com.franndelgado.payment_transactions_api.service.TransactionServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    @DisplayName("Should create a transaction converting the currency to ARS and save it.")
    void shouldCreateTransactionSuccessfullyWhenCurrencyIsNotARS() {

        TransactionRequestDTO transactionDTO = new TransactionRequestDTO();
        transactionDTO.setUserId("113411");
        transactionDTO.setAmount(new BigDecimal("123.00"));
        transactionDTO.setCurrency("EUR");
        transactionDTO.setBankCode("BANK123");
        transactionDTO.setRecipientAccount("DE89370400440532013000");

        when(currencyService.convertCurrencyToArs("EUR", new BigDecimal("123.00"))).thenReturn(new BigDecimal("209838"));

       Transaction savedTransaction = new Transaction(
            "1234", 
            "113411", 
            new BigDecimal("209838"), 
            "ARS", 
            TransactionStatus.APPROVED, 
            Instant.now(), 
            "BANK123", 
            "DE89370400440532013000"
        );

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionResponseDTO result = transactionService.createTransaction(transactionDTO);

        assertNotNull(result);
        assertEquals("113411", result.getUserId());
    }

    @Test
    @DisplayName("Should create a transaction directly in ARS without conversion.")
    void shouldCreateTransactionSuccessfullyWhenCurrencyIsEqualsToARS() {

        TransactionRequestDTO transactionDTO = new TransactionRequestDTO();
        transactionDTO.setUserId("113411");
        transactionDTO.setAmount(new BigDecimal("123.00"));
        transactionDTO.setCurrency("ARS");
        transactionDTO.setBankCode("BANK123");
        transactionDTO.setRecipientAccount("DE89370400440532013000");


       Transaction savedTransaction = new Transaction(
            "1234", 
            "113411", 
            new BigDecimal("123.00"), 
            "ARS", 
            TransactionStatus.APPROVED, 
            Instant.now(), 
            "BANK123", 
            "DE89370400440532013000"
        );

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionResponseDTO result = transactionService.createTransaction(transactionDTO);

        assertNotNull(result);
        assertEquals("113411", result.getUserId());
    }

    @Test
    @DisplayName("Should return the status of the transaction.")
    void shouldReturnTransactionStatus() {

        String transactionId = "1eb14fbf-53a7-42c4-a547-9d7e7cb692a5";

        Transaction transaction = new Transaction(
            "1eb14fbf-53a7-42c4-a547-9d7e7cb692a5", 
            "113411", 
            new BigDecimal("209838"), 
            "ARS", 
            TransactionStatus.APPROVED, 
            Instant.now(), 
            "BANK123", 
            "DE89370400440532013000"
        );
        
        when(transactionRepository.findById(transaction.getTransactionId())).thenReturn(Optional.of(transaction));

        TransactionStatus result = transactionService.getTransactionStatus(transactionId);

        assertEquals(TransactionStatus.APPROVED, result);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Should throw transactionId NotFound Exception when transaction Id does not exist.")
    void shouldThrowTransactionIdNotFoundExceptionWhenTransactionIdDoesNotExist() {
        when(transactionRepository.findById("transactionNotFound")).thenReturn(Optional.empty());

        assertThrows(TransactionIdNotFoundException.class, () -> transactionService.getTransactionStatus("transactionNotFound"));
    }

    @Test
    @DisplayName("Should return a page of approved transactions for user Id.")
    void shouldReturnAPageOfApprovedTransactions() {

        String userId = "113411";
        int page = 0;
        int size = 10;
        String sort = "createdAt,desc";

        Transaction transaction = new Transaction(
            "1eb14fbf-53a7-42c4-a547-9d7e7cb692a5", 
            userId, 
            new BigDecimal("209838"), 
            "ARS", 
            TransactionStatus.APPROVED, 
            Instant.now(), 
            "BANK123", 
            "DE89370400440532013000"
        );

        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Transaction> transactionPage = new PageImpl<>(List.of(transaction), pageable, 1);

        when(transactionRepository.findByUserIdAndStatus(userId, TransactionStatus.APPROVED, pageable)).thenReturn(transactionPage);

        Page<TransactionResponseDTO> resultPage = transactionService.getApprovedTransactionsByUserId(userId, page, size, sort);

        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(userId, resultPage.getContent().get(0).getUserId());
        
        verify(transactionRepository).findByUserIdAndStatus(userId, TransactionStatus.APPROVED, pageable);
    }

    @Test
    @DisplayName("Should throw Illegal Argument Exception when user Id is empty or null.")
    void shouldThrowIllegalArgumentExceptionWhenUserIdIsEmptyOrNull() {
        
        assertThrows(IllegalArgumentException.class, () -> transactionService.getApprovedTransactionsByUserId("", 0, 1, "createdAt,desc"));
        assertThrows(IllegalArgumentException.class, () -> transactionService.getApprovedTransactionsByUserId(null, 0, 1, "createdAt,desc"));
    }

    @Test
    @DisplayName("Should throw error when user Id does not exist.")
    void shouldThrowErrorWhenUserIdNotFound() {
        String userId = "0000";
        int page = 0;
        int size = 10;
        String sort = "createdAt,desc";

        String[] sortParams = sort.split(",");
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Transaction> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(transactionRepository.findByUserIdAndStatus(eq(userId), eq(TransactionStatus.APPROVED), any(Pageable.class)))
        .thenReturn(emptyPage);


        TransactionUserIdNotFoundException ex = assertThrows(TransactionUserIdNotFoundException.class,
            () -> transactionService.getApprovedTransactionsByUserId(userId, page, size, sort)
        );

        assertTrue(ex.getMessage().contains(userId));
        verify(transactionRepository).findByUserIdAndStatus(eq(userId), eq(TransactionStatus.APPROVED), any(Pageable.class));
    }
}
