package com.jd.java;

import com.alibaba.fastjson.JSON;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;

/**
 * TODO
 * wangzhen23
 * 2017/8/24.
 */
public class JavaTest {
    public static void main(String[] args) {
        String[] strArray = ScheduleTaskType.splitTaskItem(
                "0:{TYPE=A,KIND=1},1:{TYPE=A,KIND=2},2:{TYPE=A,KIND=3},3:{TYPE=A,KIND=4}," +
                        "4:{TYPE=A,KIND=5},5:{TYPE=A,KIND=6},6:{TYPE=A,KIND=7},7:{TYPE=A,KIND=8}," +
                        "8:{TYPE=A,KIND=9},9:{TYPE=A,KIND=10}");
        System.out.println("===============================================================");
        System.out.println(JSON.toJSONString(strArray));
        System.out.println("===============================================================");
    }
}
