package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.*;
import com.baluwo.challenge.domain.persistence.impl.*;
import com.baluwo.challenge.domain.service.impl.OrderServiceImpl;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.baluwo.challenge.domain.model.Clients.leoMessi;
import static com.baluwo.challenge.domain.model.Products.iphone11;
import static com.baluwo.challenge.domain.model.Products.playstation5;
import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.baluwo.challenge.domain.model.Sellers.sony;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest()
public class OrderServiceTest {

    private final ProductInventory inventory;
    private final ClientList clients;
    private final SellerList sellers;
    private final OfferList offers;
    private final OrderRegistry registry;
    private final OrderServiceImpl service;

    @Autowired
    public OrderServiceTest(ProductInventory inventory,
                            ClientList clients,
                            SellerList sellers,
                            OfferList offers,
                            OrderRegistry registry) {
        this.inventory = inventory;
        this.clients = clients;
        this.sellers = sellers;
        this.offers = offers;
        this.registry = registry;
        this.service = new OrderServiceImpl(this.clients, this.offers, this.registry);
    }

    @Test
    void orderShouldBeRegistered() {
        clients.save(leoMessi);
        sellers.save(apple);
        sellers.save(sony);
        inventory.save(iphone11);
        inventory.save(playstation5);
        offers.save(new Offer(apple, iphone11, new Price(100)));
        offers.save(new Offer(sony, playstation5, new Price(200)));

        Order registered = service.register(
                new OrderRequest(
                        leoMessi.id(),
                        newHashSet(
                                new OrderItem(apple.id(), iphone11.id(), 1),
                                new OrderItem(sony.id(), playstation5.id(), 2)
                        )
                )
        ).get();
        assertThat(registry.findById(registered.id())).hasValue(registered);
    }

    @Test
    public void orderShouldFailIfClientNotExists() {
        sellers.save(apple);
        Try<Throwable> failure =
                service.register(
                        new OrderRequest(
                                leoMessi.id(),
                                newHashSet(new OrderItem(apple.id(), iphone11.id(), 1))
                        )
                ).failed();
        assertTrue(failure.exists(ex -> ex instanceof ClientNotFound));
    }

    @Test
    public void orderShouldFailIfAtLeastOneOfferIsNotValid() {
        clients.save(leoMessi);
        sellers.save(apple);
        inventory.save(iphone11);
        offers.save(new Offer(apple, iphone11, new Price(100)));
        Try<Throwable> failure = service.register(
                new OrderRequest(
                        leoMessi.id(),
                        newHashSet(
                                new OrderItem(apple.id(), iphone11.id(), 1),
                                new OrderItem(sony.id(), playstation5.id(), 2)
                        )
                )
        ).failed();
        assertTrue(failure.exists(ex -> ex instanceof OfferNotFound));
    }

    @AfterEach
    public void cleanUpEach() {
        registry.deleteAll();
        offers.deleteAll();
        clients.deleteAll();
        sellers.deleteAll();
        inventory.deleteAll();
    }

}
