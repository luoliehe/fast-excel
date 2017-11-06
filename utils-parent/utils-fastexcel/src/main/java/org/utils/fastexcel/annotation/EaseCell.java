package org.utils.fastexcel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.utils.fastexcel.CellFormat;

/**
 * Excel映射注解
 * 
 * @author luoliehe
 */

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EaseCell {

	/**
	 * Excel的列下标，从0开始
	 * 
	 * @return
	 */
	int index();

	/**
	 * Excel列对象名称
	 * 
	 * @return
	 */
	String name();

	/**
	 * Excel列头名称
	 * 
	 * @return
	 */
	String label() default "";

	/**
	 * 自定义格式化Cell的值
	 * 
	 * @return
	 */
	Class<? extends CellFormat> format() default CellFormat.class;
}
