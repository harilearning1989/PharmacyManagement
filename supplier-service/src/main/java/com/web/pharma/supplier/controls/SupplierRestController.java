package com.web.pharma.supplier.controls;

import com.web.pharma.supplier.models.SupplierDto;
import com.web.pharma.supplier.services.SupplierService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/supplier")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Operations to manage medicines")
@Slf4j
public class SupplierRestController {


    private final SupplierService supplierService;

    @Operation(
            summary = "Create supplier",
            description = "Add a new supplier to the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody SupplierDto dto) {
        log.info("Creating supplier with custId={}", dto.supplierId());
        try {
            return new ResponseEntity<>(supplierService.create(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create supplier", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create supplier: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Update supplier",
            description = "Update an existing supplier's details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Supplier not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable int id, @Valid @RequestBody SupplierDto dto) {
        log.info("Updating supplier id={}", id);
        try {
            return ResponseEntity.ok(supplierService.update(id, dto));
        } catch (IllegalArgumentException ex) {
            log.warn("Supplier not found for update id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception e) {
            log.error("Failed to update supplier id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update supplier: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Partially update a supplier",
            description = "Update one or more fields of an existing supplier without replacing the entire resource."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Supplier not found"))),
            @ApiResponse(responseCode = "400", description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Invalid field provided"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> patchSupplier(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        log.info("Partially updating supplier id={} with fields={}", id, updates.keySet());
        try {
            Optional<SupplierDto> updated = supplierService.patchSupplier(id, updates);
            if (updated.isPresent()) {
                log.info("Supplier updated successfully id={}", id);
                return ResponseEntity.ok(updated.get());
            } else {
                log.warn("Supplier not found with id={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found");
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid patch request for id={}", id, e);
            return ResponseEntity.badRequest().body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            log.error("Failed to patch supplier id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update supplier: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Get supplier by ID",
            description = "Retrieve a specific supplier by their unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Supplier not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable int id) {
        log.info("Fetching supplier by id={}", id);
        try {
            Optional<SupplierDto> supplierOpt = supplierService.getById(id);

            if (supplierOpt.isPresent()) {
                log.info("Supplier found with id={}", id);
                return ResponseEntity.ok(supplierOpt.get());
            } else {
                log.warn("Supplier not found with id={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Supplier not found");
            }

        } catch (Exception e) {
            log.error("Failed to fetch supplier id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch supplier: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Delete supplier",
            description = "Delete an existing supplier by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Supplier not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        log.info("Deleting supplier id={}", id);
        try {
            supplierService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Failed to delete supplier id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete supplier: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Search suppliers",
            description = "Search suppliers by name, phone, email, or custId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SupplierDto.class)))),
            @ApiResponse(responseCode = "404", description = "No supplier found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No supplier found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Searching suppliers q={}, page={}, size={}", q, page, size);
        try {
            Page<SupplierDto> result = supplierService.search(q, PageRequest.of(page, size));
            if (result.isEmpty()) {
                log.warn("No suppliers found for query={}", q);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No supplier found");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Failed to search suppliers q={}", q, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search suppliers: " + e.getMessage());
        }
    }

    @Operation(
            summary = "List all Suppliers",
            description = "Retrieve all suppliers available in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of suppliers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SupplierDto.class)))),
            @ApiResponse(responseCode = "404", description = "No supplier found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No supplier available in store"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/listAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listAll() {
        log.info("Fetching all suppliers");
        try {
            List<SupplierDto> suppliers = supplierService.getAllSuppliers();
            if (suppliers == null || suppliers.isEmpty()) {
                log.warn("No supplier found in store");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No supplier available in store");
            }
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            log.error("Failed to fetch suppliers", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch suppliers: " + e.getMessage());
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
            int size = supplierService.saveSupplier(file);
            log.info("bulk Create file {} size {}", file.getOriginalFilename(), size);
            return ResponseEntity.status(HttpStatus.CREATED).body("Inserted " + size + " suppliers successfully");
        } catch (Exception e) {
            log.error("Failed to upload supplier file {}", file.getOriginalFilename(), e);
            return ResponseEntity.badRequest().body("Failed to import supplier: " + e.getMessage());
        }
    }
}
