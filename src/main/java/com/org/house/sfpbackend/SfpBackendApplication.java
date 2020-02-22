package com.org.house.sfpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableWebSecurity
@SpringBootApplication
@EnableRedisHttpSession
public class SfpBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SfpBackendApplication.class, args);
    }
}
