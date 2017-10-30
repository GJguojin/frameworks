package com.gj.frameworks.data.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gj.frameworks.data.dao.BaseDao;
import com.gj.frameworks.entity.IdEntity;
import com.gj.frameworks.service.BaseService;


/**
 * 通用Service, 基于Spring泛型注入，子类只需基础该类即可直接调用基础方法
 * 
 * @author zhouree
 * @param <T>
 */
public class BaseCrudService<T extends IdEntity> implements BaseService<T> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected BaseDao<T> dao;

	@Override
	public T get(Long id) {
		return dao.selectByPrimaryKey(id);
	}

	@Override
	public T create(T entity) {
		dao.insertSelective(entity);
		return entity;
	}

	@Override
	public boolean delete(Long id) {
		return dao.deleteByPrimaryKey(id) == 1;
	}

	@Override
	public boolean update(T entity) {
		return dao.updateByPrimaryKey(entity) == 1;
	}

	@Override
	public boolean updateSelective(T entity) {
		return dao.updateByPrimaryKeySelective(entity) == 1;
	}

	@Override
	public T selectOne(T entity) {
		return dao.selectOne(entity);
	}

	@Override
	public List<T> query(T entity) {
		return dao.select(entity);
	}

}
