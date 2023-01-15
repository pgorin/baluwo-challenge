package com.baluwo.challenge.domain.model;

import static java.util.UUID.randomUUID;

public class Products {

    public static final Product playstation5 = new Product(randomUUID(), new ProductDetails("Playstation 5"));
    public static final Product iphone11 = new Product(randomUUID(), new ProductDetails("Iphone 11"));

}
