package com.gj.frameworks.data.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Configuration properties for Data.
 */
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties implements BeanFactoryAware {

	public static final String PREFIX = "gj.data";

	private BeanFactory beanFactory;

	private String basePackage;
	
	/**
	 * 若不设置group值，将返回Spring Boot Application所在的包 默认只需把Spring Boot
	 * Application所在的类放在上一层的包中即可
	 */
	public String getBasePackage() {
		if (basePackage != null) {
			return basePackage;
		}
		try {
			return AutoConfigurationPackages.get(beanFactory).get(0);
		} catch (Exception e) {
			return null;
		}
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	public String getTypeAliasPackage() {
		return getBasePackage();
	}
	
	public String getTypeHandlersPackage() {
		return getBasePackage();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
}