package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Client;
import com.baluwo.challenge.domain.model.ClientInfo;
import com.baluwo.challenge.domain.persistence.impl.ClientList;
import com.baluwo.challenge.domain.service.impl.ClientServiceImpl;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashSet;

import static com.baluwo.challenge.domain.model.Clients.kunAguero;
import static com.baluwo.challenge.domain.model.Clients.leoMessi;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class ClientServiceTest {

    private final ClientList list;
    private final ClientService service;

    @Autowired
    public ClientServiceTest(ClientList list) {
        this.list = list;
        this.service = new ClientServiceImpl(list);
    }


    @Test
    public void clientShouldBeAdded() {
        Client added = service.add(new ClientInfo(leoMessi.name()));
        assertThat(list.findById(added.id())).hasValue(added);
    }

    @Test
    public void clientCanBeUpdatedIfExists() {
        list.save(leoMessi);
        Option<Client> maybeUpdated = service.update(leoMessi.id(), new ClientInfo(kunAguero.name()));
        assertEquals(list.findById(leoMessi.id()), maybeUpdated.toJavaOptional());
    }

    @Test
    public void clientCannotBeUpdatedIfNotExists() {
        Option<Client> maybeUpdated = service.update(leoMessi.id(), new ClientInfo(kunAguero.name()));
        assertTrue(maybeUpdated.isEmpty());
    }

    @Test
    public void clientCanBeRemovedIfExists() {
        list.save(leoMessi);
        Option<Client> maybeRemoved = service.remove(leoMessi.id());
        assertTrue(maybeRemoved.contains(leoMessi));
        assertFalse(list.findById(leoMessi.id()).isPresent());
    }

    @Test
    public void clientCannotBeRemovedIfNotExists() {
        Option<Client> maybeRemoved = service.remove(leoMessi.id());
        assertTrue(maybeRemoved.isEmpty());
    }

    @Test
    public void clientsCanBeListed() {
        list.save(leoMessi);
        list.save(kunAguero);
        assertEquals(newHashSet(leoMessi, kunAguero), new HashSet<>((Collection<Client>) service.list()));
    }

    @Test
    public void clientShouldBeFoundIfExists() {
        list.save(leoMessi);
        Option<Client> maybeClient = service.find(leoMessi.id());
        assertEquals(list.findById(leoMessi.id()), maybeClient.toJavaOptional());
    }

    @Test
    public void clientShouldBeMissingIfNotExists() {
        Option<Client> maybeClient = service.find(leoMessi.id());
        assertTrue(maybeClient.isEmpty());
    }

    @AfterEach
    public void cleanUpEach() {
        list.deleteAll();
    }

}
