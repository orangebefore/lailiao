package com.quark.utils;

import java.util.Random;

public class RandomUtils {

	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "0123456789"; 
	    //String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ0123456789";    
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 } 
	public static void main(String[] args) {
		System.out.println(getRandomString(5));
	}
}