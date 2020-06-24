package com.sys.util;

public class RoleUtil {
	
	private final static String[] roleList = {"驾驶员", "受理人", "签字人"};
	
	public static String[] getRoleList() {
		return roleList;
	}

	public static String parseRole(short role) {
		return roleList[role];
	}	
}
