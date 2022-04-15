package com.ashimjk.jpatryout.pessimisticlocking.repositories.customized;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "concurrency.pessimistic-locking")
public class ConcurrencyPessimisticLockingConfig {

    private boolean requiredToSetLockTimeoutForEveryQuery;
    private boolean requiredToSetLockTimeoutQueryHint;
    private long lockTimeOutInMsForQueryGetItem;
    private Test test;

    public long getMinimalPossibleLockTimeOutInMs() {
        return test.getMinimalPossibleLockTimeOutInMs();
    }

    public boolean isRequiredToSetLockTimeoutForTestsAtStartup() {
        return test.isRequiredToSetLockTimeoutForTestsAtStartup();
    }

    public long getDelayAtTheEndOfTheQueryForPessimisticLockingTestingInMs() {
        return test.getDelayAtTheEndOfTheQueryForPessimisticLockingTestingInMs();
    }

    @Getter
    @Setter
    static class Test {

        private boolean requiredToSetLockTimeoutForTestsAtStartup;
        private long minimalPossibleLockTimeOutInMs;
        private long delayAtTheEndOfTheQueryForPessimisticLockingTestingInMs;

    }

}
