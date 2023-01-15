package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.Product;
import com.baluwo.challenge.domain.model.ProductDetails;
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

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
        logger.info("Registering new product...");
        Product added = service.add(details);
        return new ResponseEntity<>(added, CREATED);
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
        Optional<Product> maybeUpdated = service.update(id, details);
        return ResponseEntity.of(maybeUpdated);
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
        Optional<Product> maybeRemoved = service.remove(id);
        return ResponseEntity.of(maybeRemoved);
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
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found",
                            content = @Content
                    )
            }
    )
    @GetMapping()
    public ResponseEntity<?> list() throws JsonProcessingException {
        logger.info("Listing products...");
        Iterable<Product> products = service.list();
        return new ResponseEntity<>(products, OK);
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
        Optional<Product> maybeProduct = service.find(id);
        return ResponseEntity.of(maybeProduct);
    }

}