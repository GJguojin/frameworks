package com.gj.frameworks.data.orm.mybatis;

import java.sql.Statement;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import com.gj.frameworks.data.orm.id.LongIdGenerator;
import com.gj.frameworks.entity.IdEntity;

public class UUIDKeyGenerator implements KeyGenerator {

	@Override
	public void processBefore(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		if (parameter instanceof IdEntity) {
			((IdEntity) parameter).setId(LongIdGenerator.generate());
		}
	}

	@Override
	public void processAfter(Executor executor, MappedStatement ms, Statement stmt, Object parameter) {
		// do nothing
	}

}
