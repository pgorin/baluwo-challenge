package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static java.math.RoundingMode.CEILING;

@ToString
@EqualsAndHashCode
public class Price {

    @Column(name = "price_amount", nullable = false, scale = 2)
    @NotNull(message = "INVALID_PRICE_AMOUNT")
    @Positive(message = "INVALID_PRICE_AMOUNT")
    private BigDecimal amount;

    // required due reflection
    public Price() {
    }

    public Price(BigDecimal amount) {
        this.amount = amount.setScale(2, CEILING);
    }

    public Price(Integer amount) {
        this(new BigDecimal(amount));
    }

    @JsonProperty
    public BigDecimal amount() {
        return amount;
    }

}
