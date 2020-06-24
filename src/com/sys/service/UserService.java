package com.sys.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sys.bo.Base;
import com.sys.bo.UserInfo;
import com.sys.db.DBConnection;
import com.sys.util.StringUtil;

public class UserService {
	
	private static UserService userService;
	
	private DBConnection connection;
	
	private UserService() {
		connection = DBConnection.getInstance();
	}
	
	public static synchronized UserService getInstance() {
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}
	
	public List<UserInfo> getUserInfoByQuery() {
		try {		
			String sql = "select * from UserInfo where isvalid = 1 order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareUserInfoList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<UserInfo>();
	}
	
	public List<UserInfo> getUserInfoAll() {
		try {		
			String sql = "select * from UserInfo order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareUserInfoList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<UserInfo>();
	}
	
	public String[] getUserNameList(List<UserInfo> userInfoList) {
		if (userInfoList != null && userInfoList.size()>0) {
			String[] userNameList = new String[userInfoList.size()+1];
			userNameList[0] = "«Î—°‘Ò";
			for (int i=0;i<userInfoList.size();++i) {
				userNameList[i+1] = userInfoList.get(i).getName();
			}
			return userNameList;
		}
		return new String[]{"«Î—°‘Ò"};
	}
	
	public String[] getUserIdList(List<UserInfo> userInfoList) {
		if (userInfoList != null && userInfoList.size()>0) {
			String[] userIdList = new String[userInfoList.size()+1];
			userIdList[0] = "0";
			for (int i=0;i<userInfoList.size();++i) {
				userIdList[i+1] = userInfoList.get(i).getId();
			}
			return userIdList;
		}
		return new String[]{"0"};
	}
	
	public int getUserIndex(List<UserInfo> userInfoList, String userId) {
		if (userInfoList != null && userInfoList.size()>0) {
			for (int i=0;i<userInfoList.size();++i) {
				UserInfo userInfo = userInfoList.get(i);
				if (StringUtil.isNotBlank(userId) && userId.equals(userInfo.getId())) {
					return i+1;
				}
			}
		}
		return 0;
	}
	
	public void saveUserInfo(UserInfo userInfo) {
		userInfo.setId(Base.generateUUID());
		String sql = String.format("insert into userInfo (" +
				"id, " +
				"name, " +
				"role, " +
				"link, " +
				"isvalid, " +
				"createTime, " +
				"opTime) values ('%s', '%s', '%d', '%s', 1, '%d', '%d')",
				userInfo.getId(), 
				userInfo.getName(),
				userInfo.getRole(),
				userInfo.getLink(),
				System.currentTimeMillis(),
				System.currentTimeMillis());
		connection.executeUpdate(sql);
	}
	
	public void updateUserInfo(UserInfo userInfo) {
		if (userInfo != null && StringUtil.isNotBlank(userInfo.getId())) {
			String sql = String.format("update userInfo set name = '%s' " +
					", role = '%d' , link = '%s' , opTime = '%d'"+
					" where id = '%s'", 
					userInfo.getName(),
					userInfo.getRole(),
					userInfo.getLink(),
					System.currentTimeMillis(),
					userInfo.getId());
			connection.executeUpdate(sql);
		}
	}
	
	public void removeUserInfo(UserInfo userInfo) {
		if (userInfo != null && StringUtil.isNotBlank(userInfo.getId())) {
			String sql = String.format("update userInfo set isValid = '0' where id = '%s'", userInfo.getId());
			connection.executeUpdate(sql);
		}
	}
	
	private List<UserInfo> prepareUserInfoList(ResultSet resultSet) {
		if (resultSet!=null) {
			try{
				List<UserInfo> userInfoList = new ArrayList<UserInfo>();
				while(resultSet.next()) {
					UserInfo userInfo = new UserInfo();
					userInfo.setId(resultSet.getString("id"));
					userInfo.setName(resultSet.getString("name"));
					userInfo.setRole(resultSet.getShort("role"));
					userInfo.setLink(resultSet.getString("link"));
					userInfo.setIsValid(resultSet.getShort("isValid"));
					userInfo.setCreateTime(resultSet.getLong("createTime"));
					userInfo.setOpTime(resultSet.getLong("opTime"));
					userInfoList.add(userInfo);
				}
				return userInfoList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<UserInfo>();
	}
}
