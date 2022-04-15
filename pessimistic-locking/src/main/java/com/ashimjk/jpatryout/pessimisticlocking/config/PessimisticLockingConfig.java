package com.ashimjk.jpatryout.pessimisticlocking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "pessimistic-locking")
public class PessimisticLockingConfig {

    private long lockTimeOutInMs;
    private Test test;

    public long getMinimalPossibleLockTimeOutInMs() {
        return test.getMinimalPossibleLockTimeOutInMs();
    }

    public long getDelayAtTheEndOfTheQueryForPessimisticLockingTestingInMs() {
        return test.getDelayAtTheEndOfTheQueryForPessimisticLockingTestingInMs();
    }

    @Getter
    @Setter
    static class Test {

        private long minimalPossibleLockTimeOutInMs;
        private long delayAtTheEndOfTheQueryForPessimisticLockingTestingInMs;

    }

}
