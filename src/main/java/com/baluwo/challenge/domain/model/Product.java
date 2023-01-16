package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @Embedded
    private ProductDetails details;

    // required due reflection
    private Product() {
    }

    public Product(UUID id, ProductDetails details) {
        this.id = id;
        this.details = details;
    }

    @JsonProperty
    public UUID id() {
        return id;
    }

    @JsonProperty
    public String name() {
        return details.name();
    }

    public Product withDetails(ProductDetails details) {
        this.details = details;
        return this;
    }

}
