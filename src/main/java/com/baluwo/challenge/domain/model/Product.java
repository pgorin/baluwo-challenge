package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @JsonProperty
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product client = (Product) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", details=" + details +
                '}';
    }
}
