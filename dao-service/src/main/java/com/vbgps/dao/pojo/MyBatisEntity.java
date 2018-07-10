package com.vbgps.dao.pojo;

import java.util.Date;

public class MyBatisEntity {

	private Number id;

	private Date createTime;

	private Number createUserId;

	private Date updateTime;

	private Number updateUserId;

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Number getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Number createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Number getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Number updateUserId) {
		this.updateUserId = updateUserId;
	}

}
