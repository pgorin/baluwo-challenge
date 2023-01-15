package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "offers")
public class Offer {

    @EmbeddedId
    private OfferId id;
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
        this.id = new OfferId(seller, product);
        this.seller = seller;
        this.product = product;
        this.price = price;
    }

    public OfferId id() {
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
