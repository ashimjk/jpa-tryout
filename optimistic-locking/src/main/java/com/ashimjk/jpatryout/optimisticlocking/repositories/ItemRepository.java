package com.ashimjk.jpatryout.optimisticlocking.repositories;

import com.ashimjk.jpatryout.optimisticlocking.domain.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {
}