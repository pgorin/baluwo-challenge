package com.baluwo.challenge.domain.service.impl;

import com.baluwo.challenge.domain.model.Client;
import com.baluwo.challenge.domain.model.ClientInfo;
import com.baluwo.challenge.domain.persistence.impl.ClientList;
import com.baluwo.challenge.domain.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Service
public class ClientServiceImpl implements ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientList list;

    @Autowired
    public ClientServiceImpl(ClientList list) {
        this.list = list;
    }

    @Override
    public Client add(ClientInfo info) {
        Client added = list.save(new Client(randomUUID(), info));
        logger.info(format("Client %s successfully added", added));
        return added;
    }

    @Override
    public Optional<Client> update(UUID id, ClientInfo info) {
        return find(id).map(client -> {
            Client updated = list.save(client.withInfo(info));
            logger.info(format("Client %s successfully updated", updated));
            return updated;
        });
    }

    @Override
    public Optional<Client> remove(UUID id) {
        return find(id).map(client -> {
            list.deleteById(client.id());
            logger.info(format("Client %s successfully removed", client));
            return client;
        });
    }

    @Override
    public Iterable<Client> list() {
        return list.findAll();
    }

    @Override
    public Optional<Client> find(UUID id) {
        return list.findById(id);
    }

}