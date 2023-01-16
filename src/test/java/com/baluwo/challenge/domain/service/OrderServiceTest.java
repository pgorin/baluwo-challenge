package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.*;
import com.baluwo.challenge.domain.persistence.impl.*;
import com.baluwo.challenge.domain.service.impl.OrderServiceImpl;
import io.vavr.control.Try;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;

import static com.baluwo.challenge.domain.model.Clients.kunAguero;
import static com.baluwo.challenge.domain.model.Clients.leoMessi;
import static com.baluwo.challenge.domain.model.Products.iphone11;
import static com.baluwo.challenge.domain.model.Products.playstation5;
import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.baluwo.challenge.domain.model.Sellers.sony;
import static com.google.common.collect.Sets.newHashSet;
import static java.time.Clock.fixed;
import static java.time.Instant.parse;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.randomUUID;
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
    private final Clock clock = fixed(parse("2023-01-16T03:34:00.777Z"), UTC);

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
        this.service = new OrderServiceImpl(this.clients, this.offers, this.registry, clock);
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

    @Test
    void orderCanBeApproved() {
        clients.save(leoMessi);
        sellers.save(apple);
        sellers.save(sony);
        inventory.save(iphone11);
        inventory.save(playstation5);
        Offer offer = offers.save(new Offer(apple, iphone11, new Price(100)));
        Order order = registry.save(
                new Order(randomUUID(), leoMessi, clock.instant().atOffset(UTC))
                        .withOffer(offer, 1)
        );

        Order approved = service.approve(
                order.id(),
                new OrderApproval(kunAguero.name(), clock.instant().atOffset(UTC))
        ).get();
        assertThat(registry.findById(approved.id())).hasValue(approved);
    }

    @Test
    void approvalShouldFailIfOrderNotExists() {
        Try<Throwable> failure = service.approve(
                randomUUID(),
                new OrderApproval(kunAguero.name(), clock.instant().atOffset(UTC))
        ).failed();
        assertTrue(failure.exists(ex -> ex instanceof OrderNotFound));
    }

    @Test
    void approvalShouldFailIfOrderAlreadyApproved() {
        clients.save(leoMessi);
        sellers.save(apple);
        sellers.save(sony);
        inventory.save(iphone11);
        inventory.save(playstation5);
        Offer offer = offers.save(new Offer(apple, iphone11, new Price(100)));
        Order order = registry.save(
                new Order(randomUUID(), leoMessi, clock.instant().atOffset(UTC))
                        .withOffer(offer, 1)
        );
        service.approve(order.id(), new OrderApproval(kunAguero.name(), clock.instant().atOffset(UTC))).get();

        Try<Throwable> failure = service.approve(
                order.id(),
                new OrderApproval(leoMessi.name(), clock.instant().atOffset(UTC))
        ).failed();
        assertTrue(failure.exists(ex -> ex instanceof OrderAlreadyApproved));
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
