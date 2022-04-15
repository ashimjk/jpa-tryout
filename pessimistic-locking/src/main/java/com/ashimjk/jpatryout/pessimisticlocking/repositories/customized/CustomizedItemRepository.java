package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized;

import com.ashimjk.jpatryout.pessimisticlocking.domain.Item;

import java.util.UUID;

public interface CustomizedItemRepository {

    Item getItemAndObtainPessimisticWriteLockingOnItById(UUID id);

}