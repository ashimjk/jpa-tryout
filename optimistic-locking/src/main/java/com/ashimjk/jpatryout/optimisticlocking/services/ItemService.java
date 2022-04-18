package com.ashimjk.jpatryout.optimisticlocking.services;

import com.ashimjk.jpatryout.optimisticlocking.domain.Item;
import com.ashimjk.jpatryout.optimisticlocking.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAmount(String id, int amount) {
        Item item = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        item.setAmount(item.getAmount() + amount);
    }

}
