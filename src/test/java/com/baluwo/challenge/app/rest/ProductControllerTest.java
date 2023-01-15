package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.ProductDetails;
import com.baluwo.challenge.domain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.baluwo.challenge.domain.model.Products.iphone11;
import static com.baluwo.challenge.domain.model.Products.playstation5;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.net.URI.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @MockBean
    private ProductService service;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void productShouldBeRegistered() {
        when(service.add(new ProductDetails(playstation5.name()))).thenReturn(playstation5);
        ResponseEntity<String> response = restTemplate.exchange(
                post(create("/products"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(format("{\"name\":\"%s\"}", playstation5.name())),
                String.class
        );

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", playstation5.id(), playstation5.name()),
                response.getBody()
        );
    }

    @Test
    public void productCanBeUpdatedIfExists() {
        when(service.update(playstation5.id(), new ProductDetails(iphone11.name())))
                .thenReturn(Optional.of(playstation5.withDetails(new ProductDetails(iphone11.name()))));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        put(create(format("/products/%s", playstation5.id())))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(format("{\"name\":\"%s\"}", iphone11.name())),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", playstation5.id(), iphone11.name()),
                response.getBody()
        );
    }

    @Test
    public void productCannotBeUpdatedIfNotExists() {
        when(service.update(playstation5.id(), new ProductDetails(iphone11.name()))).thenReturn(Optional.empty());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        put(create(format("/products/%s", playstation5.id())))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(format("{\"name\":\"%s\"}", iphone11.name())),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void productCanBeRemovedIfExists() {
        when(service.remove(playstation5.id())).thenReturn(Optional.of(playstation5));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        delete(create(format("/products/%s", playstation5.id())))
                                .accept(APPLICATION_JSON)
                                .build(),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", playstation5.id(), playstation5.name()),
                response.getBody()
        );
    }

    @Test
    public void productCannotBeRemovedIfNotExists() {
        when(service.remove(playstation5.id())).thenReturn(Optional.empty());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        delete(create(format("/products/%s", playstation5.id())))
                                .accept(APPLICATION_JSON)
                                .build(),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void productsCanBeListed() {
        when(service.list()).thenReturn(newArrayList(playstation5, iphone11));
        ResponseEntity<String> response =
                restTemplate.exchange(get(create("/products")).accept(APPLICATION_JSON).build(), String.class);

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format(
                        "[{\"id\":\"%s\",\"name\":\"%s\"},{\"id\":\"%s\",\"name\":\"%s\"}]",
                        playstation5.id(), playstation5.name(),
                        iphone11.id(), iphone11.name()
                ),
                response.getBody()
        );
    }

    @Test
    public void productShouldBeFoundIfExists() {
        when(service.find(playstation5.id())).thenReturn(Optional.of(playstation5));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        get(create(format("/products/%s", playstation5.id()))).accept(APPLICATION_JSON).build(),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", playstation5.id(), playstation5.name()),
                response.getBody()
        );
    }

    @Test
    public void productShouldBeMissingIfNotExists() {
        when(service.find(playstation5.id())).thenReturn(Optional.empty());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        get(create(format("/products/%s", playstation5.id()))).accept(APPLICATION_JSON).build(),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

}
