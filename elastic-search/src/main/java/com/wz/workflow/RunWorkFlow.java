package com.wz.workflow;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.basic.BasicWorkflow;
import com.opensymphony.workflow.config.DefaultConfiguration;
import com.opensymphony.workflow.spi.Step;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Workflow
 * wangzhen23
 * 2019/8/16.
 */
public class RunWorkFlow {
    private static Workflow workflow = new BasicWorkflow("testuser");

    public static void main(String[] args) throws WorkflowException {
        DefaultConfiguration config = new DefaultConfiguration();
        workflow.setConfiguration(config);
        //workflow name(定义在workflows.xml里，通过workflow factory处理)
        long workflowId = workflow.initialize("myWorkflow", 0, null);

        Collection currentSteps = workflow.getCurrentSteps(workflowId);
        //校验只有一个当前步骤
        assert currentSteps.size() == 1 : "Unexpected number of current steps";

        //校验这个步骤是1
        Step currentStep = (Step) currentSteps.iterator().next();
        assert currentStep.getStepId() == 1 : "Unexpected current step";

        int[] availableActions = workflow.getAvailableActions(workflowId);
        //校验只有一个可执行的动作
        assert 1 == availableActions.length : "Unexpected number of available actions";
        //校验这个步骤是1
        assert 1 == availableActions[0] : "Unexpected available action";

        workflow.doAction(workflowId, 1, null);

        Map inputs = new HashMap();
        inputs.put("working.title", "the quick brown fox");// jumped over the lazy dog, thus making this a very long title");
        workflow.doAction(workflowId, 2, inputs);

        availableActions = workflow.getAvailableActions(workflowId);
        assert 0 == availableActions.length : "Unexpected number of available actions----";


    }
}
