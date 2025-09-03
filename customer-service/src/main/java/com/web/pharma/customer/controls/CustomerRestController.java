package com.web.pharma.customer.controls;

import com.web.pharma.customer.models.CustomerDto;
import com.web.pharma.customer.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Operations to manage medicines")
@Slf4j
public class CustomerRestController {


    private CustomerService customerService;

    @Operation(
            summary = "Create customer",
            description = "Add a new customer to the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody CustomerDto dto) {
        log.info("Creating customer with custId={}", dto.custId());
        try {
            return new ResponseEntity<>(customerService.create(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create customer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create customer: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Update customer",
            description = "Update an existing customer's details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody CustomerDto dto) {
        log.info("Updating customer id={}", id);
        try {
            return ResponseEntity.ok(customerService.update(id, dto));
        } catch (IllegalArgumentException ex) {
            log.warn("Customer not found for update id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Failed to update customer id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update customer: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Get customer by ID",
            description = "Retrieve a specific customer by their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable int id) {
        log.info("Fetching customer by id={}", id);
        try {
            Optional<CustomerDto> customerOpt = customerService.getById(id);

            if (customerOpt.isPresent()) {
                log.info("Customer found with id={}", id);
                return ResponseEntity.ok(customerOpt.get());
            } else {
                log.warn("Customer not found with id={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }

        } catch (Exception e) {
            log.error("Failed to fetch customer id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch customer: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Delete customer",
            description = "Delete an existing customer by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        log.info("Deleting customer id={}", id);
        try {
            customerService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete customer id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete customer: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Search customers",
            description = "Search customers by name, phone, email, or custId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class)))),
            @ApiResponse(responseCode = "404", description = "No customer found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No customer found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching customers q={}, page={}, size={}", q, page, size);
        try {
            Page<CustomerDto> result = customerService.search(q, PageRequest.of(page, size));
            if (result.isEmpty()) {
                log.warn("No customers found for query={}", q);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customer found");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to search customers q={}", q, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search customers: " + e.getMessage());
        }
    }

    @Operation(
            summary = "List all Customers",
            description = "Retrieve all customers available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CustomerDto.class)))),
            @ApiResponse(responseCode = "404", description = "No customer found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No customer available in store"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAll() {
        log.info("Fetching all customers");
        try {
            List<CustomerDto> customers = customerService.getAllCustomers();
            if (customers == null || customers.isEmpty()) {
                log.warn("No customer found in store");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No customer available in store");
            }
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            log.error("Failed to fetch customers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch customers: " + e.getMessage());
        }
    }
}
