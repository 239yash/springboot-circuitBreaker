package com.demo.springbootcircuitBreaker.Service;

import com.demo.springbootcircuitBreaker.Config.CircuitBreakerConfigurationClass;
import com.demo.springbootcircuitBreaker.Exception.TestException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
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
    private static Integer counter = 0;
    public String testMethod() {
        counter++;
        CircuitBreaker circuitBreaker = circuitBreakerConfigurationClass.getCircuitBreakerInstance();
        log.info("Counter - " + counter + " Circuit Breaker State - " + circuitBreaker.getState());
        Supplier<String> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, this::thirdPartyServiceCall);
        try {
            return "Counter - " + counter + " " + decoratedSupplier.get();
        } catch (TestException e) {
            // Handle exception or log
            return "Fallback response: " + e.getMessage();
        } catch (CallNotPermittedException ex) {
            return "CallNotPermitted response: " + ex.getMessage();
        }
    }


    private String thirdPartyServiceCall() {
        throw new TestException("Remote service failed");
    }
}
