package com.ashimjk.jpatryout.pessimisticlocking.services;

import com.ashimjk.jpatryout.pessimisticlocking.domain.Item;
import com.ashimjk.jpatryout.pessimisticlocking.repositories.ItemRepository;
import com.ashimjk.jpatryout.pessimisticlocking.config.PessimisticLockingConfig;
import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context.CustomizedItemRepositoryContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
abstract class AbstractInventoryServiceTest {

    @Autowired private ItemRepository itemRepository;
    @Autowired private InventoryService inventoryService;

    @SpyBean private ItemService itemService;
    @SpyBean private CustomizedItemRepositoryContext customizedItemRepositoryContext;
    @SpyBean private PessimisticLockingConfig pessimisticLockingConfig;

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    void shouldIncrementItemAmount_withoutConcurrency() {
        // Given
        final Item srcItem = itemRepository.save(new Item());

        // When
        final List<Integer> itemAmounts = Arrays.asList(10, 5);
        for (final int amount : itemAmounts) {
            inventoryService.incrementAmount(srcItem.getId(), amount);
        }

        // Then
        final Item item = itemRepository
                .findById(srcItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        assertAll(() -> assertEquals(15, item.getAmount()),
                  () -> verify(itemService, times(2)).incrementAmount(any(UUID.class), anyInt()));
    }

    @Test
    void shouldIncrementItemAmount_withinPessimisticLockingConcurrencyWithMinimalLockTimeout() throws Exception {
        long minimalPossibleLockTimeOutInMs = pessimisticLockingConfig.getMinimalPossibleLockTimeOutInMs();
        when(pessimisticLockingConfig.getLockTimeOutInMs())
                .thenReturn(minimalPossibleLockTimeOutInMs);

        assertIncrementItemAmountWithPessimisticLocking(3);
    }

    @Test
    void shouldIncrementItemAmount_withinPessimisticLockingConcurrencyWithDefaultLockTimeout() throws Exception {
        assertIncrementItemAmountWithPessimisticLocking(2);
    }

    private void assertIncrementItemAmountWithPessimisticLocking(int expectedNumberOfItemServiceInvocations) throws Exception {
        // Given
        insertDelayAtTheEndOfPessimisticLockingSection();
        final Item srcItem = itemRepository.save(new Item());

        // When
        final List<Integer> itemAmounts = Arrays.asList(10, 5);
        final ExecutorService executor = Executors.newFixedThreadPool(itemAmounts.size());
        for (final int amount : itemAmounts) {
            executor.execute(() -> inventoryService.incrementAmount(srcItem.getId(), amount));
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));

        // Then
        final Item item = itemRepository
                .findById(srcItem.getId())
                .orElseThrow(() -> new IllegalArgumentException("Item not found!"));

        assertAll(() -> assertEquals(15, item.getAmount()),
                  () -> verify(itemService, times(expectedNumberOfItemServiceInvocations))
                          .incrementAmount(any(UUID.class), anyInt()));
    }

    private void insertDelayAtTheEndOfPessimisticLockingSection() {
        long delay = pessimisticLockingConfig.getDelayAtTheEndOfTheQueryForPessimisticLockingTestingInMs();
        doAnswer(invocation -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return null;
        }).when(customizedItemRepositoryContext).insertArtificialDelayAtTheEndOfTheQueryForTestsOnly();
    }

}