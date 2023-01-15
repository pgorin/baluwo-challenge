package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Client;
import com.baluwo.challenge.domain.model.ClientInfo;
import io.vavr.control.Option;

import java.util.UUID;

public interface ClientService {

    Client add(ClientInfo info);

    Option<Client> update(UUID id, ClientInfo info);

    Option<Client> remove(UUID id);

    Iterable<Client> list();

    Option<Client> find(UUID id);

}
