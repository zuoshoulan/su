package com.su.chatgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@SpringBootApplication
public class ChatgoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatgoApplication.class, args);
    }

}
