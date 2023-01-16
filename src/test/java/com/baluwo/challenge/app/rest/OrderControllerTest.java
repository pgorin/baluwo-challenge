package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.*;
import com.baluwo.challenge.domain.service.OrderService;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Clock;

import static com.baluwo.challenge.domain.model.Clients.kunAguero;
import static com.baluwo.challenge.domain.model.Clients.leoMessi;
import static com.baluwo.challenge.domain.model.Products.iphone11;
import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.google.common.collect.Sets.newHashSet;
import static io.vavr.control.Try.success;
import static java.lang.String.format;
import static java.net.URI.create;
import static java.time.Clock.fixed;
import static java.time.Instant.parse;
import static java.time.ZoneOffset.UTC;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.post;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @MockBean
    private OrderService service;
    @Autowired
    private TestRestTemplate restTemplate;
    private final Clock clock = fixed(parse("2023-01-16T03:34:00.777Z"), UTC);

    @Test
    public void orderShouldBeRegistered() {
        Order registered = new Order(randomUUID(), leoMessi, clock.instant().atOffset(UTC))
                .withOffer(new Offer(apple, iphone11, new Price(100)), 2);
        when(
                service.register(
                        new OrderRequest(
                                leoMessi.id(),
                                newHashSet(new OrderItem(apple.id(), iphone11.id(), 2))
                        )
                )
        ).thenReturn(success(registered));
        ResponseEntity<String> response = restTemplate.exchange(
                post(create("/orders"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(
                                format(
                                        "{\"client\":\"%s\",\"items\":[{\"seller\":\"%s\",\"product\":\"%s\",\"quantity\":%s}]}",
                                        leoMessi.id(),
                                        apple.id(),
                                        iphone11.id(),
                                        2
                                )
                        ),
                String.class
        );

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(
                format(
                        "{\"id\":\"%s\",\"client\":{\"id\":\"%s\",\"name\":\"%s\"},\"dateTime\":\"%s\",\"offers\":[{\"seller\":{\"id\":\"%s\",\"name\":\"%s\"},\"product\":{\"id\":\"%s\",\"name\":\"%s\"},\"price\":{\"amount\":%s},\"quantity\":%s}],\"approval\":null}",
                        registered.id(),
                        leoMessi.id(),
                        leoMessi.name(),
                        clock.instant().atOffset(UTC),
                        apple.id(),
                        apple.name(),
                        iphone11.id(),
                        iphone11.name(),
                        new BigDecimal("100.00"),
                        2
                ),
                response.getBody()
        );
    }

    @Test
    public void orderShouldFailIfClientNotExists() {
        when(
                service.register(
                        new OrderRequest(
                                leoMessi.id(),
                                newHashSet(new OrderItem(apple.id(), iphone11.id(), 2))
                        )
                )
        ).thenReturn(Try.failure(new ClientNotFound(leoMessi.id())));
        ResponseEntity<String> response = restTemplate.exchange(
                post(create("/orders"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(
                                format(
                                        "{\"client\":\"%s\",\"items\":[{\"seller\":\"%s\",\"product\":\"%s\",\"quantity\":%s}]}",
                                        leoMessi.id(),
                                        apple.id(),
                                        iphone11.id(),
                                        2
                                )
                        ),
                String.class
        );

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void orderShouldFailIfAtLeastOneOfferIsNotValid() {
        when(
                service.register(
                        new OrderRequest(
                                leoMessi.id(),
                                newHashSet(new OrderItem(apple.id(), iphone11.id(), 2))
                        )
                )
        ).thenReturn(Try.failure(new OfferNotFound(apple.id(), iphone11.id())));
        ResponseEntity<String> response = restTemplate.exchange(
                post(create("/orders"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(
                                format(
                                        "{\"client\":\"%s\",\"items\":[{\"seller\":\"%s\",\"product\":\"%s\",\"quantity\":%s}]}",
                                        leoMessi.id(),
                                        apple.id(),
                                        iphone11.id(),
                                        2
                                )
                        ),
                String.class
        );

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void orderCanBeApproved() {
        Order order = new Order(randomUUID(), leoMessi, clock.instant().atOffset(UTC))
                .withOffer(new Offer(apple, iphone11, new Price(100)), 2);
        OrderApproval approval = new OrderApproval(kunAguero.name(), clock.instant().atOffset(UTC));
        when(service.approve(order.id(), approval)).thenReturn(success(order.approve(approval).get()));
        ResponseEntity<String> response = restTemplate.exchange(
                post(create(format("/orders/%s/approval", order.id())))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(
                                format(
                                        "{\"approver\":\"%s\",\"dateTime\":\"%s\"}",
                                        kunAguero.name(),
                                        clock.instant().atOffset(UTC)
                                )
                        ),
                String.class
        );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format(
                        "{\"id\":\"%s\",\"client\":{\"id\":\"%s\",\"name\":\"%s\"},\"dateTime\":\"%s\",\"offers\":[{\"seller\":{\"id\":\"%s\",\"name\":\"%s\"},\"product\":{\"id\":\"%s\",\"name\":\"%s\"},\"price\":{\"amount\":%s},\"quantity\":%s}],\"approval\":{\"approver\":\"%s\",\"dateTime\":\"%s\"}}",
                        order.id(),
                        leoMessi.id(),
                        leoMessi.name(),
                        clock.instant().atOffset(UTC),
                        apple.id(),
                        apple.name(),
                        iphone11.id(),
                        iphone11.name(),
                        new BigDecimal("100.00"),
                        2,
                        kunAguero.name(),
                        clock.instant().atOffset(UTC)
                ),
                response.getBody()
        );
    }

}
