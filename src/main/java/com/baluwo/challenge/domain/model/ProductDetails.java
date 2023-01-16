package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
@ToString
@EqualsAndHashCode
public class ProductDetails {
    @Column(nullable = false)
    @NotNull(message = "INVALID_NAME")
    @Size(min = 3, max = 255, message = "INVALID_NAME")
    private String name;

    // required due reflection
    private ProductDetails() {
    }

    public ProductDetails(String name) {
        this.name = name;
    }

    @JsonProperty
    public String name() {
        return name;
    }


}
