package com.alves.currency_converter_lib.service;

import com.alves.currency_converter_lib.cache.CachedRate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrencyConversionService {

    private final Map<String, CachedRate> cache = new ConcurrentHashMap<>();
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("([A-Za-z$€£R]{1,3}\\$?)\\s*([0-9]+(?:[.,][0-9]{1,2})?)");

    private static final Map<String, String> CURRENCY_MAP = Map.ofEntries(
            Map.entry("R$", "BRL"),
            Map.entry("US$", "USD"),
            Map.entry("EUR", "EUR"),
            Map.entry("£", "GBP")
    );

    private final ObjectMapper mapper = new ObjectMapper();


    public BigDecimal convertToBRL(String amountParam) throws Exception {

        Matcher matcher = AMOUNT_PATTERN.matcher(amountParam.trim());

        if (!matcher.matches()) throw new IllegalArgumentException("Formato inválido" + amountParam);

        String currencySymbol = matcher.group(1).toUpperCase();
        String numberPart = matcher.group(2).replace(",", ".");
        BigDecimal amount = new BigDecimal(numberPart);

        String currencyCode = CURRENCY_MAP.get(currencySymbol);
        if (currencyCode == null) throw new IllegalArgumentException("Currency not supported" + currencySymbol);
        if (currencyCode.equals("BRL")) return amount.setScale(2, RoundingMode.HALF_UP);

        BigDecimal rate = getExchangeRate(currencyCode);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExchangeRate(String currencyCode) throws Exception{
        CachedRate cached = cache.get(currencyCode);

        if (cached != null && !cached.isExpired()) return cached.getRate();

        String urlStr = "https://api.exchangerate.host/latest?base=" + currencyCode + "&symbols=BRL";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            Map<String, Object> response = mapper.readValue(reader, Map.class);
            Map<String, Double> rates = (Map<String, Double>) response.get("rates");
            if (rates == null || !rates.containsKey("BRL")) throw new RuntimeException("Falha ao buscar taxa");
            BigDecimal rate = BigDecimal.valueOf(rates.get("BRL"));
            cache.put(currencyCode, new CachedRate(rate));

            return rate;
        }
    }

}
