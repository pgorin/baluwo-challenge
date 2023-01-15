package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class Offer {

    @Embeddable
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return sellerId.equals(id.sellerId) && productId.equals(id.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sellerId, productId);
        }
    }


    @EmbeddedId
    private Id id;
    @ManyToOne
    @MapsId("seller_id")
    @JsonProperty
    private Seller seller;
    @ManyToOne
    @MapsId("product_id")
    @JsonProperty
    private Product product;
    @Embedded
    @JsonProperty
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

    public Seller seller() {
        return seller;
    }

    public Product product() {
        return product;
    }

    public Price price() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id.equals(offer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Offer{" +
                "seller=" + seller +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
