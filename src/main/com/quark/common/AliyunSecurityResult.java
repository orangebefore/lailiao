package com.quark.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by liuhai.lh on 17/01/12.
 */
public class AliyunSecurityResult {

	private boolean pass = true;
	private String info = new String();

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}


}
