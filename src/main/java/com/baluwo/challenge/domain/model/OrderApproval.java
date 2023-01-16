package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.OffsetDateTime;

@Embeddable
@ToString
@EqualsAndHashCode
public class OrderApproval {
    @Column()
    private String approver;
    @Column(name = "approval_date_time")
    private OffsetDateTime dateTime;

    // required due reflection
    private OrderApproval() {
    }

    public OrderApproval(String approver, OffsetDateTime dateTime) {
        this.approver = approver;
        this.dateTime = dateTime;
    }

    @JsonProperty
    public String approver() {
        return approver;
    }

    @JsonProperty
    public OffsetDateTime dateTime() {
        return dateTime;
    }

}
