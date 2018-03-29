package com.smile.wz.processor;

import java.lang.annotation.*;

/**
 * 异步注释
 * wangzhen23
 * 2018/3/28.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface Async {
}
