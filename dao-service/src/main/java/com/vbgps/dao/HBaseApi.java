package com.vbgps.dao;

import java.io.IOException;
import java.util.List;

import com.vbgps.dao.entity.HBaseEntity;

public interface HBaseApi {

	<T extends HBaseEntity> void put(T object) throws IOException;

	<T extends HBaseEntity> T get(Class<T> clazz, byte[] rowKey) throws Exception;

	<T extends HBaseEntity> List<T> findList(Class<T> clazz, byte[] startRow, byte[] stopRow) throws Exception;
}
