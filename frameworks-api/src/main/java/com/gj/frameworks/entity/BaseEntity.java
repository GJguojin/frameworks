package com.gj.frameworks.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gj.frameworks.cache.CacheKeyStrategy;
import com.gj.frameworks.cache.Cacheable;
import com.gj.frameworks.support.TypeAliases;

/**
 * 基本的实体基类，继承自{@link IdEntity},实现了{@link TypeAliases}接口，可自动生成Mybatis简称
 * @author zhouree
 */
public abstract class BaseEntity extends IdEntity implements Cacheable, TypeAliases{
	
	private static final long serialVersionUID = 1L;
	
	private static final String CACHE_NAMESPACE = "entity" ;

	public BaseEntity() {
		super();
	}

	public BaseEntity(Long id) {
		super(id);
	}
	
	@Override
	public String toString() {
		return getId() == null ? super.toString() : getClass().getName()+ "[id=" + id + "]";
	}

	@JsonIgnore
	@Override
	public String getNamespace() {
		return CACHE_NAMESPACE;
	}

	/**
	 * 获取cacheKey
	 */
	@JsonIgnore
	@Override
	public String getCacheKey() {
		String group = getClass().getSimpleName().toLowerCase();
		return CacheKeyStrategy.getCacheKey(id,getNamespace(), group);
	}

}
