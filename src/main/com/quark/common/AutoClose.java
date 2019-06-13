/**
 * 
 */
package com.quark.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.quark.model.extend.User;
import com.quark.utils.DateUtils;


/**
 * @author cluo
 *
 */
public class AutoClose implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (true) {
			List<User> users = User.dao.find("select user_id,is_vip from user where is_vip=1 and vip_end_datetime <?",DateUtils.getCurrentDateTime());
			for(User user : users){
				user.set(user.is_vip, 0).update();
			}
		
		}
	}
}
