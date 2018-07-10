package com.vbgps.dao.mybatis;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.vbgps.dao.MybatisApi;
import com.vbgps.dao.entity.MyBatisEntity;
import com.vbgps.dao.mybatis.sql.BaseSqlBuild;

public abstract class MyBatisBaseApi<T extends MyBatisEntity> implements MybatisApi<T> {

	@SelectProvider(method = "findById", type = BaseSqlBuild.class)
	@Override
	public T findById(Number id) {
		return null;
	}

	@InsertProvider(method = "insert", type = BaseSqlBuild.class)
	@Override
	public void save(T entity) {

	}

	@UpdateProvider(method = "update", type = BaseSqlBuild.class)
	@Override
	public void update(T entity) {

	}

	@Override
	public void deleteById(Number id) {

	}

}
