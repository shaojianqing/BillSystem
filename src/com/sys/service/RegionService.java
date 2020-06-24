package com.sys.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sys.bo.Base;
import com.sys.bo.Region;
import com.sys.db.DBConnection;
import com.sys.util.StringUtil;

public class RegionService {
	
	private static RegionService regionService;
	
	private Map<String, Region> regionMap = new HashMap<String, Region>();
	
	private DBConnection connection;
	
	private RegionService() {
		connection = DBConnection.getInstance();
	}
	
	public static synchronized RegionService getInstance() {
		if (regionService == null) {
			regionService = new RegionService();
		}
		return regionService;
	}
	
	public List<Region> getRegionByQuery() {
		try {		
			String sql = "select * from Region where isvalid = 1 order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareRegionList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Region>();
	}
	
	public List<Region> getRegionAll() {
		try {		
			String sql = "select * from Region order by createTime desc";
			ResultSet resultSet = connection.executeQuery(sql);
			return prepareRegionList(resultSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Region>();
	}
	
	public Region getRegionById(String id)
	{
		try {
			if (StringUtil.isNotBlank(id)) {
				if (regionMap.containsKey(id)==false) {			
					String sql = "select * from Region where isvalid = 1 and id = '"+id+"' limit 1";
					ResultSet resultSet = connection.executeQuery(sql);
					if (resultSet.next()){
						Region region = new Region();
						region.setId(resultSet.getString("id"));
						region.setName(resultSet.getString("name"));
						region.setIsValid(resultSet.getShort("isValid"));
						region.setCreateTime(resultSet.getLong("createTime"));
						region.setOpTime(resultSet.getLong("opTime"));
						regionMap.put(region.getId(), region);
						return region;
					}
				} else {
					return regionMap.get(id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String[] getRegionNameList(List<Region> regionList) {
		if (regionList != null && regionList.size()>0) {
			String[] regionNameList = new String[regionList.size()+1];
			regionNameList[0] = "«Î—°‘Ò";
			for (int i=0;i<regionList.size();++i) {
				regionNameList[i+1] = regionList.get(i).getName();
			}
			return regionNameList;
		}
		return new String[]{"«Î—°‘Ò"};
	}
	
	public String[] getRegionIdList(List<Region> regionList) {
		if (regionList != null && regionList.size()>0) {
			String[] regionIdList = new String[regionList.size()+1];
			regionIdList[0] = "0";
			for (int i=0;i<regionList.size();++i) {
				regionIdList[i+1] = regionList.get(i).getId();
			}
			return regionIdList;
		}
		return new String[]{"0"};
	}
	
	public int getRegionIndex(List<Region> regionList, String regionId) {
		if (regionList != null && regionList.size()>0) {
			for (int i=0;i<regionList.size();++i) {
				Region region = regionList.get(i);
				if (StringUtil.isNotBlank(regionId) && regionId.equals(region.getId())) {
					return i+1;
				}
			}
		}
		return 0;
	}
	
	public void saveRegion(Region region) {
		region.setId(Base.generateUUID());
		String sql = String.format("insert into region (" +
				"id, " +
				"name, " +
				"isvalid, " +
				"createTime, " +
				"opTime) values ('%s', '%s', 1, '%d', '%d')",
				region.getId(), 
				region.getName(),
				System.currentTimeMillis(),
				System.currentTimeMillis());
		if (connection.executeUpdate(sql)!=0) {
			regionMap.put(region.getId(), region);
		}
	}
	
	public void updateRegion(Region region) {
		if (region != null && StringUtil.isNotBlank(region.getId())) {
			String sql = String.format("update region set name = '%s' " +
					", opTime = '%d'"+
					" where id = '%s'", 
					region.getName(),
					System.currentTimeMillis(),
					region.getId());
			if (connection.executeUpdate(sql)!=0) {
				regionMap.put(region.getId(), region);
			}
		}
	}
	
	public void removeRegion(Region region) {
		if (region != null && StringUtil.isNotBlank(region.getId())) {
			String sql = String.format("update region set isValid = '0' where id = '%s'", region.getId());
			if(connection.executeUpdate(sql)!=0) {
				regionMap.remove(region.getId());
			}
		}
	}
	
	private List<Region> prepareRegionList(ResultSet resultSet) {
		if (resultSet!=null) {
			try{
				List<Region> regionList = new ArrayList<Region>();
				while(resultSet.next()) {
					Region region = new Region();
					region.setId(resultSet.getString("id"));
					region.setName(resultSet.getString("name"));
					region.setIsValid(resultSet.getShort("isValid"));
					region.setCreateTime(resultSet.getLong("createTime"));
					region.setOpTime(resultSet.getLong("opTime"));
					regionList.add(region);
				}
				return regionList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<Region>();
	}
}
