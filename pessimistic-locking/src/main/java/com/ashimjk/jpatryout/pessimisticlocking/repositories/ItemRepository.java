package com.ashimjk.jpatryout.pessimisticlocking.repositories;

import com.ashimjk.jpatryout.pessimisticlocking.domain.Item;
import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.CustomizedItemRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ItemRepository extends CrudRepository<Item, UUID>, CustomizedItemRepository {}