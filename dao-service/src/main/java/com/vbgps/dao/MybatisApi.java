package com.vbgps.dao;

import com.vbgps.dao.pojo.MyBatisEntity;

public interface MybatisApi {

	<T extends MyBatisEntity> T findById(Number id);

	<T extends MyBatisEntity> void save(T entity);

	<T extends MyBatisEntity> void update(T entity);
	
	void deleteById(Number id);
}
