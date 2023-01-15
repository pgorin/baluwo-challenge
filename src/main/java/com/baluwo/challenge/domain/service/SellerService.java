package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Seller;
import com.baluwo.challenge.domain.model.SellerInfo;
import io.vavr.control.Option;

import java.util.UUID;

public interface SellerService {

    Seller add(SellerInfo info);

    Option<Seller> update(UUID id, SellerInfo info);

    Option<Seller> remove(UUID id);

    Iterable<Seller> list();

    Option<Seller> find(UUID id);

}
