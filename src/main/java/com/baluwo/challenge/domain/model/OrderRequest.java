package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class OrderRequest {
    @JsonProperty
    private final UUID client;
    @JsonProperty
    private final Set<OrderItem> items;

    public OrderRequest(UUID client, Set<OrderItem> items) {
        this.client = client;
        this.items = items;
    }

    public UUID client() {
        return client;
    }

    public Set<OrderItem> items() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return client.equals(that.client) && items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, items);
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "client=" + client +
                ", items=" + items +
                '}';
    }
}
