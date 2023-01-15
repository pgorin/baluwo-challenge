package com.baluwo.challenge.domain.persistence.impl;

import com.baluwo.challenge.domain.model.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SellerList extends CrudRepository<Seller, UUID> {
}
