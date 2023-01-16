package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Seller {

    @Id
    @EqualsAndHashCode.Include
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

    @JsonProperty
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

}
