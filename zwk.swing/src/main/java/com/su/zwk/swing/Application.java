package com.su.zwk.swing;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        new SpringApplicationBuilder()
                .sources(Application.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
    }

}
