
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.MedicineDto;
import com.web.pharma.inventory.services.MedicineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicines")
@RequiredArgsConstructor
@Tag(name = "Medicines", description = "Operations to manage medicines")
@Slf4j
public class MedicineRestController {

    private final MedicineService medicineService;

    @Operation(summary = "Create medicine", description = "Create a new medicine record.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Medicine created successfully",
                            content = @Content(schema = @Schema(implementation = MedicineDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    public ResponseEntity<MedicineDto> create(@Valid @RequestBody MedicineDto dto) {
        log.info("POST /api/v1/medicines - create {}", dto.itemId());
        var created = medicineService.create(dto);
        log.debug("Created medicine {}", created.itemId());
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "List medicines", description = "List all medicines.",
            responses = {@ApiResponse(responseCode = "200", description = "List returned")})
    @GetMapping
    public ResponseEntity<List<MedicineDto>> listAll() {
        log.info("GET /api/v1/medicines - listAll");
        return ResponseEntity.ok(medicineService.listAll());
    }

    @Operation(summary = "Get medicine by itemId", description = "Fetch a medicine by its item id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Medicine found",
                            content = @Content(schema = @Schema(implementation = MedicineDto.class))),
                    @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(hidden = true)))
            })
    @GetMapping("/{itemId}")
    public ResponseEntity<MedicineDto> getByItemId(@PathVariable String itemId) {
        log.info("GET /api/v1/medicines/{} - getByItemId", itemId);
        return ResponseEntity.ok(medicineService.findByItemId(itemId));
    }
}
