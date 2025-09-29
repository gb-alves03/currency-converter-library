package com.alves.currency_converter_lib.service;

import com.alves.currency_converter_lib.client.ExchangeRateClient;
import com.alves.currency_converter_lib.parser.factory.CurrencyFactory;
import com.alves.currency_converter_lib.parser.strategy.CurrencyStrategy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public interface CurrencyConversionService {

    public BigDecimal convertToBRL(String rawValue) throws Exception;

}
