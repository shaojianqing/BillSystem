package com.sys.bo;

import com.sys.util.PinyinUtil;

public class Customer extends Base {
	
	private String name;
	
	private String certNo;
	
	private String spell;
	
	public Customer() {
	}
	
	public Customer(String name) {
		this.name = name;
		this.spell = PinyinUtil.getFirstSpell(name);
	}
	
	public Customer(String name, String certNo) {
		this.name = name;
		this.certNo = certNo;
		this.spell = PinyinUtil.getFirstSpell(name);
	}
	
	public Customer(String id, String name, String certNo) {
		this.id = id;
		this.name = name;
		this.certNo = certNo;
		this.spell = PinyinUtil.getFirstSpell(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String toSqlString() {
		String format = "INSERT INTO public.customer(id, name, certNo, spell, isvalid, createtime, optime)VALUES ('%s', '%s', '%s', %d, %d, %d);";
		return String.format(format, this.getId(), this.getName(), this.getCertNo(), this.getSpell(), this.getIsValid(), this.getCreateTime(), this.getOpTime());
	}
}
