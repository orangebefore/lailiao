package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class Tag extends Model<Tag>{

    public static Tag dao = new Tag();

    public static final String tag_id="columnName=tag_id,remarks=标签集ID,dataType=int,defaultValue=null";

    public static final String tag="columnName=tag,remarks=标签名称,dataType=String,defaultValue=null";

    public static final String post_time="columnName=post_time,remarks=上传时间,dataType=String,defaultValue=null";

    public static final String sort="columnName=sort,remarks=排序：0-N，升序,dataType=int,defaultValue=0";

    public static final String type="columnName=type,remarks=0-甜心宝贝，1-甜心大哥,dataType=int,defaultValue=0";

}
