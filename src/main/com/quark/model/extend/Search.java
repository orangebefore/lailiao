package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Search extends Model<Search>{

    public static Search dao = new Search();

    public static final String search_id="columnName=search_id,remarks=搜索记录,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=null";

    public static final String isonline="columnName=isonline,remarks=是否在线：0-不在线，1-在线,dataType=int,defaultValue=null";

    public static final String is_vip="columnName=is_vip,remarks=是否是高级会员：0-不是，1-是,dataType=int,defaultValue=null";

    public static final String province="columnName=province,remarks=生活工作地区-省,dataType=String,defaultValue=";

    public static final String city="columnName=city,remarks=生活工作地区-市,dataType=String,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=搜索时间,dataType=String,defaultValue=null";

}
