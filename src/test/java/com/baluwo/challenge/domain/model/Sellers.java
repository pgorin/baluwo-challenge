package com.baluwo.challenge.domain.model;

import static java.util.UUID.randomUUID;

public class Sellers {

    public static final Seller apple = new Seller(randomUUID(), new SellerInfo("Apple"));
    public static final Seller sony = new Seller(randomUUID(), new SellerInfo("Sony"));

}
