package com.alves.currency_converter_lib.service;

import com.alves.currency_converter_lib.client.ExchangeRateClient;
import com.alves.currency_converter_lib.parser.factory.CurrencyFactory;
import com.alves.currency_converter_lib.parser.strategy.CurrencyStrategy;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    private final ExchangeRateClient client = new ExchangeRateClient();
    private final CurrencyFactory factory = new CurrencyFactory(client);

    private final Map<String, BigDecimal> cache = new ConcurrentHashMap<>();

    public BigDecimal convertToBRL(String rawValue) throws Exception {
        CurrencyStrategy strategy = factory.getStrategy(rawValue);

        String currencyCode = rawValue.startsWith("US$") ? "USD"
                : rawValue.startsWith("£") ? "GBP"
                : rawValue.startsWith("EUR") ? "EUR"
                : "BRL";

        if ("BRL".equals(currencyCode)) {
            return strategy.convertToBRL(rawValue);
        }

        String cacheKey = currencyCode + "->BRL";
        BigDecimal rate = cache.computeIfAbsent(cacheKey, key -> {
            try {
                return client.getRate(currencyCode, "BRL");
            } catch (Exception e) {
                throw new RuntimeException("Falha ao buscar taxa: " + e.getMessage(), e);
            }
        });

        BigDecimal amount = new BigDecimal(rawValue.replaceAll("[^0-9.,]", "").trim());
        return amount.multiply(rate);
    }
}
