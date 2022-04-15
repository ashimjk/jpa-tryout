package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Slf4j
@RequiredArgsConstructor
public abstract class CustomizedItemRepositoryContext {

    private static final String UNSUPPORTED_METHOD_ERROR_MESSAGE = "Method invocation not allowed!";

    protected final EntityManager em;
    protected final ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig;

    public void setLockTimeout(long timeoutDurationInMs) {
    }

    public void setLockTimeoutInSeparateTransaction(long timeoutDurationInMs) {
    }

    public void setLockTimeout(long timeoutDurationInMs, Query query) {
    }

    public long getLockTimeout() {
        throw new UnsupportedOperationException(UNSUPPORTED_METHOD_ERROR_MESSAGE);
    }

    public void setLockTimeoutIfRequired() {
        long lockTimeOutInMsForQueryGetItem = concurrencyPessimisticLockingConfig.getLockTimeOutInMsForQueryGetItem();
        setLockTimeout(lockTimeOutInMsForQueryGetItem);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setLockTimeoutIfRequiredInSeparateTransaction() {
        long lockTimeOutInMsForQueryGetItem = concurrencyPessimisticLockingConfig.getLockTimeOutInMsForQueryGetItem();
        setLockTimeoutInSeparateTransaction(lockTimeOutInMsForQueryGetItem);
    }

    public Query setLockTimeoutIfRequired(Query query) {
        boolean requiredToSetLockTimeoutForEveryQuery = concurrencyPessimisticLockingConfig.isRequiredToSetLockTimeoutForEveryQuery();
        long lockTimeOutInMsForQueryGetItem = concurrencyPessimisticLockingConfig.getLockTimeOutInMsForQueryGetItem();
        boolean requiredToSetLockTimeoutQueryHint = concurrencyPessimisticLockingConfig.isRequiredToSetLockTimeoutQueryHint();
        setLockTimeout(lockTimeOutInMsForQueryGetItem, query);

        // applicable for postgres
        // if (requiredToSetLockTimeoutForEveryQuery) {
        //     log.info("... set lockTimeOut {} ms through native query ...", lockTimeOutInMsForQueryGetItem);
        //     setLockTimeout(lockTimeOutInMsForQueryGetItem);
        // }

        // applicable for oracle
        // if (requiredToSetLockTimeoutQueryHint) {
        //     log.info("... set lockTimeOut {} ms through query hint ...", lockTimeOutInMsForQueryGetItem);
        //     query.setHint("javax.persistence.lock.timeout", String.valueOf(lockTimeOutInMsForQueryGetItem));
        // }

        return query;
    }

    public void insertArtificialDelayAtTheEndOfTheQueryForTestsOnly() {
        // for testing purposes only
    }

}