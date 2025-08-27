package com.web.pharma.inventory.services;

import com.web.pharma.inventory.model.InventoryTransaction;
import com.web.pharma.inventory.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryTransactionServiceImpl implements InventoryTransactionService {
    private final InventoryTransactionRepository txnRepository;

    @Transactional(readOnly = true)
    public List<InventoryTransaction> listAll() {
        log.debug("Listing inventory transactions");
        return txnRepository.findAll();
    }
}
