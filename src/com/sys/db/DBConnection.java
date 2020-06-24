package com.sys.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.sys.bo.Bill;
import com.sys.bo.Customer;
import com.sys.bo.LinkWay;
import com.sys.service.BillService;
import com.sys.service.CustomerService;
import com.sys.service.LinkWayService;
import com.sys.util.StringUtil;

public class DBConnection {

	private static DBConnection connection;
	
	private Connection conn;
	
	private Statement statement;
	
	private DBConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/System"; 
			conn = DriverManager.getConnection(url, "postgres", "mashu0902");
			statement = conn.createStatement();			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static synchronized DBConnection getInstance() {
		if (connection == null) {
			connection = new DBConnection();
		}
		return connection;
	}
	
	public ResultSet executeQuery(String sql) {
		try {
			return statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int executeUpdate(String sql) {
		try {
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void start(String args[]) {
		
		System.out.println("Data Prearation Start ^_^");
		
		List<Bill> billList = BillService.getInstance().getBillByAll();
		if (billList!=null && billList.size()>0) {
			for (Bill bill:billList) {
				
				String senderUnit = StringUtil.trim(bill.getSenderUnit());
				String senderPhone = StringUtil.trim(bill.getSenderPhone());
				
				String receiverUnit = StringUtil.trim(bill.getReceiverUnit());
				String receiverPhone = StringUtil.trim(bill.getReceiverPhone());
				
				if (StringUtil.isNotBlank(senderUnit) && StringUtil.isNotBlank(senderPhone)) {
					Customer customer = CustomerService.getInstance().saveCustomer(senderUnit);
					if (customer != null) {
						LinkWay linkWay = new LinkWay(customer.getId(), senderPhone);
						LinkWayService.getInstance().saveLinkWay(linkWay);
						bill.setSenderUnitId(customer.getId());
						BillService.getInstance().updateBill(bill);
					}
				}
				
				if (StringUtil.isNotBlank(receiverUnit) && StringUtil.isNotBlank(receiverPhone)) {
					Customer customer = CustomerService.getInstance().saveCustomer(receiverUnit);
					if (customer != null) {
						LinkWay linkWay = new LinkWay(customer.getId(), receiverPhone);
						LinkWayService.getInstance().saveLinkWay(linkWay);
						bill.setReceiverUnitId(customer.getId());
						BillService.getInstance().updateBill(bill);
					}
				}
			}			
		}
		
		System.out.println("Data Prearation End ~@~");
	}
}