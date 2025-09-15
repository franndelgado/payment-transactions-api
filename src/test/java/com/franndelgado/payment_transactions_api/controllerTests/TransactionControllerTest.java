package com.franndelgado.payment_transactions_api.controllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franndelgado.payment_transactions_api.controller.TransactionController;
import com.franndelgado.payment_transactions_api.dto.TransactionRequest;
import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.exceptions.TransactionIdNotFoundException;
import com.franndelgado.payment_transactions_api.exceptions.TransactionUserIdNotFoundException;
import com.franndelgado.payment_transactions_api.service.TransactionServiceImpl;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionServiceImpl transactionServiceImpl;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Returns 201 Created when the transaction is created successfully.")
    void controllerTestCreateTransactionSuccessfully() throws Exception {

        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
            "113411",
            new BigDecimal("250.00"),
            "EUR",
            "BANK123",
            "DE89370400440532013000"
        );
        
        TransactionResponseDTO expectedResponse = new TransactionResponseDTO(
            "1234", 
            "113411", 
            new BigDecimal("250.00"), 
            "EUR", 
            TransactionStatus.APPROVED, 
            Instant.now(), 
            "BANK123", 
            "DE89370400440532013000"
        );

        when(transactionServiceImpl.createTransaction(requestDTO)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(expectedResponse)))
            .andExpect(status().isCreated());

        verify(transactionServiceImpl, times(1)).createTransaction(any(TransactionRequestDTO.class));
    }

    @Test
    @DisplayName("Returns 400 Bad Request Error when the TransactionRequestDTO is invalid.")
    void shouldReturnBadRequestWhenTransactionDTOIsInvalid() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/api/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Validation failed"))
            .andExpect(jsonPath("$.fields").exists());
    }

    @Test
    @DisplayName("Returns 200 Ok and the transaction status.")
    void shouldReturnTransactionStatusWhenTransactionIdIsValid() throws Exception {

        String transactionId = "1eb14fbf-53a7-42c4-a547-9d7e7cb612a5";

        when(transactionServiceImpl.getTransactionStatus(transactionId)).thenReturn(TransactionStatus.APPROVED);

        mockMvc.perform(get("/api/transactions/{transactionId}/status", transactionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("APPROVED"));

        verify(transactionServiceImpl, times(1)).getTransactionStatus(transactionId);
    }

    @Test
    @DisplayName("Returns 404 Not Found Error when transactionId does not exist.")
    void shouldReturnNotFoundWhenTransactionIdDoesNotExist() throws Exception {

        String transactionId = "1eb14fbf-53a7-42c4-a547-9d7e7cb692a5";
        
        when(transactionServiceImpl.getTransactionStatus(transactionId)).thenThrow(new TransactionIdNotFoundException(transactionId));

        mockMvc.perform(get("/api/transactions/{transactionId}/status", transactionId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("The transaction identifier '1eb14fbf-53a7-42c4-a547-9d7e7cb692a5' does not exist."));;
    }

    @Test
    @DisplayName("Returns 400 Bad Request Error when transactionId does not match with a valid UUID.")
    void shouldReturnBadRequestWhenTransactionIdIsInvalid() throws Exception {

        String invalidTransactionId = "11-ee-invalid-22";

        mockMvc.perform(get("/api/transactions/{transactionId}/status", invalidTransactionId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.transactionId").value("Transaction ID must be a valid UUID"));
    }

    @Test
    @DisplayName("Returns page of approved transactions for a userId.")
    void controllerTestGetListOfApprovedTransactionsByUserId() throws Exception {
        String userId = "113411";
        int page = 0;
        int size = 10;
        String sort = "createdAt,desc";

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setUserId(userId);

        TransactionResponseDTO transaction = new TransactionResponseDTO(
            "1234", 
            "113411", 
            new BigDecimal("250.00"), 
            "EUR", 
            TransactionStatus.APPROVED, 
            Instant.now(), 
            "BANK123", 
            "DE89370400440532013000"
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionResponseDTO> expectedPage = new PageImpl<>(List.of(transaction), pageable,1);

        when(transactionServiceImpl.getApprovedTransactionsByUserId(userId, page, size, sort)).thenReturn(expectedPage);

        mockMvc.perform(get("/api/transactions/approved")
            .param("userId", userId)
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .param("sort", sort)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(transactionServiceImpl, times(1)).getApprovedTransactionsByUserId(userId, page, size, sort);
    }

    @Test
    @DisplayName("Returns 404 Not Found Error when userId does not exist.")
    void shouldReturnNotFoundWhenTransactionUserIdDoesNotExist() throws Exception {

        String userId = "0000";
        
        when(transactionServiceImpl.getApprovedTransactionsByUserId(userId, 0, 10, "createdAt,desc")).thenThrow(new TransactionUserIdNotFoundException(userId));

        mockMvc.perform(get("/api/transactions/approved")
            .param("userId", userId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("The user identifier '0000' does not exist."));;
    }
}
