package com.ashimjk.jpatryout.pessimisticlocking.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private static final long PESSIMISTIC_LOCKING_EXCEPTION_HANDLING_RETRY_AFTER_MS = 200;

    private final ItemService itemService;

    @Transactional(readOnly = true)
    public void incrementAmount(UUID id, int amount) {
        try {
            itemService.incrementAmount(id, amount);
        } catch (PessimisticLockingFailureException e) {
            log.error("Found pessimistic lock exception!", e);
            sleepForAWhile();
            itemService.incrementAmount(id, amount);
        }
    }

    private void sleepForAWhile() {
        try {
            TimeUnit.MILLISECONDS.sleep(PESSIMISTIC_LOCKING_EXCEPTION_HANDLING_RETRY_AFTER_MS);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

}