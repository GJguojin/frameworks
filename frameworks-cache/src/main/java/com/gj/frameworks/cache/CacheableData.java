package com.gj.frameworks.cache;


/**
 * 通用Cache数据对象
 */
public class CacheableData implements Cacheable {

	private static final long serialVersionUID = 1L;

	public static final String GROUP = "common";
	public static final String NAMESPACE = "shared";

	private String group;
	private String key;
	private Object value;

	public CacheableData(String key, Object value, String group) {
		super();
		this.key = key;
		this.value = value;
		this.group = group;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public String getCacheKey() {
		return CacheKeyStrategy.getCacheKey(key, NAMESPACE, group);
	}
}
