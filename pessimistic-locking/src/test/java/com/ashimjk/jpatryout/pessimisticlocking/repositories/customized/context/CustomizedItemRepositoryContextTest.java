package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.ConcurrencyPessimisticLockingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomizedItemRepositoryContextTest {

    @Autowired private PlatformTransactionManager transactionManager;

    @SpyBean private CustomizedItemRepositoryContext customizedItemRepositoryContext;
    @SpyBean private ConcurrencyPessimisticLockingConfig concurrencyPessimisticLockingConfig;

    @Test
    void shouldSetAndGetLockTimeOut() {
        if (concurrencyPessimisticLockingConfig.isRequiredToSetLockTimeoutQueryHint()) {
            assertThrows(UnsupportedOperationException.class, () -> customizedItemRepositoryContext.setLockTimeout(0));
            assertThrows(UnsupportedOperationException.class, () -> customizedItemRepositoryContext.getLockTimeout());
            return;
        }

        assertSetLockTimeOut(concurrencyPessimisticLockingConfig.getMinimalPossibleLockTimeOutInMs());
        assertSetLockTimeOut(TimeUnit.SECONDS.toMillis(2));
        assertSetLockTimeOut(TimeUnit.MINUTES.toMillis(2));
        assertSetLockTimeOut(TimeUnit.HOURS.toMillis(2));
        assertSetLockTimeOut(TimeUnit.DAYS.toMillis(2));
    }

    private void assertSetLockTimeOut(long expectedMilliseconds) {
        TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        customizedItemRepositoryContext.setLockTimeout(expectedMilliseconds);
        assertEquals(expectedMilliseconds, customizedItemRepositoryContext.getLockTimeout());
        transactionManager.commit(tx);
    }

}