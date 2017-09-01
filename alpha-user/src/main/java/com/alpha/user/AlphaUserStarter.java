package com.alpha.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.alpha.**.mapper")
@ServletComponentScan(basePackages={"com.alpha"})
@ComponentScan(basePackages={"com.alpha"})
@EnableFeignClients(basePackages="com.alpha.server.rpc.user")
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class AlphaUserStarter 
{
    public static void main(String[] args)
    {
        SpringApplication.run(AlphaUserStarter.class, args);
    }
}
