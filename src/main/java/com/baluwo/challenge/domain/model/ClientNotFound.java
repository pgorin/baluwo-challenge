package com.baluwo.challenge.domain.model;

import java.util.UUID;

import static java.lang.String.format;

public class ClientNotFound extends RuntimeException {

    public ClientNotFound(UUID id) {
        super(format("Client %s not found", id));
    }

}
