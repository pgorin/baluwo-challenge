package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return seller.equals(orderItem.seller) && product.equals(orderItem.product) && quantity.equals(orderItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seller, product, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "seller=" + seller +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
