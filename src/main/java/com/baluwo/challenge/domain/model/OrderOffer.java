package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_offers")
public class OrderOffer implements Serializable {

    @Embeddable
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id that = (Id) o;
            return orderId.equals(that.orderId) && sellerId.equals(that.sellerId) && productId.equals(that.productId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, sellerId, productId);
        }

    }

    @EmbeddedId
    private Id id;
    @ManyToOne
    @MapsId("order_id")
    private Order order;
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
    @Column(nullable = false)
    @JsonProperty
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

    @Override
    public String toString() {
        return "OrderOffer{" +
                "id=" + id +
                ", seller=" + seller +
                ", product=" + product +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
