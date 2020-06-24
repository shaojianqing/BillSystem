package com.sys.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	private static final char CN_NUM[] = {'Áã', 'Ò¼', '·¡', 'Èþ', 'ËÁ', 'Îé', 'Â½', 'Æâ', '°Æ' ,'¾Á'};
	
	private static final char CN_UNIT[] = {'Ê°', '°Û', 'Çª'};
	
	public static boolean isBlank(String source) {
		return (source==null || source.trim().length()==0);
	}
	
	public static boolean isNotBlank(String source) {
		return (source!=null && source.trim().length()>0);
	}
	
	public static boolean equalString(String string1, String string2)
	{
		if (StringUtil.isNotBlank(string1)) {
			return string1.equals(string2);
		} else if (StringUtil.isBlank(string1) && StringUtil.isBlank(string2))  {
			return true;
		}
		return false;
	}
	
	public static String trim(String source) {
		if (source!=null) {
			return source.replace(" ", "");
		}
		return source;
	}
	
	public static String formatString(String source) {
		if (StringUtil.isNotBlank(source)) {
			return source;
		}
		return "--";
	}
	
	public static String trimString(String source) {
		if (StringUtil.isNotBlank(source)) {
			return source.trim();
		}
		return "";
	}
	
	public static List<String> splitString(String source, int width) {
		if (StringUtil.isNotBlank(source)) {
			List<String> stringList = new ArrayList<String>();
			StringBuilder builder = new StringBuilder();
			int length=0;
			for(int i=0;i<source.length();++i) {
				char c = source.charAt(i);
				builder.append(c);
				if (StringUtil.isAscii(c)) {
					length+=11;
				} else if (StringUtil.isChinese(c)) {
					length+=15;
				}
				if (length>width-13) {
					stringList.add(builder.toString());
					builder.setLength(0);
					length=0;
				}
			}
			if (builder.length()>0) {
				stringList.add(builder.toString());
			}
			return stringList;
		}		
		return null;
	}
	
	public static int getStringLength(String source) {
		if (StringUtil.isNotBlank(source)) {
			int length=0;
			for(int i=0;i<source.length();++i) {
				char c = source.charAt(i);
				if (StringUtil.isAscii(c)) {
					length+=11;
				} else if (StringUtil.isChinese(c)) {
					length+=15;
				}
			}
			return length;
		}		
		return 0;
	}
	
	public static String[] transferString(int number) {
		String stringList[] = {"Áã", "Áã", "Áã", "Áã", "Áã"};
		StringBuilder builder = new StringBuilder();
		int index = 0;
		while (index<4 && number>0) {
				int digit = number%10;
				number=number/10;
				builder.append(CN_NUM[digit]);
				stringList[index] = builder.toString();
				builder.setLength(0);
				index++;
		}	
		if (number>0) {
			index = 0;
			while (number>0 && index<4) {
				int digit = number%10;
				if (digit==0) {
					builder.insert(0, CN_UNIT[index]);
				} else {
					if (index>0) {
						builder.insert(0, CN_UNIT[index-1]);
					}
					builder.insert(0, CN_NUM[digit]);
				}
				number=number/10;
				index++;
			}
			stringList[4] = builder.toString();
		}
		for (int i=0;i<3;++i) {
			String digit = stringList[i];
			stringList[i] = stringList[4-i];
			stringList[4-i] = digit;
		}
		return stringList;
	}
	
	public static String formatShortString(String source, int size) {
		if (source!=null && source.trim().length()>0) {
			source = source.trim();
			if (source.length()>size) {
				source = source.substring(0, size);
			}
		}
		return source;
	}
	
	public static boolean isNumber(String source) {
		if (source!=null && source.trim().length()>0) {
			return source.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		}
		return false;
	}
	
	public static boolean isNotNumber(String source) {
		if (source==null || source.trim().length()==0) {
			return true;
		}
		return (!source.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$"));
	}
	
	public static boolean isInteger(String source) {
		if (source!=null && source.trim().length()>0) {
			boolean isNumber = source.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
			if (isNumber) {
				return (!source.contains("."));
			}
		}
		return false;
	}
	
	public static boolean isNotInteger(String source) {
		if (source==null || source.trim().length()==0) {
			return true;
		}
		boolean isNumber = source.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		if (isNumber) {
			return source.contains(".");
		}
		return true;
	}
	
	public static boolean isNotExpress(String source) {
		if (source==null || source.trim().length()==0) {
			return true;
		}
		if (source.startsWith("+") || source.endsWith("+")) {
			return true;
		}
		if (source.contains("++")) {
			return true;
		}
		String list[] = source.split("[+]");
		for (String element:list){
			if (element.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
				if (element.contains(".")){
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isExpress(String source) {
		if (isNotBlank(source)) {
			if ((!source.startsWith("+")) && (!source.endsWith("+")) && (!source.contains("++"))) {
				String list[] = source.split("[+]");
				for (String element:list){
					if (element.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
						if (element.contains(".")){
							return false;
						}
					}
				}
				return true;
			}
		}		
		return false;
	}
	
	private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
	
	private static boolean isAscii(char c) {
	   if (c>0 && c<128) {
		   return true;
	   } else {
		   return false;
	   }
	}
}
