package com.sys.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sys.bo.Base;
import com.sys.bo.Bill;
import com.sys.db.DBConnection;
import com.sys.util.DateUtil;
import com.sys.util.StringUtil;

public class BillService {

	private static BillService billService;
	
	private DBConnection connection;
	
	private BillService() {
		connection = DBConnection.getInstance();
	}
	
	public static synchronized BillService getInstance() {
		if (billService == null) {
			billService = new BillService();
		}
		return billService;
	}
	
	public List<Bill> getBillByQuery() {
		try {		
			String sql = "select * from Bill where isvalid = 1 order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareBillList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Bill>();
	}
	
	public List<Bill> getBillByAll() {
		try {		
			String sql = "select * from Bill order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareBillList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Bill>();
	}
	
	public int getBillTotalCount() {
		try {		
			String sql = "select count(1) from Bill where isvalid = 1";
			ResultSet resultSet = connection.executeQuery(sql);
			if (resultSet!=null && resultSet.next()) {
				return resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Bill> queryBillByCondition(String keyword, String senderUnitId, Date startDate, Date endDate) {
		try {
			String sql = "select * from Bill where isvalid = 1 ";
			if (StringUtil.isNotBlank(keyword)) {
				sql = sql+"and (name like '%"+keyword+"%' " +
						  "or senderUnit like '%"+keyword+"%' "+
						  "or senderPhone like '%"+keyword+"%' "+
						  "or receiverUnit like '%"+keyword+"%' "+
						  "or receiverPhone like '%"+keyword+"%' "+
						  "or source like '%"+keyword+"%' "+
						  "or destination like '%"+keyword+"%') ";
			}
			if (StringUtil.isNotBlank(senderUnitId) && (!"0".equals(senderUnitId))) {
				sql = sql+"and senderUnitId='"+senderUnitId+"' ";
			}
			if (startDate!=null) {
				sql = sql+"and sendDate>='"+DateUtil.formatDate(startDate)+"' ";
			}
			if (endDate!=null) {
				sql = sql+"and sendDate<='"+DateUtil.formatDate(endDate)+"' ";
			}
			sql = sql+"order by destination asc, sendDate desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareBillList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Bill>();
	}
	
	public List<Bill> queryBillBySender(String senderId, Date startDate, Date endDate) {
		try {
			String sql = "select * from Bill where isvalid = 1 ";
			if (StringUtil.isNotBlank(senderId)) {
				sql = sql+"and senderUnitId = '"+senderId+"' ";
			}
			if (startDate!=null) {
				sql = sql+"and sendDate>='"+DateUtil.formatDate(startDate)+"' ";
			}
			if (endDate!=null) {
				sql = sql+"and sendDate<='"+DateUtil.formatDate(endDate)+"' ";
			}
			sql = sql+"order by destinationId, createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareBillList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Bill>();
	}
	
	public String getCurrentCode() {
		try {		
			String sql = "select count(1) from Bill";
			ResultSet resultSet = connection.executeQuery(sql);
			if (resultSet.next()) {
				int code = resultSet.getInt(1);
				return String.format("%08d", (code+1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "00000001";
	}
	
	public void saveBill(Bill bill) {
		if (bill!=null) {
			bill.setId(Base.generateUUID());
			long currentTime = System.currentTimeMillis();
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
					"opTime) values ('%s', '%s', '%s', '%s', '%s', '%d', '%s', '%d', '%d', '%d', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', " +
					"'%d', '%d', '%d', '%d', '%d', '%d', '%d', '"+bill.getSigner()+"', '"+bill.getSignerId()+"', 1, '"+currentTime+"', '"+currentTime+"')",
					bill.getId(), 
					bill.getCode(), 
					bill.getName(),
					bill.getPack(),
					bill.getNumStr(),
					bill.getNumber(),
					DateUtil.formatDate(bill.getSendDate()),
					bill.getPriceFee(),
					bill.getBargeFee(),
					bill.getTotalFee(),
					bill.getDeliverFee(),
					bill.getSenderUnitId(),
					bill.getSenderUnit(),
					bill.getSenderPhone(),
					bill.getSenderCertNo(),
					bill.getReceiverUnitId(),
					bill.getReceiverUnit(),
					bill.getReceiverPhone(),
					bill.getSource(),
					bill.getDestination(),
					bill.getDestinationId(),
					bill.getIsSelfGet(),
					bill.getIsSendProd(),
					bill.getIsGetPay(),
					bill.getIsCashPay(),
					bill.getIsBackPay(),
					bill.getIsBackBill(),
					bill.getBackBillNum());
			connection.executeUpdate(sql);
		}
	}
	
	public void attachBill(Bill bill, long createTime) {
		if (bill!=null) {
			bill.setId(Base.generateUUID());
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
					"opTime) values ('%s', '%s', '%s', '%s', '%s', '%d', '%s', '%d', '%d', '%d', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', " +
					"'%d', '%d', '%d', '%d', '%d', '%d', '%d', '"+bill.getSigner()+"', '"+bill.getSignerId()+"', 1, '"+createTime+"', '"+createTime+"')",
					bill.getId(), 
					bill.getCode(), 
					bill.getName(),
					bill.getPack(),
					bill.getNumStr(),
					bill.getNumber(),
					DateUtil.formatDate(bill.getSendDate()),
					bill.getPriceFee(),
					bill.getBargeFee(),
					bill.getTotalFee(),
					bill.getDeliverFee(),
					bill.getSenderUnitId(),
					bill.getSenderUnit(),
					bill.getSenderPhone(),
					bill.getSenderCertNo(),
					bill.getReceiverUnitId(),
					bill.getReceiverUnit(),
					bill.getReceiverPhone(),
					bill.getSource(),
					bill.getDestination(),
					bill.getDestinationId(),
					bill.getIsSelfGet(),
					bill.getIsSendProd(),
					bill.getIsGetPay(),
					bill.getIsCashPay(),
					bill.getIsBackPay(),
					bill.getIsBackBill(),
					bill.getBackBillNum());
			connection.executeUpdate(sql);
		}
	}
	
	public void updateBill(Bill bill) {
		if (bill!=null) {
			long currentTime = System.currentTimeMillis();
			String sql = String.format("update bill set code = '%s'," +
					"name = '%s'," +
					"pack = '%s'," +
					"numStr = '%s'," +
					"number = '%d'," +
					"sendDate = '%s'," +
					"priceFee = '%d'," +
					"bargeFee = '%d'," +
					"totalFee = '%d'," +
					"deliverFee = '%d'," +
					"senderUnitId = '%s'," +
					"senderUnit = '%s'," +
					"senderPhone = '%s'," +
					"senderCertNo = '%s'," +
					"receiverUnitId = '%s'," +
					"receiverUnit = '%s'," +
					"receiverPhone = '%s'," +
					"source = '%s'," +
					"destination = '%s'," +
					"destinationId = '%s'," +
					"isSelfGet = '%d'," +
					"isSendProd = '%d'," +
					"isGetPay = '%d'," +
					"isCashPay = '%d'," +
					"isBackPay = '%d'," +
					"isBackBill = '%d'," +
					"backBillNum = '%d'," +
					"signer = '%s'," +
					"signerId = '%s'," +
					"opTime = '"+currentTime+"' where id = '%s'",
					bill.getCode(), 
					bill.getName(),
					bill.getPack(),
					bill.getNumStr(),
					bill.getNumber(),
					DateUtil.formatDate(bill.getSendDate()),
					bill.getPriceFee(),
					bill.getBargeFee(),
					bill.getTotalFee(),
					bill.getDeliverFee(),
					bill.getSenderUnitId(),
					bill.getSenderUnit(),
					bill.getSenderPhone(),
					bill.getSenderCertNo(),
					bill.getReceiverUnitId(),
					bill.getReceiverUnit(),
					bill.getReceiverPhone(),
					bill.getSource(),
					bill.getDestination(),
					bill.getDestinationId(),
					bill.getIsSelfGet(),
					bill.getIsSendProd(),
					bill.getIsGetPay(),
					bill.getIsCashPay(),
					bill.getIsBackPay(),
					bill.getIsBackBill(),
					bill.getBackBillNum(),
					bill.getSigner(),
					bill.getSignerId(),
					bill.getId());
			connection.executeUpdate(sql);
		}
	}
	
	public void updateBillMemo(Bill bill) {
		if (bill!=null) {
			long currentTime = System.currentTimeMillis();
			String sql = String.format("update bill set code = '%s'," +
					"name = '%s'," +
					"pack = '%s'," +
					"numStr = '%s'," +
					"number = '%d'," +
					"sendDate = '%s'," +
					"priceFee = '%d'," +
					"bargeFee = '%d'," +
					"totalFee = '%d'," +
					"deliverFee = '%d'," +
					"senderUnitId = '%s'," +
					"senderUnit = '%s'," +
					"senderPhone = '%s'," +
					"senderCertNo = '%s'," +
					"receiverUnitId = '%s'," +
					"receiverUnit = '%s'," +
					"receiverPhone = '%s'," +
					"source = '%s'," +
					"destination = '%s'," +
					"destinationId = '%s'," +
					"isSelfGet = '%d'," +
					"isSendProd = '%d'," +
					"isGetPay = '%d'," +
					"isCashPay = '%d'," +
					"isBackPay = '%d'," +
					"isBackBill = '%d'," +
					"backBillNum = '%d'," +
					"signer = '%s'," +
					"signerId = '%s'," +
					"memo = '%s'," +
					"opTime = '"+currentTime+"' where id = '%s'",
					bill.getCode(), 
					bill.getName(),
					bill.getPack(),
					bill.getNumStr(),
					bill.getNumber(),
					DateUtil.formatDate(bill.getSendDate()),
					bill.getPriceFee(),
					bill.getBargeFee(),
					bill.getTotalFee(),
					bill.getDeliverFee(),
					bill.getSenderUnitId(),
					bill.getSenderUnit(),
					bill.getSenderPhone(),
					bill.getSenderCertNo(),
					bill.getReceiverUnitId(),
					bill.getReceiverUnit(),
					bill.getReceiverPhone(),
					bill.getSource(),
					bill.getDestination(),
					bill.getDestinationId(),
					bill.getIsSelfGet(),
					bill.getIsSendProd(),
					bill.getIsGetPay(),
					bill.getIsCashPay(),
					bill.getIsBackPay(),
					bill.getIsBackBill(),
					bill.getBackBillNum(),
					bill.getSigner(),
					bill.getSignerId(),
					bill.getMemo(),
					bill.getId());
			connection.executeUpdate(sql);
		}
	}
	
	public void removeBill(Bill bill) {
		if (bill!=null) {
			String sql = String.format("update bill set isvalid = '0' where id = '%s'", bill.getId());
			connection.executeUpdate(sql);
		}
	}
	
	private List<Bill> prepareBillList(ResultSet resultSet) {
		if (resultSet!=null) {
			try{
				List<Bill> billList = new ArrayList<Bill>();
				while(resultSet.next()) {
					Bill bill = new Bill();
					bill.setId(resultSet.getString("id"));
					bill.setCode(resultSet.getString("code"));
					bill.setName(resultSet.getString("name"));
					bill.setPack(resultSet.getString("pack"));
					bill.setNumStr(resultSet.getString("numStr"));
					bill.setNumber(resultSet.getInt("number"));
					bill.setSendDate(resultSet.getDate("sendDate"));
					bill.setPriceFee(resultSet.getInt("priceFee"));
					bill.setBargeFee(resultSet.getInt("bargeFee"));
					bill.setTotalFee(resultSet.getInt("totalFee"));
					bill.setDeliverFee(resultSet.getInt("deliverFee"));
					bill.setSenderUnitId(resultSet.getString("senderUnitId"));
					bill.setSenderUnit(resultSet.getString("senderUnit"));
					bill.setSenderPhone(resultSet.getString("senderPhone"));
					bill.setSenderCertNo(resultSet.getString("senderCertNo"));
					bill.setReceiverUnitId(resultSet.getString("receiverUnitId"));
					bill.setReceiverUnit(resultSet.getString("receiverUnit"));
					bill.setReceiverPhone(resultSet.getString("receiverPhone"));
					bill.setSource(resultSet.getString("source"));
					bill.setDestination(resultSet.getString("destination"));
					bill.setDestinationId(resultSet.getString("destinationId"));
					bill.setIsSelfGet(resultSet.getShort("isSelfGet"));
					bill.setIsSendProd(resultSet.getShort("isSendProd"));
					bill.setIsGetPay(resultSet.getShort("isGetPay"));
					bill.setIsCashPay(resultSet.getShort("isCashPay"));
					bill.setIsBackPay(resultSet.getShort("isBackPay"));
					bill.setIsBackBill(resultSet.getShort("isBackBill"));
					bill.setBackBillNum(resultSet.getInt("backBillNum"));
					bill.setSigner(resultSet.getString("signer"));
					bill.setSignerId(resultSet.getString("signerId"));
					bill.setMemo(resultSet.getString("memo"));
					bill.setIsValid(resultSet.getShort("isValid"));
					bill.setCreateTime(resultSet.getLong("createTime"));
					bill.setOpTime(resultSet.getLong("opTime"));
					bill.initNumber();				
					billList.add(bill);
				}
				return billList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<Bill>();
	}
}
