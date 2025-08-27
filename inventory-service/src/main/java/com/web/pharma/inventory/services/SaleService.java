package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.SaleDto;
import jakarta.validation.Valid;

public interface SaleService {
    Long createSale(@Valid SaleDto dto);
}
