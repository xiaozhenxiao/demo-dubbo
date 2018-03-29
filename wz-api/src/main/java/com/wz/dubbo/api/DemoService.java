package com.wz.dubbo.api;

import com.smile.wz.processor.Async;

@Async
public interface DemoService {

	String sayHello(String name);

	int returnHello();

	MsgInfo returnMsgInfo(MsgInfo info);

	static String staticMethod(){
		return "STATIC";
	}

	static void test(){

	}

}