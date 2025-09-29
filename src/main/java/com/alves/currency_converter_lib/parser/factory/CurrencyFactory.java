package com.alves.currency_converter_lib.parser.factory;

import com.alves.currency_converter_lib.client.ExchangeRateClient;
import com.alves.currency_converter_lib.parser.strategy.CurrencyStrategy;
import com.alves.currency_converter_lib.parser.strategy.impl.BRLStrategy;
import com.alves.currency_converter_lib.parser.strategy.impl.EURStrategy;
import com.alves.currency_converter_lib.parser.strategy.impl.GBPStrategy;
import com.alves.currency_converter_lib.parser.strategy.impl.USDStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CurrencyFactory {

    private final Map<String, Supplier<CurrencyStrategy>> strategies = new HashMap<>();

    public CurrencyFactory(ExchangeRateClient client) {
        strategies.put("US$", () -> new USDStrategy(client));
        strategies.put("£",   () -> new GBPStrategy(client));
        strategies.put("EUR", () -> new EURStrategy(client));
        strategies.put("R$",  BRLStrategy::new);
    }

    public CurrencyStrategy getStrategy(String rawValue) {
        for (Map.Entry<String, Supplier<CurrencyStrategy>> entry : strategies.entrySet()) {
            if (rawValue.startsWith(entry.getKey())) {
                return entry.getValue().get();
            }
        }
        throw new IllegalArgumentException("Moeda não suportada: " + rawValue);
    }
}
