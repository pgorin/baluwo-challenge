package com.baluwo.challenge.domain.model;

import java.util.UUID;

import static java.lang.String.format;

public class SellerNotFound extends RuntimeException {

    public SellerNotFound(UUID id) {
        super(format("Seller %s not found", id));
    }

}
