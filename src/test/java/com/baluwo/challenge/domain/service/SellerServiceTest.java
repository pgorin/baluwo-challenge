package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Seller;
import com.baluwo.challenge.domain.model.SellerInfo;
import com.baluwo.challenge.domain.persistence.impl.SellerList;
import com.baluwo.challenge.domain.service.impl.SellerServiceImpl;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashSet;

import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.baluwo.challenge.domain.model.Sellers.sony;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        Option<Seller> maybeUpdated = service.update(apple.id(), new SellerInfo(sony.name()));
        assertEquals(list.findById(apple.id()), maybeUpdated.toJavaOptional());
    }

    @Test
    public void sellerCannotBeUpdatedIfNotExists() {
        Option<Seller> maybeUpdated = service.update(apple.id(), new SellerInfo(sony.name()));
        assertTrue(maybeUpdated.isEmpty());
    }

    @Test
    public void sellerCanBeRemovedIfExists() {
        list.save(apple);
        Option<Seller> maybeRemoved = service.remove(apple.id());
        assertTrue(maybeRemoved.contains(apple));
        assertFalse(list.findById(apple.id()).isPresent());
    }

    @Test
    public void sellerCannotBeRemovedIfNotExists() {
        Option<Seller> maybeRemoved = service.remove(apple.id());
        assertTrue(maybeRemoved.isEmpty());
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
        Option<Seller> maybeSeller = service.find(apple.id());
        assertEquals(list.findById(apple.id()), maybeSeller.toJavaOptional());
    }

    @Test
    public void sellerShouldBeMissingIfNotExists() {
        Option<Seller> maybeSeller = service.find(apple.id());
        assertTrue(maybeSeller.isEmpty());
    }

    @AfterEach
    public void cleanUpEach() {
        list.deleteAll();
    }

}
