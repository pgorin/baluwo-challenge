package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "purchase_offers")
public class PurchaseOffer implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;
    @ManyToOne
    @MapsId("seller_id")
    @JsonProperty
    private Seller seller;
    @ManyToOne
    @MapsId("product_id")
    @JsonProperty
    private Product product;
    @Embedded
    private Price price;
    @Column(nullable = false)
    private Integer quantity;

    private PurchaseOffer() {
    }

    public PurchaseOffer(Purchase purchase, Seller seller, Product product, Price price, Integer quantity) {
        this.purchase = purchase;
        this.seller = seller;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public Purchase purchase() {
        return purchase;
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

    public Integer quantity() {
        return quantity;
    }

}
