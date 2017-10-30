package com.gj.frameworks.cache.serializer;

import org.apache.commons.lang3.ArrayUtils;
import org.nustaq.serialization.FSTConfiguration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.gj.frameworks.cache.Cacheable;


/**
 * 序列化方式
 */
public class FSTSerializer implements RedisSerializer<Object> {
	
	private FSTConfiguration conf;
	
	public FSTSerializer() {
		super();
		conf = FSTConfiguration.createDefaultConfiguration();
	}

	@Override
	public byte[] serialize(Object cacheable) throws SerializationException {
		if(cacheable == null){
			return ArrayUtils.EMPTY_BYTE_ARRAY;
		}
		return conf.asByteArray(cacheable);
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if(ArrayUtils.isEmpty(bytes)){
			return null;
		}
		return conf.asObject(bytes);
	}
}
