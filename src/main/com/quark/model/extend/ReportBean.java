package com.quark.model.extend;
import com.jfinal.plugin.activerecord.Model;

/**
* @author cluo
* 
* @info 
*
* @datetime2018-01-18 14:52:01
*/
public class ReportBean extends Model<ReportBean>{

    public static ReportBean dao = new ReportBean();

    public static final String report_bean_id="columnName=report_bean_id,remarks=,dataType=int,defaultValue=null";

    public static final String report_bean="columnName=report_bean,remarks=举报文字,dataType=String,defaultValue=";

    public static final String post_time="columnName=post_time,remarks=,dataType=String,defaultValue=null";

}
