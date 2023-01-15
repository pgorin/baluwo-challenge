package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.ClientInfo;
import com.baluwo.challenge.domain.service.ClientService;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static com.baluwo.challenge.domain.model.Clients.kunAguero;
import static com.baluwo.challenge.domain.model.Clients.leoMessi;
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
public class ClientControllerTest {

    @MockBean
    private ClientService service;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void clientShouldBeAdded() {
        when(service.add(new ClientInfo(leoMessi.name()))).thenReturn(leoMessi);
        ResponseEntity<String> response = restTemplate.exchange(
                post(create("/clients"))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .body(format("{\"name\":\"%s\"}", leoMessi.name())),
                String.class
        );

        assertEquals(CREATED, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", leoMessi.id(), leoMessi.name()),
                response.getBody()
        );
    }

    @Test
    public void clientCanBeUpdatedIfExists() {
        when(service.update(leoMessi.id(), new ClientInfo(kunAguero.name())))
                .thenReturn(Option.some(leoMessi.withInfo(new ClientInfo(kunAguero.name()))));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        put(create(format("/clients/%s", leoMessi.id())))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(format("{\"name\":\"%s\"}", kunAguero.name())),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", leoMessi.id(), kunAguero.name()),
                response.getBody()
        );
    }

    @Test
    public void clientCannotBeUpdatedIfNotExists() {
        when(service.update(leoMessi.id(), new ClientInfo(kunAguero.name()))).thenReturn(none());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        put(create(format("/clients/%s", leoMessi.id())))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .body(format("{\"name\":\"%s\"}", kunAguero.name())),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void clientCanBeRemovedIfExists() {
        when(service.remove(leoMessi.id())).thenReturn(Option.some(leoMessi));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        delete(create(format("/clients/%s", leoMessi.id())))
                                .accept(APPLICATION_JSON)
                                .build(),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", leoMessi.id(), leoMessi.name()),
                response.getBody()
        );
    }

    @Test
    public void clientCannotBeRemovedIfNotExists() {
        when(service.remove(leoMessi.id())).thenReturn(none());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        delete(create(format("/clients/%s", leoMessi.id())))
                                .accept(APPLICATION_JSON)
                                .build(),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void clientsCanBeListed() {
        when(service.list()).thenReturn(newArrayList(leoMessi, kunAguero));
        ResponseEntity<String> response =
                restTemplate.exchange(get(create("/clients")).accept(APPLICATION_JSON).build(), String.class);

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format(
                        "[{\"id\":\"%s\",\"name\":\"%s\"},{\"id\":\"%s\",\"name\":\"%s\"}]",
                        leoMessi.id(), leoMessi.name(),
                        kunAguero.id(), kunAguero.name()
                ),
                response.getBody()
        );
    }

    @Test
    public void clientShouldBeFoundIfExists() {
        when(service.find(leoMessi.id())).thenReturn(Option.some(leoMessi));
        ResponseEntity<String> response =
                restTemplate.exchange(
                        get(create(format("/clients/%s", leoMessi.id()))).accept(APPLICATION_JSON).build(),
                        String.class
                );

        assertEquals(OK, response.getStatusCode());
        assertEquals(
                format("{\"id\":\"%s\",\"name\":\"%s\"}", leoMessi.id(), leoMessi.name()),
                response.getBody()
        );
    }

    @Test
    public void clientShouldBeMissingIfNotExists() {
        when(service.find(leoMessi.id())).thenReturn(none());
        ResponseEntity<String> response =
                restTemplate.exchange(
                        get(create(format("/clients/%s", leoMessi.id()))).accept(APPLICATION_JSON).build(),
                        String.class
                );

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

}
