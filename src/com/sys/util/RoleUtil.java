package com.sys.util;

public class RoleUtil {
	
	private final static String[] roleList = {"��ʻԱ", "������", "ǩ����"};
	
	public static String[] getRoleList() {
		return roleList;
	}

	public static String parseRole(short role) {
		return roleList[role];
	}	
}
