package com.sys.vo;

public class CustomerLink {
	
	private String customerId;
	
	private String customerName;
	
	private String customerCertNo;
	
	private String customerPhone;
	
	public CustomerLink() {
		super();
	}
	
	public CustomerLink(String customerId, String customerName, String customerCertNo, String customerPhone) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerCertNo = customerCertNo;
		this.customerPhone = customerPhone;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCertNo() {
		return customerCertNo;
	}

	public void setCustomerCertNo(String customerCertNo) {
		this.customerCertNo = customerCertNo;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
}