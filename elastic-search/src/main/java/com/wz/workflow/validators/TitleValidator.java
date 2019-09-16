package com.wz.workflow.validators;

import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;
import com.opensymphony.workflow.WorkflowException;

import java.util.Map;

/**
 * Validator
 * wangzhen23
 * 2019/8/16.
 */
public class TitleValidator implements Validator {

    @Override
    public void validate(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        String title = (String) transientVars.get("working.title");
        if (title == null) {
            throw new InvalidInputException("Missing working.title");
        }
        if (title.length() > 30) {
            throw new InvalidInputException("Working title too long");
        }
    }
}
