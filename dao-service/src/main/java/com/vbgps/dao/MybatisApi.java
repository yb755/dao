package com.vbgps.dao;


public interface MybatisApi<T> {

	T findById(Number id);

	void save(T entity);

	void update(T entity);

	void deleteById(Number id);
}
