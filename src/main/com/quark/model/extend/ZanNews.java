package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class ZanNews extends Model<ZanNews>{

    public static ZanNews dao = new ZanNews();

    public static final String zan_news_id="columnName=zan_news_id,remarks=ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=用户ID,dataType=int,defaultValue=0";

    public static final String news_id="columnName=news_id,remarks=新闻ID,dataType=int,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=点赞时间,dataType=String,defaultValue=null";

}
