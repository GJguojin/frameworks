package com.gj.frameworks.cache;

import java.io.Serializable;

public interface Cacheable extends Serializable{
	
	/**
	 * 对象存储到cache中的命名空间
	 * @return
	 */
	String getNamespace();
	
	/**
	 * 获取Cache对象的Key
	 * @return
	 */
	String getCacheKey();

}
