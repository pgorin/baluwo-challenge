package com.baluwo.challenge.domain.persistence.impl;

import com.baluwo.challenge.domain.model.Offer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfferList extends CrudRepository<Offer, Offer.Id> {

    @Query("select o from Offer o where o.id.productId = :product")
    Iterable<Offer> findAllForProduct(UUID product);

}
