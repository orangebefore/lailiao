package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Sweet extends Model<Sweet>{

    public static Sweet dao = new Sweet();

    public static final String sweet_id="columnName=sweet_id,remarks=甜心文化语录ID,dataType=int,defaultValue=null";

    public static final String sweet="columnName=sweet,remarks=甜心文化语录,dataType=String,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=修改时间,dataType=String,defaultValue=null";

    public static final String status="columnName=status,remarks=语录状态：0-删除，1-显示,dataType=int,defaultValue=1";

    public static final String time="columnName=time,remarks=倒计时显示时间：单位秒,dataType=int,defaultValue=null";

    public static final String show_count="columnName=show_count,remarks=已显示次数,dataType=int,defaultValue=null";

}
