package com.sys.bo;

import java.util.Date;

public class BillDetail {

	private String vehicle;
	
	private String operator;
	
	private String creator;
	
	private Date createDate;
	
	public BillDetail() {
		super();
	}

	public BillDetail(String vehicle, String operator, Date createDate) {
		super();
		this.vehicle = vehicle;
		this.operator = operator;
		this.createDate = createDate;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}	
}
