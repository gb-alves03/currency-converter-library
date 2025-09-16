package com.alves.currency_converter_lib.parser.strategy.impl;

import com.alves.currency_converter_lib.parser.strategy.CurrencyStrategy;

import java.math.BigDecimal;

public class BRLStrategy implements CurrencyStrategy {

    @Override
    public BigDecimal convertToBRL(String rawValue) {
        String numeric = rawValue.replace("R$", "").trim();
        return new BigDecimal(numeric);
    }
}
