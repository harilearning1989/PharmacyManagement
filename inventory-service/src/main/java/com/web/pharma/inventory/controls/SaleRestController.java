
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.SaleDto;
import com.web.pharma.inventory.services.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@Tag(name = "Sales", description = "Create sales and deduct inventory")
@Slf4j
public class SaleRestController {

    private final SaleService saleService;

    @Operation(summary = "Create sale", description = "Create a sale and allocate batches (FIFO by expiry)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sale created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody SaleDto dto) {
        log.info("POST /api/v1/sales - create sale with {} items", dto.items().size());
        Long id = saleService.createSale(dto);
        return ResponseEntity.status(201).body(Map.of("saleId", id));
    }
}
