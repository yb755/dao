package com.vbgps.dao.mybatis;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class DateTypeHandlerCallback extends DateTypeHandler {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
		super.setNonNullParameter(ps, i, parameter, jdbcType);
	}
}
