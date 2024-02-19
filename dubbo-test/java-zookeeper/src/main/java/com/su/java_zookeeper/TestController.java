package com.su.java_zookeeper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RequestMapping("/test")
@RestController
public class TestController {


    @GetMapping("/test")
    public Object test() {
        return "zookeeperr:" + ZonedDateTime.now();
    }

}
