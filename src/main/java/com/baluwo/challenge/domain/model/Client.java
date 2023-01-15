package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @JsonProperty
    private UUID id;
    @Embedded
    private ClientInfo info;

    // required due reflection
    private Client() {
    }

    public Client(UUID id, ClientInfo info) {
        this.id = id;
        this.info = info;
    }

    public UUID id() {
        return id;
    }

    @JsonProperty
    public String name() {
        return info.name();
    }

    public Client withInfo(ClientInfo info) {
        this.info = info;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
