package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.Seller;
import com.baluwo.challenge.domain.model.SellerInfo;
import com.baluwo.challenge.domain.service.SellerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/sellers")
public class SellerController {

    private final Logger logger = LoggerFactory.getLogger(SellerController.class);
    private final SellerService service;

    @Autowired
    public SellerController(SellerService service) {
        this.service = service;
    }

    @Operation(summary = "Add a new seller")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Seller added",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Seller.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<?> add(@RequestBody SellerInfo info) throws JsonProcessingException {
        logger.info("Registering new seller...");
        Seller added = service.add(info);
        return new ResponseEntity<>(added, CREATED);
    }

    @Operation(summary = "Update seller info by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Seller info updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Seller.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seller not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id")
                                    @Parameter(description = "Id of the seller to be updated") UUID id,
                                    @RequestBody SellerInfo info) {
        logger.info(format("Updating seller with id %s...", id));
        Optional<Seller> maybeUpdated = service.update(id, info);
        return ResponseEntity.of(maybeUpdated);
    }

    @Operation(summary = "Remove seller by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Seller removed",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Seller.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seller not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> remove(@PathVariable("id")
                                    @Parameter(description = "Id of the seller to be removed") UUID id) {
        logger.info(format("Removing seller with id %s...", id));
        Optional<Seller> maybeRemoved = service.remove(id);
        return ResponseEntity.of(maybeRemoved);
    }

    @Operation(summary = "List all sellers")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of sellers",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Iterable.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seller not found",
                            content = @Content
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<?> list() throws JsonProcessingException {
        logger.info("Listing sellers...");
        Iterable<Seller> sellers = service.list();
        return new ResponseEntity<>(sellers, OK);
    }

    @Operation(summary = "Get a seller by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Seller found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Seller.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seller not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id")
                                 @Parameter(description = "Id of the seller to be get") UUID id) {
        logger.info(format("Looking for seller with id %s...", id));
        Optional<Seller> maybeSeller = service.find(id);
        return ResponseEntity.of(maybeSeller);
    }

}