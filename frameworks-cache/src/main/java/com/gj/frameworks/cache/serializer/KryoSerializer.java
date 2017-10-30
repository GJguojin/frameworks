package com.gj.frameworks.cache.serializer;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Kryo.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gj.frameworks.cache.Cacheable;


/**
 * Kryo序列化方式
 */
public class KryoSerializer implements RedisSerializer<Object> {
	
	private Kryo kryo;
	
	public KryoSerializer() {
		super();
		kryo = new Kryo();
		kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
	}

	private static final int BUFFER_SIZE = 1024;

	@Override
	public byte[] serialize(Object cacheable) throws SerializationException {
		Output output = new Output(BUFFER_SIZE,-1);
		kryo.writeClassAndObject(output, cacheable);
		return output.toBytes();
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		Input input = new Input();
		input.setBuffer(bytes);
		return (Object) kryo.readClassAndObject(input);
	}
}
