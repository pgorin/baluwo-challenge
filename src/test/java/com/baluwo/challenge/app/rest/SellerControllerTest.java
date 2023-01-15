package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.SellerInfo;
import com.baluwo.challenge.domain.service.SellerService;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.baluwo.challenge.domain.model.Sellers.sony;
import static com.google.common.collect.Lists.newArrayList;
import static io.vavr.control.Option.none;
import static java.lang.String.format;
import static java.net.URI.create;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SellerControllerTest {

    @MockBean
    private SellerService service;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void sellerShouldBeAdded() {
        when(service.add(new SellerInfo(apple.name()))).thenReturn(apple);
        ResponseEntity<String> response = restTemplate.exchange(
                post(create("/sellers"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(format("{\"name\":\"%s\"}", apple.name())),
                String.class
        );

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", apple.id(), apple.name()),
                response.getBody()
        );
    }

    @Test
    public void sellerCanBeUpdatedIfExists() {
        when(service.update(apple.id(), new SellerInfo(sony.name())))
                .thenReturn(Option.some(apple.withInfo(new SellerInfo(sony.name()))));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        put(create(format("/sellers/%s", apple.id())))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(format("{\"name\":\"%s\"}", sony.name())),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", apple.id(), sony.name()),
                response.getBody()
        );
    }

    @Test
    public void sellerCannotBeUpdatedIfNotExists() {
        when(service.update(apple.id(), new SellerInfo(sony.name()))).thenReturn(none());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        put(create(format("/sellers/%s", apple.id())))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(format("{\"name\":\"%s\"}", sony.name())),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void sellerCanBeRemovedIfExists() {
        when(service.remove(apple.id())).thenReturn(Option.some(apple));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        delete(create(format("/sellers/%s", apple.id())))
                                .accept(APPLICATION_JSON)
                                .build(),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", apple.id(), apple.name()),
                response.getBody()
        );
    }

    @Test
    public void sellerCannotBeRemovedIfNotExists() {
        when(service.remove(apple.id())).thenReturn(none());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        delete(create(format("/sellers/%s", apple.id())))
                                .accept(APPLICATION_JSON)
                                .build(),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void sellersCanBeListed() {
        when(service.list()).thenReturn(newArrayList(apple, sony));
        ResponseEntity<String> response =
                restTemplate.exchange(get(create("/sellers")).accept(APPLICATION_JSON).build(), String.class);

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format(
                        "[{\"id\":\"%s\",\"name\":\"%s\"},{\"id\":\"%s\",\"name\":\"%s\"}]",
                        apple.id(), apple.name(),
                        sony.id(), sony.name()
                ),
                response.getBody()
        );
    }

    @Test
    public void sellerShouldBeFoundIfExists() {
        when(service.find(apple.id())).thenReturn(Option.some(apple));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        get(create(format("/sellers/%s", apple.id()))).accept(APPLICATION_JSON).build(),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", apple.id(), apple.name()),
                response.getBody()
        );
    }

    @Test
    public void sellerShouldBeMissingIfNotExists() {
        when(service.find(apple.id())).thenReturn(none());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        get(create(format("/sellers/%s", apple.id()))).accept(APPLICATION_JSON).build(),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

}
