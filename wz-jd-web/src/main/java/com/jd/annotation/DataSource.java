package com.jd.annotation;

import java.lang.annotation.*;

/**
 * Created by wangzhen23 on 2016/12/20.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default DataSource.master;

    String master = "MASTER";

    String slave = "SLAVE";
}
