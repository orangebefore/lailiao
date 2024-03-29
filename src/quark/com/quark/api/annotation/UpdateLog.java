/**
 * 
 */
package com.quark.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

/**
 * @author kingsley
 *
 * @datetime 2014年11月29日 下午3:49:06
 */
@Repeatable(UpdateLogs.class)
@Retention(RetentionPolicy.RUNTIME)
// 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ ElementType.FIELD, ElementType.METHOD })
// 定义注解的作用目标**作用范围字段、枚举的常量/方法
@Documented
// 说明该注解将被包含在javadoc中
public @interface UpdateLog {
	/**
	 * 更新日期
	 * 
	 * @return
	 */
	public String date();

	/**
	 * 更新说明
	 * 
	 * @return
	 */
	public String log();

}
