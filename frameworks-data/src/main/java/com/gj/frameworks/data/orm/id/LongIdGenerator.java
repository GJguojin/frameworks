package com.gj.frameworks.data.orm.id;

public class LongIdGenerator{
	
	/**
	 * 调用Spring初始化的IdGenerator
	 */
	private static IdGenerator<Long> idGeneratorInstance;

	public static Long generate() {
		return idGeneratorInstance.generate();
	}

	protected static void setIdGeneratorInstance(IdGenerator<Long> idGeneratorInstance) {
		LongIdGenerator.idGeneratorInstance = idGeneratorInstance;
	}

}
