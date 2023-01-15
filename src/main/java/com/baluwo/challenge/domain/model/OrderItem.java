package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class OrderItem {
    @JsonProperty
    private final UUID seller;
    @JsonProperty
    private final UUID product;
    @JsonProperty
    private final Integer quantity;

    public OrderItem(UUID seller, UUID product, Integer quantity) {
        this.seller = seller;
        this.product = product;
        this.quantity = quantity;
    }

    public UUID seller() {
        return seller;
    }

    public UUID product() {
        return product;
    }

    public Integer quantity() {
        return quantity;
    }
}
