package com.vbgps.dao.mybatis.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SqlTable {

	/**
	 * 表名称
	 * @return
	 */
	String value() ;
	
	/**
	 * 别名
	 * @return
	 */
	String alias() default "" ;
}
