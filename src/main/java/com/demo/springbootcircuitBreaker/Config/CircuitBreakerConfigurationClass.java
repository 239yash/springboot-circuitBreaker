package com.demo.springbootcircuitBreaker.Config;

import com.demo.springbootcircuitBreaker.Exception.TestException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfigurationClass {
    private static CircuitBreaker circuitBreaker;

    private CircuitBreakerConfig createCircuitBreakerConfiguration() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .minimumNumberOfCalls(10)
                .slidingWindowSize(100)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(3)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .recordExceptions(TestException.class)
                .build();
    }

    @Bean
    public void createCircuitBreakerInstance() {
        // Create a CircuitBreakerRegistry with a custom global configuration
        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(createCircuitBreakerConfiguration());
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("cbDemo");
    }

    public CircuitBreaker getCircuitBreakerInstance() {
        return circuitBreaker;
    }
}
