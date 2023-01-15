package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.ClientNotFound;
import com.baluwo.challenge.domain.model.OfferNotFound;
import com.baluwo.challenge.domain.model.Order;
import com.baluwo.challenge.domain.model.OrderRequest;
import com.baluwo.challenge.domain.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @Operation(summary = "Register a new order")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Order registered",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Order.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<?> register(@RequestBody OrderRequest request) throws JsonProcessingException {
        logger.info("Registering new order...");
        return service.register(request)
                .map(order -> status(CREATED).body((Object) order))
                .recover(ClientNotFound.class, ex -> badRequest().body(ex.getCause()))
                .recover(OfferNotFound.class, ex -> badRequest().body(ex.getCause()))
                .getOrElseGet(ex -> status(INTERNAL_SERVER_ERROR).body(ex.getCause()));
    }

    @Operation(summary = "List all orders")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of orders",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Iterable.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<?> list() throws JsonProcessingException {
        logger.info("Listing orders...");
        return ok(service.list());
    }

}