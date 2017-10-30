package com.gj.frameworks.data.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.gj.frameworks.data.orm.id.IdGenerator;
import com.gj.frameworks.data.orm.id.TwitterLongIdGenerator;
import com.gj.frameworks.data.orm.mybatis.MybatisSessionFactoryBean;

/**
 * {@link EnableAutoConfiguration Auto-Configuration} for Mybatis. Contributes a
 * {@link SqlSessionFactory} and a {@link SqlSessionTemplate}.
 *
 * If {@link org.mybatis.spring.annotation.MapperScan} is used, or a configuration file is
 * specified as a property, those will be considered, otherwise this auto-configuration
 * will attempt to register mappers based on the interface definitions in or under the
 * root auto-configuration package.
 *
 * @author Eddú Meléndez
 * @author Josh Long
 */
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE+40)
@Import(DataSourceAutoConfiguration.class)
@EnableTransactionManagement(proxyTargetClass = true)
public class MybatisAutoConfiguration{

	private static Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);
	
	private static final String CONFIG_LOCATION = "classpath:mybatis-config.xml";
	
	@Autowired
	private MybatisProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		MybatisSessionFactoryBean factory = new MybatisSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setConfigLocation(this.resourceLoader.getResource(CONFIG_LOCATION));
		//factory.setTypeAliasesSuperType(TypeAliases.class);
		String typeAliasesPackage = properties.getTypeAliasPackage();
		factory.setPackagesToScan(typeAliasesPackage);
		return factory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory,ExecutorType.SIMPLE);
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	public IdGenerator idGenerator() {
		return new TwitterLongIdGenerator();
	}
	
	/**
	 * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up creating
	 * instances of {@link MapperFactoryBean}. If
	 * {@link org.mybatis.spring.annotation.MapperScan} is used then this
	 * auto-configuration is not needed. If it is _not_ used, however, then this will
	 * bring in a bean registrar and automatically register components based on the same
	 * component-scanning path as Spring Boot itself.
	 */
	@Configuration
	@EnableConfigurationProperties(MybatisProperties.class)
	@Import({ MapperScannerConfigurer.class })
	public static class MapperScannerRegistrarNotFoundConfiguration {

		@PostConstruct
		public void afterPropertiesSet() {
			logger.info("No {} found.", MapperFactoryBean.class.getName());
		}
	}
	
}