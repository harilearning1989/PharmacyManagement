
package com.web.pharma.inventory.controls;

import com.web.pharma.inventory.services.InventoryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Tag(name = "InventoryTransactions", description = "Audit trail of inventory changes")
@Slf4j
public class InventoryTransactionController {

    private final InventoryTransactionService inventoryTransactionService;

    @Operation(summary = "List transactions", description = "Returns inventory transactions",
            responses = {@ApiResponse(responseCode = "200", description = "Transactions returned")})
    @GetMapping
    public ResponseEntity<List<?>> listAll() {
        log.info("GET /api/v1/transactions - listAll");
        return ResponseEntity.ok(inventoryTransactionService.listAll());
    }
}
