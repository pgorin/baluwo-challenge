package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductDetails {
    @Column(nullable = false)
    @JsonProperty
    private String name;

    // required due reflection
    private ProductDetails() {
    }

    public ProductDetails(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetails that = (ProductDetails) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
