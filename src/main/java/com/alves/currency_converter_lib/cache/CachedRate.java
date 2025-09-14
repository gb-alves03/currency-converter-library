package com.alves.currency_converter_lib.cache;

import java.math.BigDecimal;
import java.time.Instant;

public class CachedRate {

    private BigDecimal rate;
    private final long timestamp;

    private static final long CACHE_EXPIRATION_MS = 3600_000;

    public CachedRate(BigDecimal rate) {
        this.rate = rate;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isExpired() {
        return Instant.now().toEpochMilli() - timestamp > CACHE_EXPIRATION_MS;
    }
}
