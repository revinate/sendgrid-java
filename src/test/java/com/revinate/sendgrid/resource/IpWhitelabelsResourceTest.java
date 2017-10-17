package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.IpWhitelabel;
import com.revinate.sendgrid.model.IpWhitelabelCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.util.JsonUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IpWhitelabelsResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    IpWhitelabelsResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new IpWhitelabelsResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        IpWhitelabelResource subresource = resource.entity("1");

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("1"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/whitelabel/ips"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnIpWhitelabels() throws Exception {
        IpWhitelabelCollection response = JsonUtils.fromJson(readFile("/responses/ip-whitelabels.json"),
                IpWhitelabelCollection.class);

        when(client.get("https://api.sendgrid.com/v3/whitelabel/ips", IpWhitelabelCollection.class, credential))
                .thenReturn(response);

        List<IpWhitelabel> ipWhitelabels = resource.list();

        assertThat(ipWhitelabels, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldPostAndReturnWhitelabel() throws Exception {
        IpWhitelabel response = JsonUtils.fromJson(readFile("/responses/ip-whitelabel.json"), IpWhitelabel.class);
        IpWhitelabel ipWhitelabel = new IpWhitelabel("test.com", "test", "1.1.1.1");

        when(client.post("https://api.sendgrid.com/v3/whitelabel/ips",
                IpWhitelabel.class, credential, ipWhitelabel, SendGridHttpClient.RequestType.JSON)).thenReturn(response);

        IpWhitelabel whitelabel1 = resource.create(ipWhitelabel);

        assertThat(whitelabel1, sameInstance(response));
    }

}
