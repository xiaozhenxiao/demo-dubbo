package com.wz.jd.jmeter;

import com.google.common.collect.Lists;
import com.wz.dubbo.api.DemoService;
import com.wz.dubbo.api.MsgInfo;
import com.wz.jd.spring.ApplicationContextUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class SearchPhoneOperator implements JavaSamplerClient {
	DemoService demoService = null;

	/** 参数化 **/
	@Override
	public Arguments getDefaultParameters() {
		Arguments args = new Arguments();
		args.addArgument("name", "zhangsan");
		args.addArgument("id", "98537");
		return args;
	}
	@Override
	public void setupTest(JavaSamplerContext arg0) {
		try {
			this.demoService = ApplicationContextUtil.getSpringBean("demoService", DemoService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult result = new SampleResult();
		result.sampleStart();
		MsgInfo params = new MsgInfo();
		String name = arg0.getParameter("name");
		Integer id = arg0.getIntParameter("id");
		params.setId(id);
		params.setName(name);
		params.setMsgs(Lists.newArrayList());
		MsgInfo response = demoService.returnMsgInfo(params);
		 //System.out.println("调用接口的结果=========================" +response);
		if (response != null) {
//			 System.out.println("调用接口的结果=========================" +response);
			result.setSuccessful(true);
			result.sampleEnd();
		} else {
//			 System.out.println("调用接口fail");
			result.setSuccessful(false);
			result.sampleEnd();
		}
		return result;
	}
	@Override
	public void teardownTest(JavaSamplerContext arg0) {
	}

	public static void main(String[] args) {
		SearchPhoneOperator testCase = new SearchPhoneOperator();
		JavaSamplerContext arg0 = new JavaSamplerContext(
				testCase.getDefaultParameters());
		testCase.setupTest(arg0);
		testCase.runTest(arg0);
		testCase.teardownTest(arg0);

	}

}