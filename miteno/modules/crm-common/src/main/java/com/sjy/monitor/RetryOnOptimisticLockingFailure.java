/**
 * 
 */
package com.sjy.monitor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Title: RetryOnOptimisticLockingFailure.java
 * @Package com.sjy.monitor
 * @Description: 乐观锁重试
 * @author liyan
 * @email 289149734@qq.com
 * @date 2018年1月9日 下午3:06:21
 * @version V1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryOnOptimisticLockingFailure {

}
