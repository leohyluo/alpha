package com.alpha.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.alpha.server.rpc.user.SelfDiagnosisFallbackFactory;
import com.alpha.server.rpc.user.SelfDiagnosisFeign;

@ComponentScan(basePackages={"com.alpha.server.rpc.user,com.alpha"})
@EnableDiscoveryClient
//@EnableFeignClients
@EnableCircuitBreaker
@MapperScan("com.alpha.user.mapper.**")
@SpringBootApplication
public class AlphaUserStarter 
{
    public static void main(String[] args)
    {
        SpringApplication.run(AlphaUserStarter.class, args);
    }
}
