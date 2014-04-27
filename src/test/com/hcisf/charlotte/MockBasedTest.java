package com.hcisf.charlotte;


import com.hcisf.PowerMockWithSpecTestNameRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;


@RunWith(PowerMockWithSpecTestNameRunner.class)
public class MockBasedTest {

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
