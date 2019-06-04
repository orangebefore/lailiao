package com.quark.common;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by liuhai.lh on 17/01/12.
 */
public class AliyunSecurityBase {

    public static String accessKeyId = "LTAIU9CKrsWannKt";
    public static String accessKeySecret = "MpmFqxgfFOELDIufZTkDsWOcDwf4sy";

    public static String regionId = "cn-shanghai";



    public static String getDomain(){
         if("cn-shanghai".equals(regionId)){
             return "green.cn-shanghai.aliyuncs.com";
         }

         if("cn-hangzhou".equals(regionId)){
             return "green.cn-hangzhou.aliyuncs.com";
         }

        return "green.cn-shanghai.aliyuncs.com";
    }

    public static String getEndPointName(){
        return regionId;
    }

}
