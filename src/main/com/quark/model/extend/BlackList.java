package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class BlackList extends Model<BlackList>{

    public static BlackList dao = new BlackList();

    public static final String black_list_id="columnName=black_list_id,remarks=黑名单列表,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String black_user_id="columnName=black_user_id,remarks=被屏蔽用户ID/被举报用户ID,dataType=int,defaultValue=null";

    public static final String type="columnName=type,remarks=1-举报，2-屏蔽,dataType=int,defaultValue=1";

    public static final String post_time="columnName=post_time,remarks=日期,dataType=String,defaultValue=null";

    public static final String report_beans="columnName=report_beans,remarks=举报类型,dataType=String,defaultValue=";

}
