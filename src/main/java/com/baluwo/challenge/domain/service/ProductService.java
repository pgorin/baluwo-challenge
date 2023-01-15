package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Product;
import com.baluwo.challenge.domain.model.ProductDetails;

import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    Product add(ProductDetails details);

    Optional<Product> update(UUID id, ProductDetails details);

    Optional<Product> remove(UUID id);

    Iterable<Product> list();

    Optional<Product> find(UUID id);

}
