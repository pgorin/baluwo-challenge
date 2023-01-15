package com.baluwo.challenge.domain.persistence.impl;

import com.baluwo.challenge.domain.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientList extends CrudRepository<Client, UUID> {
}
