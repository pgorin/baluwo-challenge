package com.baluwo.challenge.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class OfferId implements Serializable {

    @Column(name = "seller_id")
    private UUID sellerId;
    @Column(name = "product_id")
    private UUID productId;

    // required due reflection
    private OfferId() {
    }

    public OfferId(UUID sellerId, UUID productId) {
        this.sellerId = sellerId;
        this.productId = productId;
    }

    public OfferId(Seller seller, Product product) {
        this(seller.id(), product.id());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferId offerId = (OfferId) o;
        return sellerId.equals(offerId.sellerId) && productId.equals(offerId.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerId, productId);
    }
}
