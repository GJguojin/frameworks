package com.gj.frameworks.cache;

/**
 * Cache Key生成策略
 */
public class CacheKeyStrategy {

	public static final String COLON = ":";

	public static String getCacheKey(Object id,String... prefixs) {
		return getRedisKey(id,prefixs);
	}

	/**
	 * redis key
	 * 
	 * @param namespace
	 * @param group
	 * @param key
	 * @return
	 */
	public static String getRedisKey(Object id,String... prefixs) {
		StringBuilder builder = new StringBuilder();
		for(String prefix:prefixs){
			if (prefix != null) {
				builder.append(prefix).append(COLON);
			}
		}
		return builder.append(String.valueOf(id)).toString();
	}

}
