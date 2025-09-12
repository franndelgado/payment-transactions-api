package com.franndelgado.payment_transactions_api.service;

import java.math.BigDecimal;

public interface CurrencyService {

    BigDecimal convertCurrencyToArs(String currency, BigDecimal amount);
}
