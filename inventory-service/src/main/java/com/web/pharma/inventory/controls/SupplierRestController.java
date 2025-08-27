
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.SupplierDto;
import com.web.pharma.inventory.services.SupplierService;
import com.web.pharma.inventory.services.SupplierServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Manage suppliers")
@Slf4j
public class SupplierRestController {

    private final SupplierService supplierService;

    @Operation(summary = "Create supplier", description = "Create a supplier record",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Supplier created",
                            content = @Content(schema = @Schema(implementation = SupplierDto.class)))
            })
    @PostMapping
    public ResponseEntity<SupplierDto> create(@Valid @RequestBody SupplierDto dto) {
        log.info("POST /api/v1/suppliers - create {}", dto.name());
        return ResponseEntity.status(201).body(supplierService.create(dto));
    }

    @Operation(summary = "List suppliers", description = "List all suppliers",
            responses = {@ApiResponse(responseCode = "200", description = "Suppliers returned")})
    @GetMapping
    public ResponseEntity<List<SupplierDto>> listAll() {
        log.info("GET /api/v1/suppliers - listAll");
        return ResponseEntity.ok(supplierService.listAll());
    }

    @Operation(summary = "Get supplier by id", description = "Fetch supplier by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Supplier found",
                            content = @Content(schema = @Schema(implementation = SupplierDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getById(@PathVariable Long id) {
        log.info("GET /api/v1/suppliers/{} - getById", id);
        return ResponseEntity.ok(supplierService.findById(id));
    }

    @Operation(summary = "Update supplier", description = "Update supplier by id",
            responses = {@ApiResponse(responseCode = "200", description = "Updated")})
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDto> update(@PathVariable Long id, @Valid @RequestBody SupplierDto dto) {
        log.info("PUT /api/v1/suppliers/{} - update", id);
        return ResponseEntity.ok(supplierService.update(id, dto));
    }

    @Operation(summary = "Delete supplier", description = "Deletes a supplier",
            responses = {@ApiResponse(responseCode = "204", description = "Deleted")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/suppliers/{} - delete", id);
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
