package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public class OrderItem {
    @NotNull(message = "INVALID_SELLER")
    private final UUID seller;
    @NotNull(message = "INVALID_PRODUCT")
    private final UUID product;
    @NotNull(message = "INVALID_QUANTITY")
    @Min(value = 1, message = "INVALID_QUANTITY")
    private final Integer quantity;

    public OrderItem(UUID seller, UUID product, Integer quantity) {
        this.seller = seller;
        this.product = product;
        this.quantity = quantity;
    }

    @JsonProperty
    public UUID seller() {
        return seller;
    }

    @JsonProperty
    public UUID product() {
        return product;
    }

    @JsonProperty
    public Integer quantity() {
        return quantity;
    }

}
