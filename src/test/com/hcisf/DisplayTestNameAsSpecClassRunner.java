package com.hcisf;

import org.junit.internal.runners.InitializationError;
import org.powermock.core.spi.PowerMockTestListener;
import org.powermock.modules.junit4.internal.impl.PowerMockJUnit44RunnerDelegateImpl;

import java.lang.reflect.Method;

/**
 * Created by juliomistral on 4/25/14.
 */
public class DisplayTestNameAsSpecClassRunner extends PowerMockJUnit44RunnerDelegateImpl {

    public DisplayTestNameAsSpecClassRunner(Class<?> klass, String[] methodsToRun, PowerMockTestListener[] listeners) throws InitializationError {
        super(klass, methodsToRun, listeners);
    }

    public DisplayTestNameAsSpecClassRunner(Class<?> klass, String[] methodsToRun) throws InitializationError {
        super(klass, methodsToRun);
    }

    public DisplayTestNameAsSpecClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected String testName(Method method) {
        String methodName = super.testName(method);
        StringBuffer output = new StringBuffer();

        for (char currentChar : methodName.toCharArray()) {
            if (Character.isUpperCase(currentChar)) {
                currentChar = Character.toLowerCase(currentChar);
                output.append(' ');
            }

            output.append(currentChar);
        }

        return output.toString();
    }
}
