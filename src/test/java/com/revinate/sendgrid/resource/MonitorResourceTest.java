package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.Monitor;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonitorResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    MonitorResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new MonitorResource("https://api.sendgrid.com/v3/subusers/test1", client, credential);
    }

    @Test
    public void retrieve_shouldReturnMonitor() throws Exception {
        Monitor response = JsonUtils.fromJson(readFile("/responses/monitor.json"), Monitor.class);

        when(client.get("https://api.sendgrid.com/v3/subusers/test1/monitor",
                Monitor.class, credential)).thenReturn(response);

        Monitor monitor = resource.retrieve();

        assertThat(monitor, sameInstance(response));
    }

    @Test
    public void create_shouldPostAndReturnMonitor() throws Exception {
        Monitor response = JsonUtils.fromJson(readFile("/responses/monitor.json"), Monitor.class);
        Monitor monitor = new Monitor();
        monitor.setEmail("monitor@email.com");
        monitor.setFrequency(1000);

        when(client.post("https://api.sendgrid.com/v3/subusers/test1/monitor", monitor,
                Monitor.class, credential)).thenReturn(response);

        Monitor monitor1 = resource.create(monitor);

        assertThat(monitor1, sameInstance(response));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void update_shouldPutAndReturnMonitor() throws Exception {
        Monitor response = JsonUtils.fromJson(readFile("/responses/monitor.json"), Monitor.class);
        Monitor monitor = new Monitor();
        monitor.setEmail("monitor@email.com");
        monitor.setFrequency(1000);

        when(client.put(any(String.class), any(Monitor.class), any(Class.class),
                any(Credential.class))).thenReturn(response);

        Monitor monitor1 = resource.update(monitor);

        assertThat(monitor1, sameInstance(response));

        ArgumentCaptor<Monitor> captor = ArgumentCaptor.forClass(Monitor.class);
        verify(client).put(eq("https://api.sendgrid.com/v3/subusers/test1/monitor"),
                captor.capture(), eq(Monitor.class), eq(credential));

        Monitor monitor2 = captor.getValue();

        assertThat(monitor2, notNullValue());
        assertThat(monitor2.getEmail(), equalTo(monitor.getEmail()));
        assertThat(monitor2.getFrequency(), equalTo(monitor.getFrequency()));
    }

    @Test
    public void delete_shouldDeleteMonitor() throws Exception {
        resource.delete();

        verify(client).delete("https://api.sendgrid.com/v3/subusers/test1/monitor", credential);
    }
}
