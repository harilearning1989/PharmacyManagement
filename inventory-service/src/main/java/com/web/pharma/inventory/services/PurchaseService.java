package com.web.pharma.inventory.services;

import com.web.pharma.inventory.dto.PurchaseDto;
import jakarta.validation.Valid;

public interface PurchaseService {
    Long recordPurchase(PurchaseDto dto);
}
