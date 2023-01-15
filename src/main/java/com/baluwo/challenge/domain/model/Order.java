package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @JsonProperty
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonProperty
    private Client client;
    @Column(nullable = false)
    @JsonProperty
    private OffsetDateTime dateTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty
    private Set<OrderOffer> offers;


    // required due reflection
    private Order() {
    }

    public Order(UUID id, Client client, OffsetDateTime dateTime) {
        this.id = id;
        this.client = client;
        this.dateTime = dateTime;
        this.offers = new HashSet<>();
    }

    public UUID id() {
        return id;
    }

    public Client client() {
        return client;
    }

    public OffsetDateTime dateTime() {
        return dateTime;
    }

    public Set<OrderOffer> offers() {
        return offers;
    }

    public Order withOffer(Offer offer, Integer quantity) {
        this.offers.add(new OrderOffer(this, offer, quantity));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order client = (Order) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", client=" + client +
                ", dateTime=" + dateTime +
                ", offers=" + offers +
                '}';
    }
}
