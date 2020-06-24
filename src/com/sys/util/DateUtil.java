package com.sys.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String formatChineseDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍMM‘¬dd»’");
		return sdf.format(date);
	}
	
	public static String formatSpecialDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy   MM   dd");
		return sdf.format(date);
	}
}
