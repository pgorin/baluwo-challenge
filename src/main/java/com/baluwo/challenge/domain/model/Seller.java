package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @JsonProperty
    private UUID id;
    @Embedded
    private SellerInfo info;

    // required due reflection
    private Seller() {
    }

    public Seller(UUID id, SellerInfo info) {
        this.id = id;
        this.info = info;
    }

    public UUID id() {
        return id;
    }

    @JsonProperty
    public String name() {
        return info.name();
    }

    public Seller withInfo(SellerInfo info) {
        this.info = info;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller client = (Seller) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
