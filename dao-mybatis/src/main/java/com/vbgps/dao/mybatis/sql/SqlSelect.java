package com.vbgps.dao.mybatis.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SqlSelect {

	/**
	 * 字段名称
	 * @return
	 */
	String value() ;
	
	/**
	 * 自动构建类型
	 * @return
	 */
	int[] type() default {0} ;
	
	/**
	 * 查询类型
	 * @return
	 */
	SelectType selectType() default SelectType.EQUAL;
}
