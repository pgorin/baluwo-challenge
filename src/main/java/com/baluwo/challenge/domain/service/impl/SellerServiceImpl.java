package com.baluwo.challenge.domain.service.impl;

import com.baluwo.challenge.domain.model.Seller;
import com.baluwo.challenge.domain.model.SellerInfo;
import com.baluwo.challenge.domain.persistence.impl.SellerList;
import com.baluwo.challenge.domain.service.SellerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Service
public class SellerServiceImpl implements SellerService {

    private final Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);

    private final SellerList list;

    @Autowired
    public SellerServiceImpl(SellerList list) {
        this.list = list;
    }

    @Override
    public Seller add(SellerInfo info) {
        Seller added = list.save(new Seller(randomUUID(), info));
        logger.info(format("Seller %s successfully added", added));
        return added;
    }

    @Override
    public Optional<Seller> update(UUID id, SellerInfo info) {
        return find(id).map(client -> {
            Seller updated = list.save(client.withInfo(info));
            logger.info(format("Seller %s successfully updated", updated));
            return updated;
        });
    }

    @Override
    public Optional<Seller> remove(UUID id) {
        return find(id).map(client -> {
            list.deleteById(client.id());
            logger.info(format("Seller %s successfully removed", client));
            return client;
        });
    }

    @Override
    public Iterable<Seller> list() {
        return list.findAll();
    }

    @Override
    public Optional<Seller> find(UUID id) {
        return list.findById(id);
    }

}