package com.baluwo.challenge.domain.service.impl;

import com.baluwo.challenge.domain.model.*;
import com.baluwo.challenge.domain.persistence.impl.OfferList;
import com.baluwo.challenge.domain.persistence.impl.ProductInventory;
import com.baluwo.challenge.domain.persistence.impl.SellerList;
import com.baluwo.challenge.domain.service.ProductService;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static io.vavr.API.For;
import static io.vavr.control.Option.ofOptional;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductInventory inventory;
    private final SellerList sellers;
    private final OfferList offers;

    @Autowired
    public ProductServiceImpl(ProductInventory inventory, SellerList sellers, OfferList offers) {
        this.inventory = inventory;
        this.sellers = sellers;
        this.offers = offers;
    }

    @Override
    public Product add(ProductDetails details) {
        Product added = inventory.save(new Product(randomUUID(), details));
        logger.info(format("Product %s successfully added", added));
        return added;
    }

    @Override
    public Option<Product> update(UUID id, ProductDetails details) {
        return find(id).map(product -> {
            Product updated = inventory.save(product.withDetails(details));
            logger.info(format("Product %s successfully updated", updated));
            return updated;
        });
    }

    @Override
    public Option<Product> remove(UUID id) {
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
    public Option<Product> find(UUID id) {
        return ofOptional(inventory.findById(id));
    }

    @Override
    public Try<Offer> offer(UUID productId, UUID sellerId, Price price) {
        return (Try<Offer>) For(ofOptional(inventory.findById(productId)))
                .yield(product ->
                        For(ofOptional(sellers.findById(sellerId))).yield(seller ->
                                For(ofOptional(offers.findById(new OfferId(seller, product))))
                                        .yield()
                                        .fold(
                                                () -> {
                                                    Offer added = offers.save(new Offer(seller, product, price));
                                                    logger.info(format("Offer %s successfully added", added));
                                                    return success(added);
                                                },
                                                offer -> failure(new ProductAlreadyOffered(offer))
                                        )

                        ).getOrElse(failure(new SellerNotFound()))
                ).getOrElse(failure(new ProductNotFound()));
    }

    @Override
    public Try<Iterable<Offer>> offers(UUID productId) {
        return find(productId)
                .map(product -> success(offers.findAllForProduct(product.id())))
                .getOrElse(failure(new ProductNotFound()));
    }

}