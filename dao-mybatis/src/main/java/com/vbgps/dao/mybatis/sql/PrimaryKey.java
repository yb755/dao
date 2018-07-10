package com.vbgps.dao.mybatis.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {

	/**
	 * 插入是否跳过主键
	 * @return
	 */
	boolean insertIsSkip() default true ;
	
	/**
	 * 更新是否跳过
	 * @return
	 */
	boolean updateIsSkip() default true ;
}
