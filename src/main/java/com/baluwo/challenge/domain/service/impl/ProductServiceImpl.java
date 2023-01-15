package com.baluwo.challenge.domain.service.impl;

import com.baluwo.challenge.domain.model.Product;
import com.baluwo.challenge.domain.model.ProductDetails;
import com.baluwo.challenge.domain.persistence.impl.ProductInventory;
import com.baluwo.challenge.domain.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductInventory inventory;

    @Autowired
    public ProductServiceImpl(ProductInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Product add(ProductDetails details) {
        Product added = inventory.save(new Product(randomUUID(), details));
        logger.info(format("Product %s successfully added", added));
        return added;
    }

    @Override
    public Optional<Product> update(UUID id, ProductDetails details) {
        return find(id).map(product -> {
            Product updated = inventory.save(product.withDetails(details));
            logger.info(format("Product %s successfully updated", updated));
            return updated;
        });
    }

    @Override
    public Optional<Product> remove(UUID id) {
        return find(id).map(product -> {
            inventory.deleteById(product.id());
            logger.info(format("Product %s successfully removed", product));
            return product;
        });
    }

    @Override
    public Iterable<Product> list() {
        return inventory.findAll();
    }

    @Override
    public Optional<Product> find(UUID id) {
        return inventory.findById(id);
    }

}