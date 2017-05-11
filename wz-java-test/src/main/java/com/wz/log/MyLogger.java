package com.wz.log;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Created by wangzhen23 on 2017/4/21.
 */
public class MyLogger {
    private static final Logger logger = LoggerFactory.getLogger(MyLogger.class);

    public static void main(String[] args) {
        logger.info("wang xiao xiao test test !!!!!!");
        MDC.put("invokeNo", "xxxxxxxxxxxx");
        logger.info("wang xiao xiao test test !!!!!!");
        NDC.push("yyyyyyyyyyyyyyyy");
        NDC.push("xxxxxxxxxxxxxxxx");
        NDC.push("zzzzzzzzzzzzzzzz");
        logger.info("wang xiao xiao test test !!!!!!");
        NDC.pop();
        logger.info("wang xiao xiao test test !!!!!!");
    }
}
