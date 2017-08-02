package com.sjy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Dict.java
 * @Package com.sjy.annotation
 * @Description: 字典项注解
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月2日 上午10:30:30
 * @version V1.0
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dict {

	String name() default "";

	String text() default "";

	boolean editable() default false;

}
