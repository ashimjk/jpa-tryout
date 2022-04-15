package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "derby")
public class DerbyCustomizedItemRepositoryContextImpl extends CustomizedItemRepositoryContext {

    public DerbyCustomizedItemRepositoryContextImpl(EntityManager em, ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig) {
        super(em, concurrencyPessimisticLockingConfig);
    }

    @Override
    public void setLockTimeout(long timeoutDurationInMs) {
        long timeoutDurationInSec = TimeUnit.MILLISECONDS.toSeconds(timeoutDurationInMs);
        Query query = em.createNativeQuery(String.format(
                "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.waitTimeout',  '%d')",
                timeoutDurationInSec));
        query.executeUpdate();
    }

    @Override
    public long getLockTimeout() {
        Query query = em.createNativeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('derby.locks.waitTimeout')");
        long timeoutDurationInSec = Long.valueOf((String) query.getSingleResult());
        return TimeUnit.SECONDS.toMillis(timeoutDurationInSec);
    }

}