package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Seller;
import com.baluwo.challenge.domain.model.SellerInfo;

import java.util.Optional;
import java.util.UUID;

public interface SellerService {

    Seller add(SellerInfo info);

    Optional<Seller> update(UUID id, SellerInfo info);

    Optional<Seller> remove(UUID id);

    Iterable<Seller> list();

    Optional<Seller> find(UUID id);

}
