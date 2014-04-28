package com.hcisf;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.common.internal.impl.AbstractCommonPowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.annotation.Annotation;


public class PowerMockWithSpecTestNameRunner extends AbstractCommonPowerMockRunner {

    public PowerMockWithSpecTestNameRunner(Class<?> klass) throws Exception {
        super(klass, DisplayTestNameAsSpecClassRunner.class);
    }

    /**
     * Clean up some state to avoid OOM issues
     */
    @Override
    public void run(RunNotifier notifier) {
        Description description = getDescription();
        try {
            super.run(notifier);
        } finally {
            Whitebox.setInternalState(description, "fAnnotations", new Annotation[]{});
        }
    }
}
