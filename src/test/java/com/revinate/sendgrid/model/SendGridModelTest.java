package com.revinate.sendgrid.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class SendGridModelTest {

    @Test
    public void toString_shouldReturnJson() throws Exception {
        IpPool ipPool = new IpPool();
        assertThat(ipPool.toString(), containsString("JSON"));
    }
}
