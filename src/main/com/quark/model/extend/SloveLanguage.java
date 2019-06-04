package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class SloveLanguage extends Model<SloveLanguage>{

    public static SloveLanguage dao = new SloveLanguage();

    public static final String slove_language_id="columnName=slove_language_id,remarks=,dataType=int,defaultValue=null";

    public static final String slove_lang="columnName=slove_lang,remarks=甜心语言,dataType=String,defaultValue=null";

    public static final String type="columnName=type,remarks=0-女，1-男,dataType=int,defaultValue=0";

    public static final String post_time="columnName=post_time,remarks=时间,dataType=String,defaultValue=null";

}
