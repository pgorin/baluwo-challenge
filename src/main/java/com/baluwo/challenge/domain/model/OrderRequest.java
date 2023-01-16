package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public class OrderRequest {
    @NotNull(message = "INVALID_CLIENT")
    private final UUID client;
    @NotNull(message = "INVALID_ITEMS")
    @NotEmpty
    private final Set<OrderItem> items;

    public OrderRequest(UUID client, Set<OrderItem> items) {
        this.client = client;
        this.items = items;
    }

    @JsonProperty
    public UUID client() {
        return client;
    }

    @JsonProperty
    public Set<OrderItem> items() {
        return items;
    }

}
