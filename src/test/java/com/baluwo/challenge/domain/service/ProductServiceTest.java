package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Offer;
import com.baluwo.challenge.domain.model.Price;
import com.baluwo.challenge.domain.model.Product;
import com.baluwo.challenge.domain.model.ProductDetails;
import com.baluwo.challenge.domain.persistence.impl.OfferList;
import com.baluwo.challenge.domain.persistence.impl.ProductInventory;
import com.baluwo.challenge.domain.persistence.impl.SellerList;
import com.baluwo.challenge.domain.service.impl.ProductServiceImpl;
import io.vavr.control.Option;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import static com.baluwo.challenge.domain.model.Products.iphone11;
import static com.baluwo.challenge.domain.model.Products.playstation5;
import static com.baluwo.challenge.domain.model.Sellers.apple;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
public class ProductServiceTest {

    private final ProductInventory inventory;
    private final OfferList offers;
    private final SellerList sellers;
    private final ProductService service;

    @Autowired
    public ProductServiceTest(ProductInventory inventory, OfferList offers, SellerList sellers) {
        this.inventory = inventory;
        this.offers = offers;
        this.sellers = sellers;
        this.service = new ProductServiceImpl(inventory, sellers, offers);
    }


    @Test
    public void productShouldBeAdded() {
        Product added = service.add(new ProductDetails(playstation5.name()));
        assertThat(inventory.findById(added.id())).hasValue(added);
    }

    @Test
    public void productCanBeUpdatedIfExists() {
        inventory.save(playstation5);
        Option<Product> maybeUpdated = service.update(playstation5.id(), new ProductDetails(iphone11.name()));
        assertEquals(inventory.findById(playstation5.id()), maybeUpdated.toJavaOptional());
    }

    @Test
    public void productCannotBeUpdatedIfNotExists() {
        Option<Product> maybeUpdated = service.update(playstation5.id(), new ProductDetails(iphone11.name()));
        assertTrue(maybeUpdated.isEmpty());
    }

    @Test
    public void productCanBeRemovedIfExists() {
        inventory.save(playstation5);
        Option<Product> maybeRemoved = service.remove(playstation5.id());
        assertTrue(maybeRemoved.contains(playstation5));
        assertFalse(inventory.findById(playstation5.id()).isPresent());
    }

    @Test
    public void productCannotBeRemovedIfNotExists() {
        Option<Product> maybeRemoved = service.remove(playstation5.id());
        assertTrue(maybeRemoved.isEmpty());
    }

    @Test
    public void productsCanBeListed() {
        inventory.save(playstation5);
        inventory.save(iphone11);
        assertEquals(newHashSet(playstation5, iphone11), new HashSet<>((Collection<Product>) service.list()));
    }

    @Test
    public void productShouldBeFoundIfExists() {
        inventory.save(playstation5);
        Option<Product> maybeProduct = service.find(playstation5.id());
        assertEquals(inventory.findById(playstation5.id()), maybeProduct.toJavaOptional());
    }

    @Test
    public void productShouldBeMissingIfNotExists() {
        Option<Product> maybeProduct = service.find(playstation5.id());
        assertTrue(maybeProduct.isEmpty());
    }

    @Test
    public void productOfferShouldBeAddedIfNotAlreadyExists() {
        inventory.save(iphone11);
        sellers.save(apple);
        Offer added = service.offer(iphone11.id(), apple.id(), new Price(100)).get();
        assertThat(offers.findById(added.id())).hasValue(added);
    }

    @Test
    public void productOffersCanBeListed() {
        inventory.save(iphone11);
        sellers.save(apple);
        Offer offer = offers.save(new Offer(apple, iphone11, new Price(100)));
        assertTrue(service.offers(iphone11.id()).contains(newHashSet(offer)));
    }

    @AfterEach
    public void cleanUpEach() {
        offers.deleteAll();
        sellers.deleteAll();
        inventory.deleteAll();
    }

}
