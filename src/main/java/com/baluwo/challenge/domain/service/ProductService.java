package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Offer;
import com.baluwo.challenge.domain.model.Price;
import com.baluwo.challenge.domain.model.Product;
import com.baluwo.challenge.domain.model.ProductDetails;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Set;
import java.util.UUID;

public interface ProductService {

    Product add(ProductDetails details);

    Option<Product> update(UUID id, ProductDetails details);

    Option<Product> remove(UUID id);

    Iterable<Product> list();

    Option<Product> find(UUID id);

    Try<Offer> offer(UUID product, UUID seller, Price price);

    Try<Iterable<Offer>> offers(UUID product);

}
