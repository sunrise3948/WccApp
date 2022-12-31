package com.chasingdns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.chasingdns.controller","com.chasingdns.service","com.chasingdns.repository","com.chasingdns.exception","com.chasingdns.security"})
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.chasingdns.entity"})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
