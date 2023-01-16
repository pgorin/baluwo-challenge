package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "order_offers")
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderOffer implements Serializable {

    @Embeddable
    @EqualsAndHashCode
    public static class Id implements Serializable {
        @Column(name = "order_id")
        private UUID orderId;
        @Column(name = "seller_id")
        private UUID sellerId;
        @Column(name = "product_id")
        private UUID productId;

        // required due reflection
        private Id() {
        }

        public Id(UUID orderId, UUID sellerId, UUID productId) {
            this.orderId = orderId;
            this.sellerId = sellerId;
            this.productId = productId;
        }

        public Id(Order order, Seller seller, Product product) {
            this(order.id(), seller.id(), product.id());
        }

    }

    @EmbeddedId
    @EqualsAndHashCode.Include
    private Id id;
    @ManyToOne
    @MapsId("order_id")
    private Order order;
    @ManyToOne
    @MapsId("seller_id")
    @ToString.Include
    private Seller seller;
    @ManyToOne
    @MapsId("product_id")
    @ToString.Include
    private Product product;
    @Embedded
    @ToString.Include
    private Price price;
    @Column(nullable = false)
    @ToString.Include
    private Integer quantity;

    private OrderOffer() {
    }

    public OrderOffer(Order order, Offer offer, Integer quantity) {
        this.id = new Id(order, offer.seller(), offer.product());
        this.order = order;
        this.seller = offer.seller();
        this.product = offer.product();
        this.price = offer.price();
        this.quantity = quantity;
    }

    public Order order() {
        return order;
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

    @JsonProperty
    public Integer quantity() {
        return quantity;
    }

}
