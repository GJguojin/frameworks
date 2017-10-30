package com.gj.frameworks.data.dao;

import com.gj.frameworks.entity.IdEntity;

public interface BaseDao<T extends IdEntity> extends Mapper<T,Long>{

}
