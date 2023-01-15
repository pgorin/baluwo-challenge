package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}
