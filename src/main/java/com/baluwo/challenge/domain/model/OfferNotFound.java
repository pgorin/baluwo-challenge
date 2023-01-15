package com.baluwo.challenge.domain.model;

import java.util.UUID;

import static java.lang.String.format;

public class OfferNotFound extends RuntimeException {

    public OfferNotFound(UUID seller, UUID product) {
        super(format("Offer for seller %s and product %s not found", seller, product)
        );
    }

}
