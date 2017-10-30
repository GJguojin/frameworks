package com.gj.frameworks.cache;

public interface CacheClient<T> {
	
	public static final int ONEWEEK = 7*24*3600;//一周
	public static final int ONEDAY = 24*3600;//一天
	public static final int ONEHOUR = 3600;//一小时
	public static final int HALFHOUR = 1800;//半小时
	
	T get(String key);

	/**
	 * Get item and set a new expiration time for it
	 * 
	 * @param key
	 * @param seconds New expiration time, in seconds.
	 * @return
	 */
	T getAndTouch(final String key, int seconds);

	/**
	 * Set a new expiration time for an existing item,using default opTimeout second.
	 * 
	 * @param key item's key
	 * @param seconds New expiration time, in seconds. Can be up to 30 days. After
	 *            30 days, is treated as a unix timestamp of an exact date.
	 */
	boolean touch(final String key, int seconds);

	/**
	 * 向cache服务器添加一个要缓存的数据,如果cache服务器中已经存在要存储的key，此时add方法调用失败.无过期时间，默认一周有效
	 * 
	 * @param key key to store data under
	 * @param data data to store
	 * @return true, if the data was successfully stored
	 */
	boolean add(String key, T data);

	/**
	 * 向cache服务器添加一个要缓存的数据,如果cache服务器中已经存在要存储的key，此时add方法调用失败
	 * 
	 * @param key key to store data under
	 * @param data data to store
	 * @param seconds 过期时间，单位秒
	 * @return true, if the data was successfully stored
	 */
	boolean add(String key, T data, int seconds);

	/**
	 * 如果要设置的key不存在时，则set方法同add方法，如果要设置的key已经存在时，则set方法同replace方法,无过期时间，默认一周有效
	 * 
	 * @param key key to store data under
	 * @param data data to store
	 * @return true, if the data was successfully stored
	 */
	boolean set(String key, T data);

	/**
	 * 如果要设置的key不存在时，则set方法同add方法，如果要设置的key已经存在时，则set方法同replace方法
	 * 
	 * @param key key to store data under
	 * @param data data to store
	 * @param seconds 过期时间，单位秒
	 * @return true, if the data was successfully stored
	 */
	boolean set(String key, T data, int seconds);

	/**
	 * 删除cache中的对象
	 * 
	 * @param key
	 * @return
	 */
	boolean delete(String key);

	/**
	 * 只对已经存在的数据就行替换，数据不存在返回false,默认
	 * @param key
	 * @param data
	 * @return
	 */
	boolean replace(String key, T data);

	/**
	 * 只对已经存在的数据就行替换，数据不存在返回false
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	boolean replace(String key, T value, int seconds);
	
	/**
	 * The TTL command returns the remaining time to live in seconds of a key that has an EXPIRE set. 
	 * @param key
	 * @return
	 */
	Long ttl(final String key);
}
