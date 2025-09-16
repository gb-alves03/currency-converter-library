package com.alves.currency_converter_lib.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ExchangeRateClient {

    private static final String API_KEY = "a3d54c74f1aec113495b287c";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public BigDecimal getRate(String from, String to) throws Exception {
        String endpoint = BASE_URL + API_KEY + "/pair/" + from + "/" + to;
        URL url = new URL(endpoint);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (Scanner sc = new Scanner(conn.getInputStream())) {
            StringBuilder response = new StringBuilder();
            while (sc.hasNext()) {
                response.append(sc.nextLine());
            }

            JSONObject json = new JSONObject(response.toString());
            if (!"success".equalsIgnoreCase(json.getString("result"))) {
                throw new RuntimeException("API retornou erro: " + json.toString());
            }
            return json.getBigDecimal("conversion_rate");
        }
    }
}
