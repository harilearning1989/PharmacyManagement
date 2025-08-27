
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.InventoryBatchDto;
import com.web.pharma.inventory.mapper.InventoryBatchMapper;
import com.web.pharma.inventory.repository.InventoryBatchRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Batch-level inventory operations")
public class InventoryRestController {

    private static final Logger log = LoggerFactory.getLogger(InventoryRestController.class);
    private final InventoryBatchRepository batchRepository;
    private final InventoryBatchMapper batchMapper;

    @Operation(summary = "List all batches", description = "Returns all inventory batches",
            responses = {@ApiResponse(responseCode = "200", description = "Batches returned",
                    content = @Content(schema = @Schema(implementation = InventoryBatchDto.class)))})
    @GetMapping("/batches")
    public ResponseEntity<List<InventoryBatchDto>> listBatches() {
        log.info("GET /api/v1/inventory/batches - list all");
        return ResponseEntity.ok(batchRepository.findAll().stream().map(batchMapper::toDto).toList());
    }
}
