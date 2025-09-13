package com.franndelgado.payment_transactions_api.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import com.franndelgado.payment_transactions_api.constants.CurrencyServiceConstants;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public BigDecimal convertCurrencyToArs(String currency, BigDecimal amount) {

        if (CurrencyServiceConstants.CURRENCY_ARS.equals(currency)) return amount;
        
        return amount.multiply(getConversionRate(currency));
    }

    private BigDecimal getConversionRate(String originCurrency) {

        originCurrency = originCurrency.toUpperCase();

        if (CurrencyServiceConstants.CURRENCY_USD.equals(originCurrency)) return BigDecimal.valueOf(1454);
        else if (CurrencyServiceConstants.CURRENCY_EUR.equals(originCurrency)) return BigDecimal.valueOf(1706);
        else if (CurrencyServiceConstants.CURRENCY_GBP.equals(originCurrency)) return BigDecimal.valueOf(1972);
        else throw new IllegalArgumentException(CurrencyServiceConstants.UNSUPPORTED_CURRENCY_MESSAGE + originCurrency);
    }
}
