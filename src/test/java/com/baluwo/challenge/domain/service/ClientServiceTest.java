package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Client;
import com.baluwo.challenge.domain.model.ClientInfo;
import com.baluwo.challenge.domain.persistence.impl.ClientList;
import com.baluwo.challenge.domain.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static com.baluwo.challenge.domain.model.Clients.kunAguero;
import static com.baluwo.challenge.domain.model.Clients.leoMessi;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        Optional<Client> maybeUpdated = service.update(leoMessi.id(), new ClientInfo(kunAguero.name()));
        assertEquals(list.findById(leoMessi.id()), maybeUpdated);
    }

    @Test
    public void clientCannotBeUpdatedIfNotExists() {
        Optional<Client> maybeUpdated = service.update(leoMessi.id(), new ClientInfo(kunAguero.name()));
        assertFalse(maybeUpdated.isPresent());
    }

    @Test
    public void clientCanBeRemovedIfExists() {
        list.save(leoMessi);
        Optional<Client> maybeRemoved = service.remove(leoMessi.id());
        assertThat(maybeRemoved).hasValue(leoMessi);
        assertFalse(list.findById(leoMessi.id()).isPresent());
    }

    @Test
    public void clientCannotBeRemovedIfNotExists() {
        Optional<Client> maybeRemoved = service.remove(leoMessi.id());
        assertFalse(maybeRemoved.isPresent());
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
        Optional<Client> maybeClient = service.find(leoMessi.id());
        assertEquals(list.findById(leoMessi.id()), maybeClient);
    }

    @Test
    public void clientShouldBeMissingIfNotExists() {
        Optional<Client> maybeClient = service.find(leoMessi.id());
        assertFalse(maybeClient.isPresent());
    }

    @AfterEach
    public void cleanUpEach() {
        list.deleteAll();
    }

}
