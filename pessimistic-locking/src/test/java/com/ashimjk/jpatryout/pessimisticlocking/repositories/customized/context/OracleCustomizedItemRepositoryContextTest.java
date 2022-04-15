package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized.context;

import com.ashimjk.jpatryout.pessimisticlocking.config.PessimisticLockingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("oracle")
class OracleCustomizedItemRepositoryContextTest {

    @Autowired private PessimisticLockingConfig pessimisticLockingConfig;

    @SpyBean private CustomizedItemRepositoryContext customizedItemRepositoryContext;

    @Test
    void shouldSetAndGetLockTimeOut() {
        long lockTimeOutInMsForQueryGetItem = pessimisticLockingConfig.getLockTimeOutInMs();
        assertThrows(UnsupportedOperationException.class, () -> customizedItemRepositoryContext.setLockTimeout(0));
        assertThat(customizedItemRepositoryContext.getLockTimeout()).isEqualTo(lockTimeOutInMsForQueryGetItem);
    }

}