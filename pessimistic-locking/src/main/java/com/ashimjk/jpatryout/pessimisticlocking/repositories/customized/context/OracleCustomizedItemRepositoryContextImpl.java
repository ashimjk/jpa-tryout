package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "oracle", matchIfMissing = true)
public class OracleCustomizedItemRepositoryContextImpl extends CustomizedItemRepositoryContext {

    public OracleCustomizedItemRepositoryContextImpl(
            EntityManager em,
            ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig
    ) {
        super(em, concurrencyPessimisticLockingConfig);
    }

}