package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class News extends Model<News>{

    public static News dao = new News();

    public static final String news_id="columnName=news_id,remarks=,dataType=int,defaultValue=null";

    public static final String title="columnName=title,remarks=标题,dataType=String,defaultValue=";

    public static final String new_abstract="columnName=new_abstract,remarks=摘要,dataType=String,defaultValue=";

    public static final String cover="columnName=cover,remarks=封面,dataType=String,defaultValue=";

    public static final String content="columnName=content,remarks=内容-正文,dataType=String,defaultValue=null";

    public static final String read_num="columnName=read_num,remarks=阅读数,dataType=int,defaultValue=10";

    public static final String zan_num="columnName=zan_num,remarks=点赞数,dataType=int,defaultValue=10";

    public static final String writer="columnName=writer,remarks=作者,dataType=String,defaultValue=admin";

    public static final String send_type="columnName=send_type,remarks=发送类型：0-女，1-男，2-全部发送,dataType=int,defaultValue=2";

    public static final String publish_date="columnName=publish_date,remarks=发表日期,dataType=String,defaultValue=";

    public static final String publish_YM="columnName=publish_YM,remarks=发布年月,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=发表时间,dataType=String,defaultValue=null";

}
