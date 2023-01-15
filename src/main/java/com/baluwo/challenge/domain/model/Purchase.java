package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @JsonProperty
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(nullable = false)
    private OffsetDateTime dateTime;
    @OneToMany(mappedBy = "purchase")
    private Set<PurchaseOffer> offers;


    // required due reflection
    private Purchase() {
    }

    public Purchase(Client client, OffsetDateTime dateTime, Set<PurchaseOffer> offers) {
        this.client = client;
        this.dateTime = dateTime;
        this.offers = offers;
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

    public Set<PurchaseOffer> offers() {
        return offers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase client = (Purchase) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
