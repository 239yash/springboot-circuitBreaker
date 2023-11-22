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
    public void testMethod() {
        // Having a counter integer just for clarity on logs
        counter++;
        // Fetching our circuit breaker instance which was created initially in a bean
        CircuitBreaker circuitBreaker = circuitBreakerConfigurationClass.getCircuitBreakerInstance();
        log.info("Api call counter - " + counter + " and circuit breaker state - " + circuitBreaker.getState());

        try {
            // Decorate your call to third party call method with a CircuitBreaker
            // And execute the decorated supplier and recover from any exception
            Supplier<String> decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, this::thirdPartyServiceCall);
            log.info("Call Successful - " + decoratedSupplier.get());
        } catch (TestException e) {
            // Handle our custom test exception
            log.error("TestException - " + e.getMessage());
        } catch (CallNotPermittedException ex) {
            // Handle call not permitted exception which is provided by resilience4j-circuitBreaker
            log.error("CallNotPermittedException - " + ex.getMessage());
        }
    }

    private String thirdPartyServiceCall() {
        // Throwing our custom test exception everytime this method is called!
        throw new TestException("Remote service failed!");
    }
}
