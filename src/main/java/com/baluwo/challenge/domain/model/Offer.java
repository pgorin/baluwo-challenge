package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "offers")
@ToString
@EqualsAndHashCode
public class Offer {

    @Embeddable
    @ToString
    @EqualsAndHashCode
    public static class Id implements Serializable {

        @Column(name = "seller_id")
        private UUID sellerId;
        @Column(name = "product_id")
        private UUID productId;

        // required due reflection
        private Id() {
        }

        public Id(UUID sellerId, UUID productId) {
            this.sellerId = sellerId;
            this.productId = productId;
        }

        public Id(Seller seller, Product product) {
            this(seller.id(), product.id());
        }

    }


    @EmbeddedId
    private Id id;
    @ManyToOne
    @MapsId("seller_id")
    private Seller seller;
    @ManyToOne
    @MapsId("product_id")
    private Product product;
    @Embedded
    private Price price;

    // required due reflection
    private Offer() {
    }

    public Offer(Seller seller, Product product, Price price) {
        this.id = new Id(seller, product);
        this.seller = seller;
        this.product = product;
        this.price = price;
    }

    public Id id() {
        return id;
    }

    @JsonProperty
    public Seller seller() {
        return seller;
    }

    @JsonProperty
    public Product product() {
        return product;
    }

    @JsonProperty
    public Price price() {
        return price;
    }

}
