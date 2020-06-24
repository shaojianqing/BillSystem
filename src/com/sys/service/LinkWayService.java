package com.sys.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sys.bo.Base;
import com.sys.bo.LinkWay;
import com.sys.db.DBConnection;
import com.sys.util.StringUtil;

public class LinkWayService {
	
	private static LinkWayService linkWayService;
	
	private DBConnection connection;
	
	private LinkWayService() {
		connection = DBConnection.getInstance();
	}
	
	public static synchronized LinkWayService getInstance() {
		if (linkWayService == null) {
			linkWayService = new LinkWayService();
		}
		return linkWayService;
	}
	
	public List<LinkWay> getLinkWayByCustomerId(String customerId) {
		if (StringUtil.isNotBlank(customerId)) {
			String sql = "select * from LinkWay where isvalid = 1 and customerId = '"+customerId+"'";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareLinkWayList(resultSet);
		} else {
			return new ArrayList<LinkWay>();
		}
	}
	
	public List<LinkWay> getLinkWayAll() {
		String sql = "select * from LinkWay";
		ResultSet resultSet = connection.executeQuery(sql);
		return prepareLinkWayList(resultSet);
	}
	
	public void saveLinkWay(LinkWay linkWay) {
		if (linkWay!=null) {		
			String sql = "select * from LinkWay where isvalid = 1 and customerId = '"+linkWay.getCustomerId()+"' and " +
						 "phone = '"+ linkWay.getPhone() +"'";
			ResultSet resultSet = connection.executeQuery(sql);
			List<LinkWay> list = prepareLinkWayList(resultSet);
			if (list==null || list.size()==0) {			
				linkWay.setId(Base.generateUUID());
				sql = String.format("insert into linkWay (" +
						"id, " +
						"customerId, " +
						"phone, " +
						"isvalid, " +
						"createTime, " +
						"opTime) values ('%s', '%s', '%s', 1, '%d', '%d')",
						linkWay.getId(), 
						linkWay.getCustomerId(),
						linkWay.getPhone(),
						System.currentTimeMillis(),
						System.currentTimeMillis());
				connection.executeUpdate(sql);
			}
		}
	}
	
	public void updateLinkWay(LinkWay linkWay) {
		if (linkWay != null && StringUtil.isNotBlank(linkWay.getId())) {
			String sql = String.format("update linkWay set phone = '%s' " +
					", opTime = '%d'"+
					" where id = '%s'", 
					linkWay.getPhone(),
					System.currentTimeMillis(),
					linkWay.getId());
			connection.executeUpdate(sql);
		}
	}
	
	public void removeLinkWay(LinkWay linkWay) {
		if (linkWay != null && StringUtil.isNotBlank(linkWay.getId())) {
			String sql = String.format("update linkWay set isValid = '0' where id = '%s'", linkWay.getId());
			connection.executeUpdate(sql);
		}
	}
	
	private List<LinkWay> prepareLinkWayList(ResultSet resultSet) {
		if (resultSet!=null) {
			try{
				List<LinkWay> linkWayList = new ArrayList<LinkWay>();
				while(resultSet.next()) {
					LinkWay linkWay = new LinkWay();
					linkWay.setId(resultSet.getString("id"));
					linkWay.setCustomerId(resultSet.getString("customerId"));
					linkWay.setPhone(resultSet.getString("phone"));
					linkWay.setIsValid(resultSet.getShort("isValid"));
					linkWay.setCreateTime(resultSet.getLong("createTime"));
					linkWay.setOpTime(resultSet.getLong("opTime"));
					linkWayList.add(linkWay);
				}
				return linkWayList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<LinkWay>();
	}
}
