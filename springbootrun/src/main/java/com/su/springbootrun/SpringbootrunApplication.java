package com.su.springbootrun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;


/**
 * todo
 * 1。@SpringBootApplication运行机制
 * 2。Autoconfig是怎么把bean注入到spring容器的
 */
@RestController
@SpringBootApplication
public class SpringbootrunApplication {

    public static void main(String[] args) {

        StopWatch sw = new StopWatch();
        sw.start("springboot启动过程");
        SpringApplication.run(SpringbootrunApplication.class, args);
        sw.stop();
        System.out.println(sw.prettyPrint());
        System.out.println(sw.getTotalTimeMillis());
        System.out.println(sw.getLastTaskName());
        System.out.println(sw.getLastTaskInfo());
        System.out.println(sw.getTaskCount());

    }

//    @Autowired
//    private Hello nihao;

    @GetMapping("/test")
    public Object test() {

//        nihao.sayHi();

        return ZonedDateTime.now();
    }

}
