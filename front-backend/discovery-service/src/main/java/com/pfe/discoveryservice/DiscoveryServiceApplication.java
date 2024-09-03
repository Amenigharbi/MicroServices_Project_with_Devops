package com.pfe.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;




@SpringBootApplication
@EnableEurekaServer //L’annotation @EnableEurekaServer est utilisée pour que mon application Spring Boot agisse comme un serveur Eureka.
public class DiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }

}
