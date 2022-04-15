package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.config.PessimisticLockingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "derby")
public class DerbyCustomizedItemRepositoryContext extends CustomizedItemRepositoryContext {

    protected final EntityManager entityManager;

    public DerbyCustomizedItemRepositoryContext(
            PessimisticLockingConfig pessimisticLockingConfig,
            EntityManager entityManager
    ) {
        super(pessimisticLockingConfig);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Query configureLockTimeout(Query query) {
        setLockTimeout(pessimisticLockingConfig.getLockTimeOutInMs());
        return query;
    }

    @Override
    public void setLockTimeout(long timeoutDurationInMs) {
        log.info("... set lockTimeOut {} ms through native query ...", timeoutDurationInMs);
        long timeoutDurationInSec = TimeUnit.MILLISECONDS.toSeconds(timeoutDurationInMs);
        entityManager.createNativeQuery(String.format(
                             "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.waitTimeout',  '%d')",
                             timeoutDurationInSec))
                     .executeUpdate();
    }

    @Override
    public long getLockTimeout() {
        Query query = entityManager.createNativeQuery(
                "VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('derby.locks.waitTimeout')");
        long timeoutDurationInSec = Long.parseLong((String) query.getSingleResult());
        return TimeUnit.SECONDS.toMillis(timeoutDurationInSec);
    }

}