package com.sys.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.sys.bo.Bill;
import com.sys.bo.Customer;
import com.sys.bo.LinkWay;
import com.sys.bo.Region;
import com.sys.bo.UserInfo;
import com.sys.service.BillService;
import com.sys.service.CustomerService;
import com.sys.service.LinkWayService;
import com.sys.service.RegionService;
import com.sys.service.UserService;

public class DataBaseExporter {

	public static void main(String[] args) throws IOException {
		
		File sqlFile = new File("E:\\database.txt");
		
		FileOutputStream outputStream = new FileOutputStream(sqlFile);	
	
		List<Region> regionList = RegionService.getInstance().getRegionAll();
		if (regionList!=null && regionList.size()>0) {
			for (Region region:regionList) {
				outputStream.write(region.toSqlString().getBytes());
			}
		}
		
		List<Customer> customerList = CustomerService.getInstance().getCustomerAll();
		if (customerList!=null && customerList.size()>0) {
			for (Customer customer:customerList) {
				outputStream.write(customer.toSqlString().getBytes());
			}
		}
		
		List<UserInfo> userInfoList = UserService.getInstance().getUserInfoAll();
		if (userInfoList!=null && userInfoList.size()>0) {
			for (UserInfo userInfo:userInfoList) {
				outputStream.write(userInfo.toSqlString().getBytes());
			}
		}
		
		List<LinkWay> linkWayList = LinkWayService.getInstance().getLinkWayAll();
		if (linkWayList!=null && linkWayList.size()>0) {
			for (LinkWay linkWay:linkWayList) {
				outputStream.write(linkWay.toSqlString().getBytes());
			}
		}
		
		List<Bill> billList = BillService.getInstance().getBillByAll();
		if (billList!=null && billList.size()>0) {
			for (Bill bill:billList) {
				outputStream.write(bill.toSqlString().getBytes());
			}
		}
	}
}
