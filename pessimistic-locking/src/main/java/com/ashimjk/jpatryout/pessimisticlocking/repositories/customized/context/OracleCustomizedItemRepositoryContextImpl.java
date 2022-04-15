package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "oracle", matchIfMissing = true)
public class OracleCustomizedItemRepositoryContextImpl extends CustomizedItemRepositoryContext {

    public OracleCustomizedItemRepositoryContextImpl(
            EntityManager em,
            ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig
    ) {
        super(em, concurrencyPessimisticLockingConfig);
    }

    @Override
    public void setLockTimeout(long timeoutDurationInMs, Query query) {
        log.info("... set lockTimeOut {} ms through query hint ...", timeoutDurationInMs);
        query.setHint("javax.persistence.lock.timeout", String.valueOf(timeoutDurationInMs));
    }

}