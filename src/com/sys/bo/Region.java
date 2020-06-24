package com.sys.bo;

import com.sys.util.StringUtil;

public class Region extends Base {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj!=null && obj instanceof Region) {
			Region region = (Region)obj;
			return StringUtil.equalString(this.getId(), region.getId());
		}
		return false;
	}
	
	public String toSqlString() {
		String format = "INSERT INTO public.region(id, name, isvalid, createtime, optime)VALUES ('%s', '%s', %d, %d, %d);";
		return String.format(format, this.getId(), this.getName(), this.getIsValid(),this.getOpTime(), this.getCreateTime());
	}
}
