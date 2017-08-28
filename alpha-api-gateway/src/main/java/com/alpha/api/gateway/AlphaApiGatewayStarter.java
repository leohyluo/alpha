package com.alpha.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class AlphaApiGatewayStarter 
{
    public static void main(String[] args)
    {
        SpringApplication.run(AlphaApiGatewayStarter.class, args);
    }
}