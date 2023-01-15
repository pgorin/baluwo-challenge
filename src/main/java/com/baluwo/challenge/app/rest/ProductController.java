package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.*;
import com.baluwo.challenge.domain.service.ProductService;
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

import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Add a new product")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Product added",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Product.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<?> add(@RequestBody ProductDetails details) throws JsonProcessingException {
        logger.info("Adding new product...");
        return status(CREATED).body(service.add(details));
    }

    @Operation(summary = "Update product details by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product details updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Product.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id")
                                    @Parameter(description = "Id of the product to be updated") UUID id,
                                    @RequestBody ProductDetails details) {
        logger.info(format("Updating product with id %s...", id));
        return of(service.update(id, details).toJavaOptional());
    }

    @Operation(summary = "Remove product by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product removed",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Product.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> remove(@PathVariable("id")
                                    @Parameter(description = "Id of the product to be removed") UUID id) {
        logger.info(format("Removing product with id %s...", id));
        return of(service.remove(id).toJavaOptional());
    }

    @Operation(summary = "List all products")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of products",
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
        logger.info("Listing products...");
        return ok(service.list());
    }

    @Operation(summary = "Get a product by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Product.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id")
                                 @Parameter(description = "Id of the product to be get") UUID id) {
        logger.info(format("Looking for product with id %s...", id));
        return of(service.find(id).toJavaOptional());
    }

    @Operation(summary = "Add a new product offer")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Offer",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Offer.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Seller not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Product already offered",
                            content = @Content
                    )
            }
    )
    @PostMapping("{id}/offers")
    public ResponseEntity<?> offer(@PathVariable("id")
                                   @Parameter(description = "Id of the offered product") UUID product,
                                   @RequestParam("seller")
                                   @Parameter(description = "Id of the offered seller") UUID seller,
                                   @RequestBody Price price) {
        logger.info(format("Adding new offer for product %s and seller %s...", product, seller));
        return service.offer(product, seller, price)
                .map(offer -> status(CREATED).body((Object) offer))
                .recover(ProductNotFound.class, ex -> notFound().build())
                .recover(SellerNotFound.class, ex -> badRequest().body(ex.getCause()))
                .recover(ProductAlreadyOffered.class, ex -> status(CONFLICT).body(ex.getCause()))
                .getOrElseGet(ex -> status(INTERNAL_SERVER_ERROR).body(ex.getCause()));
    }

    @Operation(summary = "List the product offers")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product offers",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Iterable.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping("{id}/offers")
    public ResponseEntity<?> offers(@PathVariable("id")
                                    @Parameter(description = "Id of the offered product") UUID id) {
        logger.info(format("Listing offers for product with id %s...", id));
        return service.offers(id)
                .map(offers -> ok((Object) offers))
                .recover(ProductNotFound.class, notFound().build())
                .getOrElseGet(ex -> status(INTERNAL_SERVER_ERROR).body(ex.getCause()));
    }

}