package com.gj.frameworks.data.orm.mybatis;

import java.util.ArrayList;

import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;


/**
 * 继承tk.mybatis.mapper.mapperhelper.MapperHelper，重写processConfiguration方法
 * @author zhouree
 *
 */
public class MapperHelper extends tk.mybatis.mapper.mapperhelper.MapperHelper {

	/**
	 * 配置指定的接口
	 *
	 * @param configuration
	 * @param mapperInterface
	 */
	@Override
	public void processConfiguration(Configuration configuration, Class<?> mapperInterface) {
		String prefix = mapperInterface != null ? mapperInterface.getCanonicalName() : "";
		for (Object object : new ArrayList<Object>(configuration.getMappedStatements())) {
			if (object instanceof MappedStatement) {
				MappedStatement ms = (MappedStatement) object;
				if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
					MetaObject msObject = SystemMetaObject.forObject(ms);
					msObject.setValue("keyGenerator", new UUIDKeyGenerator());
				}
				if (ms.getId().startsWith(prefix) && isMapperMethod(ms.getId())) {
					if (ms.getSqlSource() instanceof ProviderSqlSource) {
						setSqlSource(ms);
					}
				}
			}
		}
	}

}
