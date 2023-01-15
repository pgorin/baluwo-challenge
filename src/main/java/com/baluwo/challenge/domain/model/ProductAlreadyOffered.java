package com.baluwo.challenge.domain.model;

import static java.lang.String.format;

public class ProductAlreadyOffered extends RuntimeException {

    public ProductAlreadyOffered(Offer offer) {
        super(
                format(
                        "Product %s already offered by seller %s with price %s",
                        offer.product(),
                        offer.seller(),
                        offer.price()
                )
        );
    }

}
