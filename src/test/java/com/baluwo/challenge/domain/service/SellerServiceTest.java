package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Seller;
import com.baluwo.challenge.domain.model.SellerInfo;
import com.baluwo.challenge.domain.persistence.impl.SellerList;
import com.baluwo.challenge.domain.service.impl.SellerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static com.baluwo.challenge.domain.model.Sellers.sony;
import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest()
public class SellerServiceTest {

    private final SellerList list;
    private final SellerService service;

    @Autowired
    public SellerServiceTest(SellerList list) {
        this.list = list;
        this.service = new SellerServiceImpl(list);
    }


    @Test
    public void sellerShouldBeAdded() {
        Seller added = service.add(new SellerInfo(apple.name()));
        assertThat(list.findById(added.id())).hasValue(added);
    }

    @Test
    public void sellerCanBeUpdatedIfExists() {
        list.save(apple);
        Optional<Seller> maybeUpdated = service.update(apple.id(), new SellerInfo(sony.name()));
        assertEquals(list.findById(apple.id()), maybeUpdated);
    }

    @Test
    public void sellerCannotBeUpdatedIfNotExists() {
        Optional<Seller> maybeUpdated = service.update(apple.id(), new SellerInfo(sony.name()));
        assertFalse(maybeUpdated.isPresent());
    }

    @Test
    public void sellerCanBeRemovedIfExists() {
        list.save(apple);
        Optional<Seller> maybeRemoved = service.remove(apple.id());
        assertThat(maybeRemoved).hasValue(apple);
        assertFalse(list.findById(apple.id()).isPresent());
    }

    @Test
    public void sellerCannotBeRemovedIfNotExists() {
        Optional<Seller> maybeRemoved = service.remove(apple.id());
        assertFalse(maybeRemoved.isPresent());
    }

    @Test
    public void sellersCanBeListed() {
        list.save(apple);
        list.save(sony);
        assertEquals(newHashSet(apple, sony), new HashSet<>((Collection<Seller>) service.list()));
    }

    @Test
    public void sellerShouldBeFoundIfExists() {
        list.save(apple);
        Optional<Seller> maybeSeller = service.find(apple.id());
        assertEquals(list.findById(apple.id()), maybeSeller);
    }

    @Test
    public void sellerShouldBeMissingIfNotExists() {
        Optional<Seller> maybeSeller = service.find(apple.id());
        assertFalse(maybeSeller.isPresent());
    }

    @AfterEach
    public void cleanUpEach() {
        list.deleteAll();
    }

}
