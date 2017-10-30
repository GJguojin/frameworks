package com.gj.frameworks.service;

import java.util.List;

import com.gj.frameworks.entity.IdEntity;

public interface BaseService<T extends IdEntity> {
	
	T get(Long id);
	
	/**
	 * 创建实体对象，null的属性不会保存，会使用数据库默认值
	 */
	T create(T entity);
	
	boolean delete(Long id);
	
	/**
	 * 更新实体全部字段，null值会被更新
	 * @param entity
	 * @return
	 */
	boolean update(T entity);
	
	/**
	 * 只更新不为null的字段
	 * @param entity
	 * @return
	 */
	boolean updateSelective(T entity);
	
	/**
	 * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
	 * @param entity
	 * @return
	 */
	T selectOne(T entity);
	
	/**
	 * 根据实体中的属性值进行查询，查询条件使用等号
	 * @param entity
	 * @return
	 */
	List<T> query(T entity);

}
