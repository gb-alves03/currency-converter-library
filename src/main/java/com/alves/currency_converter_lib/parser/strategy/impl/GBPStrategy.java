package com.alves.currency_converter_lib.parser.strategy.impl;

import com.alves.currency_converter_lib.client.ExchangeRateClient;
import com.alves.currency_converter_lib.parser.strategy.CurrencyStrategy;

import java.math.BigDecimal;

public class GBPStrategy implements CurrencyStrategy {

    private final ExchangeRateClient client;

    public GBPStrategy(ExchangeRateClient client) {
        this.client = client;
    }

    @Override
    public BigDecimal convertToBRL(String rawValue) throws Exception {
        String numeric = rawValue.replace("£", "").trim();
        BigDecimal amount = new BigDecimal(numeric);
        BigDecimal rate = client.getRate("GBP", "BRL");
        return amount.multiply(rate);
    }
}
