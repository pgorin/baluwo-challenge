package com.baluwo.challenge.domain.service;

import com.baluwo.challenge.domain.model.Order;
import com.baluwo.challenge.domain.model.OrderRequest;
import io.vavr.Tuple3;
import io.vavr.control.Try;

import java.util.Set;
import java.util.UUID;

public interface OrderService {

    Try<Order> register(OrderRequest request);

    Iterable<Order> list();

}
