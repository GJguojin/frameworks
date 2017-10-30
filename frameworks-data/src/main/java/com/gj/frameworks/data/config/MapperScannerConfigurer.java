package com.gj.frameworks.data.config;

import java.util.Properties;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import com.gj.frameworks.data.dao.Mapper;
import com.gj.frameworks.data.orm.mybatis.MapperHelper;

import tk.mybatis.mapper.common.Marker;


public class MapperScannerConfigurer implements ImportBeanDefinitionRegistrar, 
	ResourceLoaderAware, EnvironmentAware, BeanFactoryAware {

	private static Logger logger = LoggerFactory.getLogger(MapperScannerConfigurer.class);
	
	private static final String basePackagesKey = "gj.data.base_package";

	private ResourceLoader resourceLoader;
	private Environment environment;
	private BeanFactory beanFactory;
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		MapperHelper mapperHelper = new MapperHelper();
		mapperHelper.registerMapper(Mapper.class);
		Properties mapperProperties = new Properties();
		mapperProperties.setProperty("style", "normal");
		mapperHelper.setProperties(mapperProperties);
		
		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		scanner.setEnvironment(environment);
		scanner.setResourceLoader(resourceLoader);
		scanner.setMarkerInterface(Marker.class);
		scanner.registerFilters();
		
		String defaultPackages = AutoConfigurationPackages.get(beanFactory).get(0);
		String  basePackages = "${"+basePackagesKey+":"+defaultPackages+"}";
		try {
			logger.info("MyBatis auto-configuration package '{}'", basePackages);
			scanner.doScan(basePackages);
		} catch (IllegalStateException ex) {
			logger.debug("Could not determine auto-configuration " + "package, automatic mapper scanning disabled.");
		}
		
        String[] names = registry.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(name);
            if (beanDefinition instanceof GenericBeanDefinition) {
            	GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinition;
                if (MapperFactoryBean.class.getName().equals(definition.getBeanClassName())) {
                    definition.setBeanClass(tk.mybatis.spring.mapper.MapperFactoryBean.class);
                    definition.getPropertyValues().add("mapperHelper", mapperHelper);
                }
            }
        }
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}