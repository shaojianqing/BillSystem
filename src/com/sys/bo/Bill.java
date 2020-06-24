package com.sys.bo;

import java.util.Date;

import com.sys.util.DateUtil;
import com.sys.util.NumberUtil;
import com.sys.util.StringUtil;

public class Bill extends Base {
	
	private String code;
	
	private String name;
	
	private String pack;
	
	private String numStr;
	
	private int number;
	
	private Date sendDate;
	
	private int priceFee;
	
	private int bargeFee;
	
	private int totalFee;
	
	private int deliverFee;
	
	private String senderUnitId;
	
	private String senderUnit;
	
	private String senderPhone;
	
	private String senderCertNo;
	
	private String receiverUnitId;
	
	private String receiverUnit;
	
	private String receiverPhone;
	
	private String source;
	
	private String destination;
	
	private String destinationId;
	
	private short isSelfGet;
	
	private short isSendProd;
	
	private short isGetPay;
	
	private short isCashPay;
	
	private short isBackPay;
	
	private short isBackBill;
	
	private int backBillNum;
	
	private String signer;
	
	private String signerId;
	
	private String memo;
	
	public void initData() {
		if (StringUtil.isNotBlank(this.numStr)) {
			int number = NumberUtil.calculate(numStr);
			this.number = number;
		}
	}
	
	public void initNumber() {
		if (StringUtil.isBlank(this.numStr)) {
			this.numStr = String.valueOf(this.number);
		}
	}	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public String getNumStr() {
		return numStr;
	}

	public void setNumStr(String numStr) {
		this.numStr = numStr;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSenderCertNo() {
		return senderCertNo;
	}

	public void setSenderCertNo(String senderCertNo) {
		this.senderCertNo = senderCertNo;
	}

	public int getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(int priceFee) {
		this.priceFee = priceFee;
	}

	public int getBargeFee() {
		return bargeFee;
	}

	public void setBargeFee(int bargeFee) {
		this.bargeFee = bargeFee;
	}

	public int getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}

	public int getDeliverFee() {
		return deliverFee;
	}

	public void setDeliverFee(int deliverFee) {
		this.deliverFee = deliverFee;
	}

	public String getSenderUnitId() {
		return senderUnitId;
	}

	public void setSenderUnitId(String senderUnitId) {
		this.senderUnitId = senderUnitId;
	}

	public String getSenderUnit() {
		return senderUnit;
	}

	public void setSenderUnit(String senderUnit) {
		this.senderUnit = senderUnit;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getReceiverUnitId() {
		return receiverUnitId;
	}

	public void setReceiverUnitId(String receiverUnitId) {
		this.receiverUnitId = receiverUnitId;
	}

	public String getReceiverUnit() {
		return receiverUnit;
	}

	public void setReceiverUnit(String receiverUnit) {
		this.receiverUnit = receiverUnit;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public short getIsSelfGet() {
		return isSelfGet;
	}

	public void setIsSelfGet(short isSelfGet) {
		this.isSelfGet = isSelfGet;
	}

	public short getIsSendProd() {
		return isSendProd;
	}

	public void setIsSendProd(short isSendProd) {
		this.isSendProd = isSendProd;
	}

	public short getIsGetPay() {
		return isGetPay;
	}

	public void setIsGetPay(short isGetPay) {
		this.isGetPay = isGetPay;
	}

	public short getIsCashPay() {
		return isCashPay;
	}

	public void setIsCashPay(short isCashPay) {
		this.isCashPay = isCashPay;
	}

	public short getIsBackPay() {
		return isBackPay;
	}

	public void setIsBackPay(short isBackPay) {
		this.isBackPay = isBackPay;
	}

	public short getIsBackBill() {
		return isBackBill;
	}

	public void setIsBackBill(short isBackBill) {
		this.isBackBill = isBackBill;
	}

	public int getBackBillNum() {
		return backBillNum;
	}

	public void setBackBillNum(int backBillNum) {
		this.backBillNum = backBillNum;
	}

	public String getSigner() {
		return signer;
	}

	public void setSigner(String signer) {
		this.signer = signer;
	}

	public String getSignerId() {
		return signerId;
	}

	public void setSignerId(String signerId) {
		this.signerId = signerId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj!=null && obj instanceof Bill) {
			Bill bill = (Bill)obj;
			return StringUtil.equalString(this.getId(), bill.getId());
		}
		return false;
	}

	public String toSqlString() {
		
		String sql = String.format("insert into bill (" +
				"id, " +
				"code, " +
				"name, " +
				"pack, " +
				"numStr, " +
				"number, " +					
				"sendDate, " +
				"priceFee, " +
				"bargeFee, " +
				"totalFee, " +
				"deliverFee, " +
				"senderUnitId, " +
				"senderUnit, " +
				"senderPhone, " +
				"senderCertNo, " +
				"receiverUnitId, " +
				"receiverUnit, " +
				"receiverPhone, " +
				"source, " +
				"destination, " +
				"destinationId, " +
				"isSelfGet, " +
				"isSendProd, " +
				"isGetPay, " +
				"isCashPay, " +
				"isBackPay, " +
				"isBackBill, " +
				"backBillNum, " +
				"signer, " +
				"signerId, " +
				"isvalid, " +
				"createTime, " +
				"opTime) values ('%s', '%s', '%s', '%s', '%s', '%d', '%s', '%d', '%d', '%d', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', " +
				"'%d', '%d', '%d', '%d', '%d', '%d', '%d', '"+this.getSigner()+"', '"+this.getSignerId()+"',"+this.getIsValid()+", '"+this.getCreateTime()+"', '"+this.getOpTime()+"');",
				this.getId(), 
				this.getCode(), 
				this.getName(),
				this.getPack(),
				this.getNumStr(),
				this.getNumber(),
				DateUtil.formatDate(this.getSendDate()),
				this.getPriceFee(),
				this.getBargeFee(),
				this.getTotalFee(),
				this.getDeliverFee(),
				this.getSenderUnitId(),
				this.getSenderUnit(),
				this.getSenderPhone(),
				this.getSenderCertNo(),
				this.getReceiverUnitId(),
				this.getReceiverUnit(),
				this.getReceiverPhone(),
				this.getSource(),
				this.getDestination(),
				this.getDestinationId(),
				this.getIsSelfGet(),
				this.getIsSendProd(),
				this.getIsGetPay(),
				this.getIsCashPay(),
				this.getIsBackPay(),
				this.getIsBackBill(),
				this.getBackBillNum());
		
		return sql;
	}
}
