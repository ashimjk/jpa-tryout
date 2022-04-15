package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "derby")
public class DerbyCustomizedItemRepositoryContextImpl extends CustomizedItemRepositoryContext {

    private final PlatformTransactionManager transactionManager;

    public DerbyCustomizedItemRepositoryContextImpl(
            EntityManager em,
            ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig,
            PlatformTransactionManager transactionManager
    ) {
        super(em, concurrencyPessimisticLockingConfig);
        this.transactionManager = transactionManager;
    }

    @Override
    public void setLockTimeoutInSeparateTransaction(long timeoutDurationInMs) {
        log.info("... set lockTimeOut {} ms through native query at startup ...", timeoutDurationInMs);
        // TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        executeLockTimeoutQuery(timeoutDurationInMs);
        // transactionManager.commit(tx);
    }

    private void executeLockTimeoutQuery(long timeoutDurationInMs) {
        log.info("... set lockTimeOut {} ms through native query ...", timeoutDurationInMs);
        long timeoutDurationInSec = TimeUnit.MILLISECONDS.toSeconds(timeoutDurationInMs);
        em.createNativeQuery(String.format(
                  "CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.locks.waitTimeout',  '%d')",
                  timeoutDurationInSec))
          .executeUpdate();
    }

    @Override
    public long getLockTimeout() {
        Query query = em.createNativeQuery("VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY('derby.locks.waitTimeout')");
        long timeoutDurationInSec = Long.valueOf((String) query.getSingleResult());
        return TimeUnit.SECONDS.toMillis(timeoutDurationInSec);
    }

}