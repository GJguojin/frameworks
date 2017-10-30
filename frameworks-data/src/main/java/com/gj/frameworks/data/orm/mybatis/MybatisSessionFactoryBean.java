package com.gj.frameworks.data.orm.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.gj.frameworks.support.TypeAliases;


public class MybatisSessionFactoryBean extends SqlSessionFactoryBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String RESOURCE_PATTERN = "/**/*.class";

	private String[] packagesToScan;
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private final TypeFilter filter = new AssignableTypeFilter(TypeAliases.class);

	@Override
	public void afterPropertiesSet() throws Exception {
		if (packagesToScan != null && packagesToScan.length > 0) {
			scanPackages();
		}
		super.afterPropertiesSet();
	}

	/**
	 * 自动扫描TypeAliases和TypeHandler
	 * 
	 * @return
	 * @throws Exception
	 */
	protected void scanPackages() throws Exception {
		List<Class<?>> list = new ArrayList<Class<?>>();
		try {
			for (String pkg : packagesToScan) {
				logger.info("Scan MyBatis type aliases packages:{}",pkg);
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
						+ ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
				Resource[] resources = this.resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if (filter.match(reader, readerFactory)) {
							list.add(this.resourcePatternResolver.getClassLoader().loadClass(className));
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new PersistenceException("Failed to scan classpath for unlisted classes", ex);
		}
		Class<?>[] typeAliases = new Class<?>[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Class<?> clazz = list.get(i);
			typeAliases[i] = clazz;
		}
		setTypeAliases(typeAliases);
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

}
