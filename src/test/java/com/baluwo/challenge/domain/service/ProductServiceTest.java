package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Product;
import com.baluwo.challenge.domain.model.ProductDetails;
import com.baluwo.challenge.domain.persistence.impl.ProductInventory;
import com.baluwo.challenge.domain.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static com.baluwo.challenge.domain.model.Products.iphone11;
import static com.baluwo.challenge.domain.model.Products.playstation5;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest()
public class ProductServiceTest {

    private final ProductInventory inventory;
    private final ProductService service;

    @Autowired
    public ProductServiceTest(ProductInventory inventory) {
        this.inventory = inventory;
        this.service = new ProductServiceImpl(inventory);
    }


    @Test
    public void productShouldBeAdded() {
        Product added = service.add(new ProductDetails(playstation5.name()));
        assertThat(inventory.findById(added.id())).hasValue(added);
    }

    @Test
    public void productCanBeUpdatedIfExists() {
        inventory.save(playstation5);
        Optional<Product> maybeUpdated = service.update(playstation5.id(), new ProductDetails(iphone11.name()));
        assertEquals(inventory.findById(playstation5.id()), maybeUpdated);
    }

    @Test
    public void productCannotBeUpdatedIfNotExists() {
        Optional<Product> maybeUpdated = service.update(playstation5.id(), new ProductDetails(iphone11.name()));
        assertFalse(maybeUpdated.isPresent());
    }

    @Test
    public void productCanBeRemovedIfExists() {
        inventory.save(playstation5);
        Optional<Product> maybeRemoved = service.remove(playstation5.id());
        assertThat(maybeRemoved).hasValue(playstation5);
        assertFalse(inventory.findById(playstation5.id()).isPresent());
    }

    @Test
    public void productCannotBeRemovedIfNotExists() {
        Optional<Product> maybeRemoved = service.remove(playstation5.id());
        assertFalse(maybeRemoved.isPresent());
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
        Optional<Product> maybeProduct = service.find(playstation5.id());
        assertEquals(inventory.findById(playstation5.id()), maybeProduct);
    }

    @Test
    public void productShouldBeMissingIfNotExists() {
        Optional<Product> maybeProduct = service.find(playstation5.id());
        assertFalse(maybeProduct.isPresent());
    }

    @AfterEach
    public void cleanUpEach() {
        inventory.deleteAll();
    }

}
