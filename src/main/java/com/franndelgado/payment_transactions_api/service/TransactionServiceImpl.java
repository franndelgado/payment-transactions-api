package com.franndelgado.payment_transactions_api.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.franndelgado.payment_transactions_api.dto.TransactionRequestDTO;
import com.franndelgado.payment_transactions_api.dto.TransactionResponseDTO;
import com.franndelgado.payment_transactions_api.entity.Transaction;
import com.franndelgado.payment_transactions_api.enums.TransactionStatus;
import com.franndelgado.payment_transactions_api.exceptions.TransactionIdNotFoundException;
import com.franndelgado.payment_transactions_api.exceptions.TransactionUserIdNotFoundException;
import com.franndelgado.payment_transactions_api.repository.TransactionRepository;
import com.franndelgado.payment_transactions_api.service.payment.PaymentMethod;
import com.franndelgado.payment_transactions_api.service.payment.PaymentMethodFactory;
import com.franndelgado.payment_transactions_api.constants.TransactionServiceConstants;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final CurrencyService currencyService;
    private final TransactionRepository transactionRepository;
    private final PaymentMethodFactory paymentMethodFactory;

    public TransactionServiceImpl(CurrencyService currencyService,TransactionRepository transactionRepository,
        PaymentMethodFactory paymentMethodFactory) {
        this.currencyService = currencyService;
        this.transactionRepository = transactionRepository;
        this.paymentMethodFactory = paymentMethodFactory;
    }

    /**
     * <b>POST Method:</b><br>
     * This method receives a TransactionRequestDTO, converts the amount to ARS if the currency is different, 
     * assigns a random status and saves the transaction to the database.
     * Finally returns a TransactionResponseDTO.
     *
     * @param transactionDTO
     * @return transactionResponseDTO
     */    
    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionDTO) {

        Transaction newTransaction = new Transaction();
        
        newTransaction.setUserId(transactionDTO.getUserId());

        BigDecimal finalAmount;
        if (!TransactionServiceConstants.CURRENCY_ARS.equals(transactionDTO.getCurrency())) {
            finalAmount = currencyService.convertCurrencyToArs(transactionDTO.getCurrency(),transactionDTO.getAmount());
        } else {
            finalAmount = transactionDTO.getAmount();
        }
        newTransaction.setAmount(finalAmount);
        newTransaction.setCurrency(TransactionServiceConstants.CURRENCY_ARS);
        newTransaction.setStatus(TransactionStatus.values()[new Random().nextInt(TransactionStatus.values().length)]);
        newTransaction.setCreatedAt(Instant.now());
        newTransaction.setBankCode(transactionDTO.getBankCode());
        newTransaction.setRecipientAccount(transactionDTO.getRecipientAccount());
        newTransaction.setPaymentType(transactionDTO.getPaymentType());

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(transactionDTO.getPaymentType());
        paymentMethod.processPayment(newTransaction);
        
        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return mapToDTO(savedTransaction);
    }

    /**
     * <b>GET Method:</b><br>
     * This method receives a Transaction Identifier, finds it in the database, 
     * and returns the status of the transaction.
     * Throws TransactionIdNotFoundException if the transaction Id does not exist.
     *
     * @param transactionId
     * @return TransactionStatus
     */
    @Override
    @Transactional(readOnly = true)
    public TransactionStatus getTransactionStatus(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new TransactionIdNotFoundException(transactionId));

        return transaction.getStatus();
    }

    /**
     * <b>GET Method:</b><br>
     * This method receives a User Identifier with pageable options, finds it in the database,
     * and returns all the approved transactions of the user.
     * Throws TransactionUserIdNotFoundException if the userId does not exist.
     *
     * @param userId
     * @param page
     * @param size
     * @param sort
     * @return Page<TransactionResponseDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getApprovedTransactionsByUserId(String userId, int page, int size, String sort) {
        
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException(TransactionServiceConstants.MISSING_USER_ID_ERROR);
        }
        if (!transactionRepository.existsByUserId(userId)) {
            throw new TransactionUserIdNotFoundException(userId);
        }

        String[] sortParams = (sort != null && sort.contains(",")) ? sort.split(",") : new String[]{"createdAt", "DESC"};
        Sort sortObj = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Transaction> transactions = transactionRepository.findByUserIdAndStatus(userId, TransactionStatus.APPROVED, pageable);

        return transactions.map(this::mapToDTO);
    }

    /**
     * This helper method maps a Transaction entity to a TransactionResponseDTO,
     * and returns it.
     * @param transaction
     * @return TransactionResponseDTO
     */
    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setTransactionId(transaction.getTransactionId());
        dto.setUserId(transaction.getUserId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setBankCode(transaction.getBankCode());
        dto.setRecipientAccount(transaction.getRecipientAccount());
        return dto;
    }
}
