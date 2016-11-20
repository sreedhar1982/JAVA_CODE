package com.spring;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@RestController
@EnableCircuitBreaker
@EnableDiscoveryClient
public class HelloController {

    @RequestMapping("/test/{id}")
	@HystrixCommand(fallbackMethod = "reliable")
    public String index() {
        return "Greetings from Spring Boot!";
    }
 public String reliable() {
    return "Cloud Native Java (O'Reilly)";
  }
}