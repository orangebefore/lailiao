package com.quarkso.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.quark.utils.DateUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("1234567333".substring(7));
		
		long start = System.currentTimeMillis();
        searchBuffer(new File("C://images"), "17", 1024*1024*2);
        System.out.println("耗时：" +  (System.currentTimeMillis() - start) + "ms");
        System.out.println(getCurrentAgeByBirthdate("1988-03-11"));
        
      
		//message=KeyName.APPLY_TASK_HAS;
	}


/**
  * @author jerry.chen
  * @param brithday
  * @return
  * @throws ParseException
  *             根据生日获取年龄;
  */
 public static int getCurrentAgeByBirthdate(String brithday){
  try {
   Calendar calendar = Calendar.getInstance();
   SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
   String currentTime = formatDate.format(calendar.getTime());
   Date today = formatDate.parse(currentTime);
   Date brithDay = formatDate.parse(brithday);
 
   return today.getYear() - brithDay.getYear();
  } catch (Exception e) {
   return 0;
  }
 }
	/**
     * 在指定文件路径搜索含有keyword关键字文件，找到后将文件路径输出到控制台。
     * 先搜索文件名，如果文件名不含keyword关键字时再搜索文件内容是否含有keyword关键字。
     * @param file
     * @param keyword
     * @param bufferSize
     */
    public static void searchBuffer(File file, String keyword, int bufferSize) {
        if (file.isFile()) {
            if(file.getName().contains(keyword.trim())) {
                System.out.println(file.getPath()+"=");
                 
            } else {
                BufferedReader in = null;
                try {
                    try {
                        in = new BufferedReader(new FileReader(file), bufferSize); 
                        String line = "";
                        while ((line = in.readLine()) != null) {
                            if (line.contains(keyword.trim())) {
                                System.err.println(file.getPath()+"==");
                                break;
                            }
                        }
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
 
            }
             
        } else {
            File[] filenames = file.listFiles();
            if (filenames == null) {
                return;
            }
            for (File f : filenames) {
                searchBuffer(f, keyword, bufferSize);
            }
             
        }
    }
     
}
