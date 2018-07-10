package com.vbgps.dao.mybatis;

import com.vbgps.dao.MybatisApi;
import com.vbgps.dao.pojo.MyBatisEntity;

public abstract class MyBatisBaseApi<T extends MyBatisEntity> implements MybatisApi<T> {

	@Override
	public T findById(Number id) {
		return null;
	}

	@Override
	public void save(T entity) {

	}

	@Override
	public void update(T entity) {

	}

	@Override
	public void deleteById(Number id) {

	}

}
