package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized;

import com.ashimjk.jpatryout.pessimisticlocking.domain.Item;
import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context.CustomizedItemRepositoryContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CustomizedItemRepositoryImpl implements CustomizedItemRepository {

    private final EntityManager em;
    private final CustomizedItemRepositoryContext customizedItemRepositoryContext;

    @Override
    public Item getItemAndObtainPessimisticWriteLockingOnItById(UUID id) {
        log.info("Trying to obtain pessimistic lock ...");

        Query query = em.createQuery("select item from Item item where item.id = :id")
                        .setParameter("id", id)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE);

        query = customizedItemRepositoryContext.configureLockTimeout(query);

        Item item = (Item) query.getSingleResult();
        log.info("... pessimistic lock obtained ...");

        customizedItemRepositoryContext.insertArtificialDelayAtTheEndOfTheQueryForTestsOnly();
        log.info("... pessimistic lock released.");

        return item;
    }

}