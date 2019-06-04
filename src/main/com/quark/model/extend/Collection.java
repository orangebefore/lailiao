package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Collection extends Model<Collection>{

    public static Collection dao = new Collection();

    public static final String collection_id="columnName=collection_id,remarks=收藏ID,dataType=int,defaultValue=null";

    public static final String user_id="columnName=user_id,remarks=收藏者ID,dataType=int,defaultValue=null";

    public static final String collection_user_id="columnName=collection_user_id,remarks=被收藏者ID,dataType=int,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=收藏时间,dataType=String,defaultValue=null";

}
