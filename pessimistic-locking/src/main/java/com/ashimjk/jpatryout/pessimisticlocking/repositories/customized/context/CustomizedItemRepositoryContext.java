package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.config.PessimisticLockingConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;

@Slf4j
@RequiredArgsConstructor
public abstract class CustomizedItemRepositoryContext {

    private static final String UNSUPPORTED_METHOD_ERROR_MESSAGE = "Method invocation not allowed!";

    protected final PessimisticLockingConfig pessimisticLockingConfig;

    public abstract Query configureLockTimeout(Query query);

    public void setLockTimeout(long timeoutDurationInMs) {
        throw new UnsupportedOperationException(UNSUPPORTED_METHOD_ERROR_MESSAGE);
    }

    public long getLockTimeout() {
        return pessimisticLockingConfig.getLockTimeOutInMs();
    }

    public void insertArtificialDelayAtTheEndOfTheQueryForTestsOnly() {
        // for testing purposes only
    }

}