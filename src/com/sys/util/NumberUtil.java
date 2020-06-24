package com.sys.util;

import com.sys.util.StringUtil;

public class NumberUtil {

	public static int calculate(String express) {
		if (StringUtil.isExpress(express)) {
			String list[] = express.split("[+]");
			int sum = 0;
			for (String element:list) {
				int number = Integer.valueOf(element);
				sum+=number;
			}
			return sum;
		}
		return 0;
	}
}
