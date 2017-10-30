package com.gj.frameworks.data.dao;

import java.io.Serializable;

import tk.mybatis.mapper.common.Marker;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.insert.InsertMapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.SelectCountMapper;
import tk.mybatis.mapper.common.base.select.SelectMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;


/**
 * 通用Mapper接口,其他接口继承该接口即可
 * @author zhouree
 *
 * @param <T>
 */
public interface Mapper<T,PK extends Serializable> extends Marker,
		SelectOneMapper<T>,
		SelectMapper<T>,
		SelectCountMapper<T>,
		SelectByPrimaryKeyMapper<T>,
		InsertMapper<T>,
        InsertSelectiveMapper<T>,
        UpdateByPrimaryKeyMapper<T>,
        UpdateByPrimaryKeySelectiveMapper<T>,
        DeleteByPrimaryKeyMapper<T>{

}
