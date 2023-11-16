package com.demo.springbootcircuitBreaker.Service;

import com.demo.springbootcircuitBreaker.Config.CircuitBreakerConfigurationClass;
import com.demo.springbootcircuitBreaker.Exception.TestException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@Slf4j
@AllArgsConstructor
public class MainService {
    private final CircuitBreakerConfigurationClass circuitBreakerConfigurationClass;
    private static Integer counter = 1;
    public String testMethod() {
        CircuitBreaker circuitBreaker = circuitBreakerConfigurationClass.getCircuitBreakerInstance();
        log.info("Counter - " + counter + " Circuit Breaker State - " + circuitBreaker.getState());
        Supplier<String> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, this::thirdPartyServiceCall);
        try {
            return "Counter - " + counter + " " + decoratedSupplier.get();
        } catch (Exception e) {
            // Handle exception or log
            return "Fallback response: " + e.getMessage();
        } finally {
            counter++;
        }
    }


    private String thirdPartyServiceCall() {
        if (Math.random() < 0.7) {
            throw new TestException("Remote service failed");
        }
        return "Remote call successful";
    }
}
