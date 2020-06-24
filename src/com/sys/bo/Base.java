package com.sys.bo;

import java.util.UUID;

public class Base {
	
	public static final short BASE_FALSE = 0;
	
	public static final short BASE_TRUE = 1;
	
	protected String id;
	
	protected short isValid;
	
	protected long createTime;
	
	protected long opTime;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public short getIsValid() {
		return isValid;
	}

	public void setIsValid(short isValid) {
		this.isValid = isValid;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getOpTime() {
		return opTime;
	}

	public void setOpTime(long opTime) {
		this.opTime = opTime;
	}
	
	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
