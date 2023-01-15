package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import java.math.BigDecimal;

public class Price {

    @Column(name = "price_amount", nullable = false)
    @JsonProperty
    private BigDecimal amount;

    // required due reflection
    public Price() {
    }

    public Price(BigDecimal amount) {
        this.amount = amount;
    }

    public Price(Integer amount) {
        this(new BigDecimal(amount));
    }

    public BigDecimal amount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                '}';
    }
}
