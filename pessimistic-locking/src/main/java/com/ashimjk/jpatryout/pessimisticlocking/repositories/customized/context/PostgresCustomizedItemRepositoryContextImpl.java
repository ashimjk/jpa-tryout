package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "spring.jpa.database", havingValue = "postgresql")
public class PostgresCustomizedItemRepositoryContextImpl extends CustomizedItemRepositoryContext {

    public PostgresCustomizedItemRepositoryContextImpl(
            EntityManager em,
            ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig
    ) {
        super(em, concurrencyPessimisticLockingConfig);
    }

    @Override
    public void setLockTimeout(long timeoutDurationInMs) {
        Query query = em.createNativeQuery("set local lock_timeout = " + timeoutDurationInMs);
        query.executeUpdate();
    }

    @Override
    public long getLockTimeout() {
        Query query = em.createNativeQuery("show lock_timeout");
        String result = (String) query.getSingleResult();
        return parseLockTimeOutToMilliseconds(result);
    }

    private static final String[] TIME_MEASURES = {"ms", "s", "min", "h", "d"};
    private static final TimeUnit[] TIME_UNITS = {TimeUnit.MILLISECONDS, TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS};

    private long parseLockTimeOutToMilliseconds(String lockTimeOut) {
        for (int idx = 0; idx < TIME_MEASURES.length; idx++) {
            if (lockTimeOut.contains(TIME_MEASURES[idx])) {
                long time = Long.parseLong(
                        lockTimeOut.substring(0, lockTimeOut.length() - TIME_MEASURES[idx].length())
                );
                return time * TIME_UNITS[idx].toMillis(1);
            }
        }

        return Long.parseLong(lockTimeOut);
    }

}