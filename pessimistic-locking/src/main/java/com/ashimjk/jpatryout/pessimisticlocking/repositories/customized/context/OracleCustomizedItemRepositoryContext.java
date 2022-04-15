package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.config.PessimisticLockingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.Query;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "oracle", matchIfMissing = true)
public class OracleCustomizedItemRepositoryContext extends CustomizedItemRepositoryContext {

    public OracleCustomizedItemRepositoryContext(
            PessimisticLockingConfig pessimisticLockingConfig
    ) {
        super(pessimisticLockingConfig);
    }

    @Override
    public Query configureLockTimeout(Query query) {
        long timeoutDurationInMs = pessimisticLockingConfig.getLockTimeOutInMs();
        log.info("... set lockTimeOut {} ms through query hint ...", timeoutDurationInMs);
        query.setHint("javax.persistence.lock.timeout", String.valueOf(timeoutDurationInMs));
        return query;
    }

}