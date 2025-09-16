package com.alves.currency_converter_lib.parser.strategy;

import java.math.BigDecimal;

public interface CurrencyStrategy {
    BigDecimal convertToBRL(String rawValue) throws Exception;
}
