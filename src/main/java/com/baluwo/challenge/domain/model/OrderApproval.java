package com.baluwo.challenge.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Embeddable
public class OrderApproval {
    @Column()
    @JsonProperty
    private String approver;
    @Column(name = "approval_date_time")
    @JsonProperty
    private OffsetDateTime dateTime;

    // required due reflection
    private OrderApproval() {
    }

    public OrderApproval(String approver, OffsetDateTime dateTime) {
        this.approver = approver;
        this.dateTime = dateTime;
    }

    public String approver() {
        return approver;
    }

    public OffsetDateTime dateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderApproval that = (OrderApproval) o;
        return Objects.equals(approver, that.approver) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(approver, dateTime);
    }

    @Override
    public String toString() {
        return "OrderApproval{" +
                "approver='" + approver + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
