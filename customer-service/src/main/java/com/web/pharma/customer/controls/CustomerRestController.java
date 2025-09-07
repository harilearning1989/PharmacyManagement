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
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Operations to manage medicines")
@Slf4j
public class CustomerRestController {


    private final CustomerService customerService;

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
            summary = "Partially update a customer",
            description = "Update one or more fields of an existing customer without replacing the entire resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer not found"))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid field provided"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchCustomer(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        log.info("Partially updating customer id={} with fields={}", id, updates.keySet());
        try {
            Optional<CustomerDto> updated = customerService.patchCustomer(id, updates);
            if (updated.isPresent()) {
                log.info("Customer updated successfully id={}", id);
                return ResponseEntity.ok(updated.get());
            } else {
                log.warn("Customer not found with id={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid patch request for id={}", id, e);
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to patch customer id={}", id, e);
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

    @Operation(
            summary = "Search customer by phone number",
            description = "Retrieve a customer using their phone number (must be a valid Indian mobile number)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CustomerDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid phone number format",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Phone number must be a valid 10-digit Indian mobile number"))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Customer not found with phone: 9876543210"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/search/by-phone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByPhone(
            @RequestParam
            @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
            String phone
    ) {
        log.info("Searching customer by phone={}", phone);
        try {
            Optional<CustomerDto> customerOpt = customerService.findByPhone(phone);

            if (customerOpt.isPresent()) {
                log.info("Customer found with phone={}", phone);
                return ResponseEntity.ok(customerOpt.get());
            } else {
                log.warn("No customer found with phone={}", phone);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Customer not found with phone: " + phone);
            }

        } catch (Exception e) {
            log.error("Failed to fetch customer by phone={}", phone, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch customer: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Bulk import medicines",
            description = "Upload a JSON file containing a list of medicines . " +
                    "The API parses the file and saves all records into the database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicines imported successfully",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Inserted 1000 medicines successfully"))),
            @ApiResponse(responseCode = "400", description = "Invalid input or parsing error",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Failed to import medicines: invalid JSON structure"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Upload JSON file with medicines. " +
                    "The JSON must contain a root node `medical_store_inventory` holding an array of medicine records.",
            required = true,
            content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> bulkCreate(@RequestParam("file") MultipartFile file) {
        log.info("bulk Create file {}", file.getOriginalFilename());
        try {
            int size = customerService.saveCustomer(file);
            log.info("bulk Create file {} size {}", file.getOriginalFilename(), size);
            return ResponseEntity.status(HttpStatus.CREATED).body("Inserted " + size + " customers successfully");
        } catch (Exception e) {
            log.error("Failed to upload customer file {}", file.getOriginalFilename(), e);
            return ResponseEntity.badRequest().body("Failed to import customer: " + e.getMessage());
        }
    }
}
