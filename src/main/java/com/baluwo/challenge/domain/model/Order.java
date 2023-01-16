package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

@Entity
@Table(name = "orders")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(name = "date_time", nullable = false)
    private OffsetDateTime dateTime;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<OrderOffer> offers;
    @Embedded
    @JsonProperty
    // TODO using nullable instead of Option due unresolved hibernate mapping
    private OrderApproval approval;

    // required due reflection
    private Order() {
    }

    public Order(UUID id, Client client, OffsetDateTime dateTime) {
        this.id = id;
        this.client = client;
        this.dateTime = dateTime;
        this.offers = new HashSet<>();
        this.approval = null;
    }

    @JsonProperty
    public UUID id() {
        return id;
    }

    @JsonProperty
    public Client client() {
        return client;
    }

    @JsonProperty
    public OffsetDateTime dateTime() {
        return dateTime;
    }

    @JsonProperty
    public Set<OrderOffer> offers() {
        return offers;
    }

    public Option<OrderApproval> approval() {
        return Option.of(approval);
    }

    public Order withOffer(Offer offer, Integer quantity) {
        this.offers.add(new OrderOffer(this, offer, quantity));
        return this;
    }

    public Try<Order> approve(OrderApproval approval) {
        return this.approval().fold(
                () -> {
                    this.approval = approval;
                    return success(this);
                },
                current -> failure(new OrderAlreadyApproved(current))
        );
    }

}
