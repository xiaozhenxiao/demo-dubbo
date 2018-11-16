package com.wz.jd.jmeter;

import com.jd.crp.domain.task.Task;
import com.jd.crp.service.task.TaskDispatcherService;
import com.wz.jd.spring.ApplicationContextUtil;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class JsfPtest implements JavaSamplerClient {
	TaskDispatcherService taskDispatcherService = null;

	/** 参数化 **/
	@Override
	public Arguments getDefaultParameters() {
		Arguments args = new Arguments();
		args.addArgument("type", "10");
		args.addArgument("id", "10");
		return args;
	}
	@Override
	public void setupTest(JavaSamplerContext arg0) {
		try {
			this.taskDispatcherService = ApplicationContextUtil.getSpringBean("taskDispatcherService", TaskDispatcherService.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult result = new SampleResult();
		result.sampleStart();
		Integer type = arg0.getIntParameter("type");
		Long id = arg0.getLongParameter("id");
		Task task = new Task.Builder().id(id).type(type).build();
		try {
			taskDispatcherService.dispatchMessage(task);
			result.setSuccessful(true);
			result.sampleEnd();
		} catch (Exception e) {
			result.setSuccessful(false);
			result.sampleEnd();
			System.out.println("fail************************");
			e.printStackTrace();
		}
		//System.out.println("调用接口的结果=========================" +response);

		return result;
	}
	@Override
	public void teardownTest(JavaSamplerContext arg0) {
	}

	public static void main(String[] args) {
		JsfPtest testCase = new JsfPtest();
		JavaSamplerContext arg0 = new JavaSamplerContext(
				testCase.getDefaultParameters());
		testCase.setupTest(arg0);
		testCase.runTest(arg0);
		testCase.teardownTest(arg0);

	}

}