package com.baluwo.challenge.domain.model;

import java.util.UUID;

import static java.lang.String.format;

public class OrderNotFound extends RuntimeException {

    public OrderNotFound(UUID id) {
        super(format("Order %s not found", id));
    }

}
