package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Client;
import com.baluwo.challenge.domain.model.ClientInfo;

import java.util.Optional;
import java.util.UUID;

public interface ClientService {

    Client add(ClientInfo info);

    Optional<Client> update(UUID id, ClientInfo info);

    Optional<Client> remove(UUID id);

    Iterable<Client> list();

    Optional<Client> find(UUID id);

}
