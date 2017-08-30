package com.alpha.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan(basePackages={"com.alpha"})
@MapperScan("com.alpha.**.mapper")
@ComponentScan(basePackages={"com.alpha.server.rpc.user,com.alpha"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@SpringBootApplication
public class AlphaUserStarter 
{
    public static void main(String[] args)
    {
        SpringApplication.run(AlphaUserStarter.class, args);
    }
}
