package com.wz.process;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * wangzhen23
 * 2019/1/8.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Param {
}
