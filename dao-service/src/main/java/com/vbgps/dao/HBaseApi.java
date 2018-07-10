package com.vbgps.dao;

import java.util.List;

import com.vbgps.dao.pojo.HBaseEntity;

public interface HBaseApi {

	<T extends HBaseEntity> void put(T object);

	<T extends HBaseEntity> T get(byte[] rowKey);

	<T extends HBaseEntity> List<T> findList(byte[] startRow, byte[] endRow);

	<T extends HBaseEntity> List<T> findList(byte[] startRow, byte[] endRow, int limit);
}
