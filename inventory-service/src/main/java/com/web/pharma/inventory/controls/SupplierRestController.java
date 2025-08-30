
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.SupplierDto;
import com.web.pharma.inventory.services.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Manage suppliers")
@Slf4j
public class SupplierRestController {

    private final SupplierService supplierService;

    // ================= CREATE =================
    @Operation(
            summary = "Create a new supplier",
            description = "Add a new supplier to the database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid supplier data",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Supplier name cannot be empty"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public ResponseEntity<?> createSupplier(@RequestBody SupplierDto supplier) {
        log.info("Creating supplier {}", supplier.name());
        try {
            SupplierDto created = supplierService.saveSupplier(supplier);
            return ResponseEntity.status(201).body(created);
        } catch (Exception e) {
            log.error("Error creating supplier", e);
            return ResponseEntity.badRequest().body("Failed to create supplier: " + e.getMessage());
        }
    }

    // ================= READ ALL =================
    @Operation(
            summary = "List all suppliers",
            description = "Retrieve all suppliers from the database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SupplierDto.class)))),
            @ApiResponse(responseCode = "404", description = "No suppliers found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "No suppliers available"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public ResponseEntity<?> getAllSuppliers() {
        log.info("Fetching all suppliers");
        try {
            List<SupplierDto> suppliers = supplierService.getAllSuppliers();
            if (suppliers.isEmpty()) {
                return ResponseEntity.status(404).body("No suppliers available");
            }
            return ResponseEntity.ok(suppliers);
        } catch (Exception e) {
            log.error("Error fetching suppliers", e);
            return ResponseEntity.status(500).body("Error fetching suppliers: " + e.getMessage());
        }
    }

    // ================= READ ONE =================
    @Operation(
            summary = "Get supplier by ID",
            description = "Retrieve a supplier's details using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Supplier with ID 10 not found"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        log.info("Fetching supplier with ID {}", id);
        try {
            Optional<SupplierDto> supplierOpt = supplierService.getSupplierById(id);
            if (supplierOpt.isPresent()) {
                return ResponseEntity.ok(supplierOpt.get());
            } else {
                return ResponseEntity.status(404).body("Supplier with ID " + id + " not found");
            }
        } catch (Exception e) {
            log.error("Error fetching supplier {}", id, e);
            return ResponseEntity.status(500).body("Error fetching supplier: " + e.getMessage());
        }
    }

    // ================= UPDATE =================
    @Operation(
            summary = "Update supplier",
            description = "Update an existing supplier's details by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(@PathVariable Long id, @RequestBody SupplierDto supplier) {
        log.info("Updating supplier {}", id);
        try {
            SupplierDto updated = supplierService.updateSupplier(id, supplier);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Supplier with ID " + id + " not found");
        } catch (Exception e) {
            log.error("Error updating supplier {}", id, e);
            return ResponseEntity.badRequest().body("Failed to update supplier: " + e.getMessage());
        }
    }

    // ================= PATCH (Partial Update Example) =================
    @Operation(
            summary = "Partially update supplier",
            description = "Update specific fields of a supplier without modifying all fields."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier partially updated successfully",
                    content = @Content(schema = @Schema(implementation = SupplierDto.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid patch request",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchSupplier(@PathVariable Long id, @RequestBody SupplierDto supplierDto) {
        log.info("Patching supplier {}", id);
        try {
            SupplierDto patched = supplierService.patchSupplier(id, supplierDto);
            return ResponseEntity.ok(patched);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Supplier with ID " + id + " not found");
        } catch (Exception e) {
            log.error("Error patching supplier {}", id, e);
            return ResponseEntity.badRequest().body("Failed to patch supplier: " + e.getMessage());
        }
    }

    // ================= DELETE =================
    @Operation(
            summary = "Delete supplier",
            description = "Delete a supplier from the database by ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id) {
        log.info("Deleting supplier {}", id);
        try {
            supplierService.deleteSupplier(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Supplier with ID " + id + " not found");
        } catch (Exception e) {
            log.error("Error deleting supplier {}", id, e);
            return ResponseEntity.status(500).body("Failed to delete supplier: " + e.getMessage());
        }
    }
}

