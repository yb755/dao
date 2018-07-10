package com.vbgps.dao.mybatis.sql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseSqlBuild<T,ID> {

	private static final Logger logger = LoggerFactory.getLogger(BaseSqlBuild.class);
	
	public String insert(T t){
		String insertSql = SqlBuild.buildInsertSql(this.getEntityClass());
		logger.debug("insert build sql:{}",insertSql);
		return insertSql ;
	}
	
	@SuppressWarnings({"unchecked" })
	public String batchInsert(Map<String,Object> params){
		List<T> values = (List<T>) params.get("listValues");
		return SqlBuild.buildBatchInsertSql(values,this.getEntityClass(), 0, "listValues") ;
	}
	
	@SuppressWarnings({"unchecked" })
	public String batchInsertAndType(Map<String,Object> params){
		List<T> values = (List<T>) params.get("listValues");
		Integer type = (Integer)params.get("type");
		return SqlBuild.buildBatchInsertSql(values,this.getEntityClass(), type, "listValues") ;
	}
	
	public String findById(ID id){
		StringBuilder builder = new StringBuilder();
		String selectSql = buildDefaultColumnSelect();
		builder.append(selectSql);
		builder.append(this.findIdCondition());
		logger.debug("findById build sql:{}",builder.toString());
		return builder.toString();
	}
	
	public String update(T t){
		StringBuilder builder = new StringBuilder();
		builder.append("update");
		builder.append(" ");
		builder.append(this.findTable());
		builder.append(" set");
		builder.append(" ");
		builder.append(SqlBuild.buildUpdateColums(t));
		builder.append(this.findIdCondition());
		logger.debug("update build sql:{}",builder.toString());
		return builder.toString();
	}
	
	public String findByObject(Object obj){
		StringBuilder builder = new StringBuilder();
		String selectSql = buildDefaultColumnSelect();
		builder.append(selectSql);
		builder.append(" where 1=1");
		builder.append(SqlBuild.buildSelectCondition(obj, 0,""));
		logger.debug("findByObject build sql:{}",builder.toString());
		return builder.toString();
	}
	
	public String findByObjectAndType(Map<String,Object> params){
		Object obj = params.get("obj");
		Integer type = (Integer)params.get("type");
		StringBuilder builder = new StringBuilder();
		String selectSql = buildColumnSelect(type);
		builder.append((selectSql==null||"".equals(selectSql))?buildDefaultColumnSelect():selectSql);
		builder.append(" where 1=1");
		builder.append(SqlBuild.buildSelectCondition(obj, type,"obj"));
		logger.debug("findByObjectAndType build sql:{}",builder.toString());
		return builder.toString();
	}
	
	protected Class<?> getEntityClass()
	{
		// 泛型转换
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<?> entity = (Class<?>) pt.getActualTypeArguments()[0];
		return entity ;
	}
	
	protected Class<?> getIdClass()
	{
		// 泛型转换
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<?> id = (Class<?>) pt.getActualTypeArguments()[1];
		return id ;
	}
	
	protected String buildDefaultColumnSelect(){
		return buildColumnSelect(0);
	}
	
	protected String buildColumnSelect(int type){
		Class<?> clazz = getEntityClass() ;
		StringBuilder builder = new StringBuilder(SqlBuild.SELECT_START);
		String colums = SqlBuild.buildSelectColumns(clazz,type);
		builder.append(colums);
		builder.append(" from");
		builder.append(" ");
		builder.append(SqlBuild.existSqlTable(clazz).value());
		return builder.toString();
	}
	
	protected String findTable(){
		Class<?> clazz = getEntityClass() ;
		return SqlBuild.existSqlTable(clazz).value();
	}
	
	protected String findIdCondition(){
		StringBuilder builder = new StringBuilder(" where");
		Class<?> clazz = getEntityClass() ;
		Field field =  SqlBuild.primaryField(clazz);
		builder.append(" ");
		builder.append(SqlBuild.existSqlField(field).value());
		builder.append(" = ");
		builder.append("#{");
		builder.append(field.getName());
		builder.append("}");
		return builder.toString() ;
	}
}
