package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class UserIncome extends Model<UserIncome>{

    public static UserIncome dao = new UserIncome();

    public static final String user_income_id="columnName=user_income_id,remarks=用户年收入集合ID,dataType=int,defaultValue=null";

    public static final String income="columnName=income,remarks=年收入,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=0";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

}
