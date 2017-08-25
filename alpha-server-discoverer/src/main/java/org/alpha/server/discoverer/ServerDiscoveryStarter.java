package org.alpha.server.discoverer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServerDiscoveryStarter 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ServerDiscoveryStarter.class, args);
    }
}
