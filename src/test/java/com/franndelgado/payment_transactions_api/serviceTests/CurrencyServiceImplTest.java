package com.franndelgado.payment_transactions_api.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import com.franndelgado.payment_transactions_api.service.CurrencyServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyServiceImpl();
    }

    @Test
    @DisplayName("Should mantain the exact amount when currency is equals to ARS.")
    void shouldConvertCurrencyToArs() {
        
        BigDecimal amountResult =  currencyService.convertCurrencyToArs("ARS", new BigDecimal("125.00"));
        
        assertEquals(new BigDecimal("125.00"), amountResult);
    }

    @Test
    @DisplayName("Should convert currency USD to ARS correctly.")
    void shouldGetConversionRateOfUSD() {
        
        BigDecimal amountResult =  currencyService.convertCurrencyToArs("USD", new BigDecimal("125"));
        
        assertEquals(new BigDecimal("181750"), amountResult);
    }

    @Test
    @DisplayName("Should convert currency EUR to ARS correctly.")
    void shouldGetConversionRateOfEUR() {
        
        BigDecimal amountResult =  currencyService.convertCurrencyToArs("EUR", new BigDecimal("125"));
        
        assertEquals(new BigDecimal("213250"), amountResult);
    }

    @Test
    @DisplayName("Should convert currency GBP to ARS correctly.")
    void shouldGetConversionRateOfGBP() {
        
        BigDecimal amountResult =  currencyService.convertCurrencyToArs("GBP", new BigDecimal("125"));
        
        assertEquals(new BigDecimal("246500"), amountResult);
    }

    @Test
    @DisplayName("Should throw Illegal Argument Exception when recive an unsoported currency.")
    void shouldGetConversionRateOfAnUnsupportedCurrency() {
        assertThrows(IllegalArgumentException.class, () -> currencyService.convertCurrencyToArs("JPY", new BigDecimal("125")));
    }
}
