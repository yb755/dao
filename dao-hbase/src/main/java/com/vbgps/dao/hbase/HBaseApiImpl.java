package com.vbgps.dao.hbase;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import com.vbgps.dao.HBaseApi;
import com.vbgps.dao.annotations.HBaseColumn;
import com.vbgps.dao.entity.HBaseEntity;
import com.vbgps.dao.util.ObjectUtil;
import com.vbgps.dao.util.ReflectionUtility;

public class HBaseApiImpl implements HBaseApi {

	private HTable htable = null;
	private ReentrantLock readLock = new ReentrantLock();

	private String zookeeperCluster;

	private String tableName;

	@PostConstruct
	private void init() throws IOException {
		if (htable != null) {
			return;
		}
		if (zookeeperCluster == null) {
			throw new RuntimeException("zookeeperCluster没有配置");
		}
		if (tableName == null) {
			throw new RuntimeException("tableName没有配置");
		}
		readLock.lock();
		try {
			Configuration conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", zookeeperCluster);
			// conf.set("hbase.zookeeper.property.clientPort", "2181");
			Connection conn = ConnectionFactory.createConnection(conf, Executors.newFixedThreadPool(10));
			htable = (HTable) conn.getTable(TableName.valueOf(tableName));
		} finally {
			readLock.unlock();
		}
	}

	public <T extends HBaseEntity> void put(T object) throws IOException {
		init();
		HBaseEntity entity = (HBaseEntity) object;
		byte[] rowKey = entity.getRowKey();
		Put put = new Put(rowKey);
		Class<? extends HBaseEntity> clazz = object.getClass();
		Field[] fields = ReflectionUtility.getAllField(clazz);
		int columnCount = 0;
		for (Field field : fields) {
			HBaseColumn hbaseColumn = field.getAnnotation(HBaseColumn.class);
			if (hbaseColumn != null) {
				String name = hbaseColumn.name();
				if (name == null) {
					name = field.getName();
				}
				Object obj = ReflectionUtility.getFieldValue(object, name);
				if (obj == null) {
					continue;
				}
				byte[] value = null;
				if (obj instanceof String) {
					value = ((String) obj).getBytes();
				} else if (obj instanceof Number) {
					value = obj.toString().getBytes();
				} else if (obj instanceof Date) {
					value = String.valueOf(((Date) obj).getTime() / 1000).getBytes();
				} else if (obj instanceof Boolean) {
					value = ((Boolean) obj) ? "1".getBytes() : "0".getBytes();
				} else if (obj instanceof Serializable) {
					value = ObjectUtil.toByteArray(obj);
				}
				if (value != null) {
					put.addColumn(hbaseColumn.family().getBytes(), name.getBytes(), value);
					columnCount++;
				}
			}
		}
		if (columnCount > 0) {
			htable.put(put);
		}
	}

	public <T extends HBaseEntity> T get(Class<T> clazz, byte[] rowKey) throws Exception {
		init();
		Result result = htable.get(new Get(rowKey));
		T instance = converResult2Object(result, clazz);
		return instance;
	}

	public <T extends HBaseEntity> List<T> findList(Class<T> clazz, byte[] startRow, byte[] stopRow) throws Exception {
		init();
		List<T> resultList = new ArrayList<T>();
		Scan scan = new Scan();
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		ResultScanner resultScanner = htable.getScanner(scan);
		resultScanner.forEach(new Consumer<Result>() {

			@Override
			public void accept(Result result) {
				T instance = null;
				try {
					instance = converResult2Object(result, clazz);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (instance != null) {
					resultList.add(instance);
				}
			}

		});
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	private <T extends HBaseEntity> T converResult2Object(Result result, Class<T> clazz) throws InstantiationException, IllegalAccessException {
		T object = clazz.newInstance();
		Field[] fields = ReflectionUtility.getAllField(clazz);
		int columnCount = 0;
		for (Field field : fields) {
			HBaseColumn hbaseColumn = field.getAnnotation(HBaseColumn.class);
			if (hbaseColumn != null) {
				String name = hbaseColumn.name();
				if (name == null) {
					name = field.getName();
				}
				Cell cell = result.getColumnLatestCell(hbaseColumn.family().getBytes(), name.getBytes());
				if (cell == null) {
					continue;
				}
				byte[] values = cell.getValueArray();

				Class typeClz = field.getType();
				String fieldName = field.getName();
				if (values != null) {
					if (typeClz == String.class) {
						ReflectionUtility.setFieldValue(object, fieldName, new String(values));
					} else if (typeClz == Date.class) {
						String date = new String(values);
						if ("".equals(date)) {
							ReflectionUtility.setFieldValue(object, field.getName(), null);
						} else {
							Date dateValue = null;
							if (date.matches("[0-9]{13}")) {
								dateValue = new Date(Long.valueOf(date));
							}
							ReflectionUtility.setFieldValue(object, field.getName(), dateValue);
						}
					} else if (typeClz == Integer.class || typeClz == int.class) {
						String val = new String(values);
						Integer intValue = null;
						if (!"".equals(val)) {
							intValue = Integer.valueOf(val);
						}
						ReflectionUtility.setFieldValue(object, field.getName(), intValue);
					} else if (typeClz == Double.class || typeClz == double.class) {
						String val = new String(values);
						Double doubleValue = null;
						if (!"".equals(val)) {
							doubleValue = Double.valueOf(val);
						}
						ReflectionUtility.setFieldValue(object, field.getName(), doubleValue);
					} else if (typeClz == Long.class || typeClz == long.class) {
						String val = new String(values);
						Long longValue = null;
						if (!"".equals(val)) {
							longValue = Long.valueOf(val);
						}
						ReflectionUtility.setFieldValue(object, field.getName(), longValue);
					} else if (typeClz == Float.class || typeClz == float.class) {
						String val = new String(values);
						Float floatValue = null;
						if (!"".equals(val)) {
							floatValue = Float.valueOf(val);
						}
						ReflectionUtility.setFieldValue(object, field.getName(), floatValue);
					} else {
						ReflectionUtility.setFieldValue(object, field.getName(), ObjectUtil.toObject(values));
					}
					columnCount++;
				}
			}
		}
		if (columnCount > 0) {
			return object;
		} else {
			return null;
		}
	}
}
