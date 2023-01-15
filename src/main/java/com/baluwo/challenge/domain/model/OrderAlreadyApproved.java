package com.baluwo.challenge.domain.model;

import static java.lang.String.format;

public class OrderAlreadyApproved extends RuntimeException {

    public OrderAlreadyApproved(OrderApproval approval) {
        super(format("Order already approved by %s on %s", approval.approver(), approval.dateTime()));
    }

}
