package com.baluwo.challenge.app.rest;

import com.baluwo.challenge.domain.model.Client;
import com.baluwo.challenge.domain.model.ClientInfo;
import com.baluwo.challenge.domain.service.ClientService;
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

import javax.validation.Valid;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

    private final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @Operation(summary = "Add a new client")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client added",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Client.class)
                                    )
                            }
                    )
            }
    )
    @PostMapping()
    public ResponseEntity<?> add(@RequestBody @Valid ClientInfo info) throws JsonProcessingException {
        logger.info("Adding new client...");
        return status(CREATED).body(service.add(info));
    }

    @Operation(summary = "Update client info by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client info updated",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Client.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable("id")
                                    @Parameter(description = "Id of the client to be updated") UUID id,
                                    @RequestBody @Valid ClientInfo info) {
        logger.info(format("Updating client with id %s...", id));
        return of(service.update(id, info).toJavaOptional());
    }

    @Operation(summary = "Remove client by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client removed",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Client.class)
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> remove(@PathVariable("id")
                                    @Parameter(description = "Id of the client to be removed") UUID id) {
        logger.info(format("Removing client with id %s...", id));
        return of(service.remove(id).toJavaOptional());
    }

    @Operation(summary = "List all clients")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of clients",
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
        logger.info("Listing clients...");
        return ok(service.list());
    }

    @Operation(summary = "Get a client by its id")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client found",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = Client.class)
                                    )
                            }
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<?> get(@PathVariable("id")
                                 @Parameter(description = "Id of the client to be get") UUID id) {
        logger.info(format("Looking for client with id %s...", id));
        return of(service.find(id).toJavaOptional());
    }

}