package com.su.gateway;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.SocketUtils;

import java.time.Duration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT)
public class GatewayApplicationTests {

    @LocalServerPort
    protected int port = 0;

    protected static int managementPort;

    protected WebTestClient webClient;
    protected String baseUri;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeClass
    public static void beforeClass() {
        managementPort = SocketUtils.findAvailableTcpPort();

        System.setProperty("management.server.port", String.valueOf(managementPort));
    }

    @AfterClass
    public static void afterClass() {
        System.clearProperty("management.server.port");
    }

    @Before
    public void setup() {
        baseUri = "http://localhost:" + port;
        this.webClient = WebTestClient.bindToServer().responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
    }

    @Test
    public void contextLoads() {
        webClient.get()
                .uri("/get")
                .exchange()
                .expectStatus()
                .is3xxRedirection()
//                .isOk()
        ;

        ResponseEntity responseEntity = restTemplate.getForEntity(baseUri + "", String.class,"");
        HttpStatus httpStatus = responseEntity.getStatusCode();
        System.out.println("httpStatus:" + httpStatus);

    }


}
