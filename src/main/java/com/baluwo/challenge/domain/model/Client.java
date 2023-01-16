package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "clients")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @EqualsAndHashCode.Include
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

    @JsonProperty
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

}
