package com.demo.springbootcircuitBreaker.Controller;

import com.demo.springbootcircuitBreaker.Service.MainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
@AllArgsConstructor
public class RestApiController {
    private final MainService mainService;
    @GetMapping
    public String getController() {
        mainService.testMethod();
        return "done";
    }
}
