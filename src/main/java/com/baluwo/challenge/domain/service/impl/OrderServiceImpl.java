package com.baluwo.challenge.domain.service.impl;

import com.baluwo.challenge.domain.model.*;
import com.baluwo.challenge.domain.persistence.impl.*;
import com.baluwo.challenge.domain.service.OrderService;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.vavr.API.For;
import static io.vavr.control.Option.ofOptional;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static java.util.function.Function.identity;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final ProductInventory inventory;
    private final ClientList clients;
    private final SellerList sellers;
    private final OfferList offers;
    private final OrderRegistry registry;

    @Autowired
    public OrderServiceImpl(ProductInventory inventory,
                            ClientList clients,
                            SellerList sellers,
                            OfferList offers,
                            OrderRegistry registry) {
        this.inventory = inventory;
        this.clients = clients;
        this.sellers = sellers;
        this.offers = offers;
        this.registry = registry;
    }

    @Override
    public Try<Order> register(OrderRequest request) {
        return For(ofOptional(clients.findById(request.client()))).yield(client -> {
            List<Try<Tuple2<Offer, Integer>>> maybeOffers = For(
                    request.items().stream()
                            .map(item ->
                                    ofOptional(offers.findById(new Offer.Id(item.seller(), item.product())))
                                            .map(offer -> success(new Tuple2<>(offer, item.quantity())))
                                            .getOrElse(failure(new OfferNotFound(item.seller(), item.product())))
                            )
                            .collect(Collectors.toList())
            ).yield().toList();
            return (Try<Order>) maybeOffers.find(Try::isFailure).fold(
                    () -> success(
                            registry.save(
                                    maybeOffers.foldLeft(
                                            new Order(UUID.randomUUID(), client, OffsetDateTime.now()),
                                            (builder, item) -> builder.withOffer(item.get()._1, item.get()._2)
                                    )
                            )
                    ),
                    identity()
            );
        }).getOrElse(failure(new ClientNotFound(request.client())));
    }

    @Override
    public Iterable<Order> list() {
        return registry.findAll();
    }

}
