package com.sys.bo;

public class UserInfo extends Base {
	
	private String name;
	
	private short role;
	
	private String link;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getRole() {
		return role;
	}

	public void setRole(short role) {
		this.role = role;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String toSqlString() {
		String format = "INSERT INTO public.userinfo(id, name, role, link, isvalid, createtime, optime)VALUES ('%s', '%s', %d, '%s', %d, %d, %d);";
		return String.format(format, this.getId(), this.getName(), this.getRole(), this.getLink(), this.getIsValid(), this.getCreateTime(), this.getOpTime());
	}
}
