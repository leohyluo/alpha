package com.alpha.self.diagnosis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xc.xiong on 2017/8/31.
 */
@MapperScan(basePackages = "com.alpha.**.mapper",sqlSessionTemplateRef  = "sqlSessionTemplate")
@ServletComponentScan(basePackages={"com.alpha"})
@ComponentScan(basePackages={"com.alpha"})
@EnableFeignClients(basePackages="com.alpha.server.rpc.user")
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class AlphaApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AlphaApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(AlphaApplication.class, args);
    }
}
