package com.baluwo.challenge.domain.model;

import java.util.UUID;

import static java.lang.String.format;

public class ProductNotFound extends RuntimeException {

    public ProductNotFound(UUID id) {
        super(format("Product %s not found", id));
    }

}
