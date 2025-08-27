
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.dto.PurchaseDto;
import com.web.pharma.inventory.services.PurchaseService;
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
@RequestMapping("/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases", description = "Record purchases and create inventory batches")
@Slf4j
public class PurchaseRestController {

    private final PurchaseService purchaseService;

    @Operation(summary = "Record purchase", description = "Record a purchase invoice and create batches",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Purchase recorded"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(hidden = true)))
            })
    @PostMapping
    public ResponseEntity<Map<String, Object>> record(@Valid @RequestBody PurchaseDto dto) {
        log.info("POST /purchases - record invoice {}", dto.invoiceNo());
        Long id = purchaseService.recordPurchase(dto);
        return ResponseEntity.status(201).body(Map.of("purchaseId", id));
    }
}
