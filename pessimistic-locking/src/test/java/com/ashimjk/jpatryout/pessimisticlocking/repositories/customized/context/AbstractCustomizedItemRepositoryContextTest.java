package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.config.PessimisticLockingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
abstract class AbstractCustomizedItemRepositoryContextTest {

    @Autowired private PlatformTransactionManager transactionManager;

    @SpyBean private CustomizedItemRepositoryContext customizedItemRepositoryContext;
    @SpyBean private PessimisticLockingConfig pessimisticLockingConfig;

    @Test
    void shouldSetAndGetLockTimeOut() {
        assertSetLockTimeOut(pessimisticLockingConfig.getMinimalPossibleLockTimeOutInMs());
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