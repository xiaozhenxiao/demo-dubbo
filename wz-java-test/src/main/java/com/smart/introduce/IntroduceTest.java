package com.smart.introduce;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class IntroduceTest {

    public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
        ForumService forumService = (ForumService)ctx.getBean("forumService");
        forumService.removeForum(10);
        forumService.removeTopic(1022);
        Monitorable moniterable = (Monitorable)forumService;
        moniterable.setMonitorActive(true);
        forumService.removeForum(10);
        forumService.removeTopic(1022); 
	}
}
