package com.gj.frameworks.data.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceAutoConfiguration {

	@Autowired
	private DataSourceProperties properties;

	
	@Bean
	public HikariDataSource dataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(properties.getUrl());
		ds.setUsername(properties.getUsername());
		ds.setPassword(properties.getPassword());
		ds.setMaximumPoolSize(properties.getMaxPoolSize());
		ds.setMinimumIdle(properties.getMinIdle());
		return ds;
	}
	

}
