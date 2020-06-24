package com.sys.bo;

public class LinkWay extends Base {
	
	private String customerId;
	
	private String phone;
	
	public LinkWay() {
	}
	
	public LinkWay(String customerId, String phone) {
		this.customerId = customerId;
		this.phone = phone;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String toSqlString() {
		String format = "INSERT INTO public.linkway(id, customerid, phone, isvalid, createtime, optime) VALUES ('%s', '%s', '%s', %d, %d, %d);";
		return String.format(format, this.getId(), this.getCustomerId(), this.getPhone(), this.getIsValid(),this.getOpTime(), this.getCreateTime());
	}
}