package com.alpha.commons.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
public class DruidConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);
	
	@Value("${db.host}")
	private String ip;
	
	@Value("${db.port}")
	private String port;
	
	@Value("${db.name}")
	private String dbName;
	
	//@Value("${db.encrypt.flag}")
	private String encryptFlag;

	@ConditionalOnClass(DruidDataSource.class)
	@ConditionalOnProperty(name="spring.datasource.type", havingValue="com.alibaba.druid.pool.DruidDataSource", matchIfMissing=true)
	class Druid extends DruidConfiguration {
		
		@Bean(name = "dataSource")
		@ConfigurationProperties("spring.datasource.druid")
		public DruidDataSource dataSource(DataSourceProperties properties) {
			
			if("1".equals(encryptFlag)) {
				/*properties.setUsername(properties.getUsername());
				properties.setPassword(properties.getPassword());
				
				StringBuffer sb = new StringBuffer("jdbc:mysql://");
				sb.append(Encrypt.dCode(ip.getBytes())).append(":").append(port).append("/")
				.append(Encrypt.dCode(dbName.getBytes()))
				.append("?useUnicode=true&characterEncoding=utf8&useOldAliasMetadataBehavior=true");
				
				String url = sb.toString();
				properties.setUrl(url);
				*/
			}
			
			DruidDataSource druidDataSource = (DruidDataSource) properties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
			DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
			String validationQuery = databaseDriver.getValidationQuery();
			if(validationQuery != null) {
				druidDataSource.setValidationQuery(validationQuery);
			}
			System.out.println("DruidConfiguration init completed................");
			logger.info("DruidConfiguration init completed................");
			return druidDataSource;
		}

		@Bean(name = "sqlSessionFactory")
		@Primary
		public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
			SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
			bean.setDataSource(dataSource);
			bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:/mybatis-config.xml"));
			bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/*.xml"));

			return bean.getObject();
		}

		@Bean(name = "sqlSessionTemplate")
		@Primary
		public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
			return new SqlSessionTemplate(sqlSessionFactory);
		}
	}
}
