package com.franndelgado.payment_transactions_api.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public BigDecimal convertCurrencyToArs(String currency, BigDecimal amount) {
        
        if ("ARS".equals(currency)) return amount;
        
        return amount.multiply(getConversionRate(currency));
    }

    private BigDecimal getConversionRate(String originCurrency) {

        originCurrency = originCurrency.toUpperCase();

        if ("USD".equals(originCurrency)) return BigDecimal.valueOf(1454);
        else if ("EUR".equals(originCurrency)) return BigDecimal.valueOf(1706);
        else if ("GBP".equals(originCurrency)) return BigDecimal.valueOf(1972);
        else throw new IllegalArgumentException("Unsupported currency: " + originCurrency);
    }
}
