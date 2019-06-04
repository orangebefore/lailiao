package com.quark.utils;

import java.util.regex.Pattern;

public class StringUtils {
	
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	
	public static String UpperCaseFirstLatter(String from) {
		return from.substring(0, 1).toUpperCase() + from.substring(1);
	}
	
	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}
}
