package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.BaseSendGridTest;
import com.revinate.sendgrid.model.Whitelabel;
import com.revinate.sendgrid.model.WhitelabelCollection;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.SendGridHttpClient.RequestType;
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
public class DomainWhitelabelsResourceTest extends BaseSendGridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SendGridHttpClient client;

    @Mock
    Credential credential;

    DomainWhitelabelsResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new DomainWhitelabelsResource("https://api.sendgrid.com/v3", client, credential);
    }

    @Test
    public void entity_shouldReturnResource() throws Exception {
        DomainWhitelabelResource subresource = resource.entity("1");

        assertThat(subresource, notNullValue());
        assertThat(subresource.getId(), equalTo("1"));
        assertThat(subresource.getBaseUrl(), equalTo(resource.getBaseUrl() + "/whitelabel/domains"));
        assertThat(subresource.getClient(), sameInstance(client));
        assertThat(subresource.getCredential(), sameInstance(resource.getCredential()));
    }

    @Test
    public void list_shouldReturnWhitelabels() throws Exception {
        WhitelabelCollection response = JsonUtils.fromJson(readFile("/responses/domain-whitelabels.json"),
                WhitelabelCollection.class);

        when(client.get("https://api.sendgrid.com/v3/whitelabel/domains", WhitelabelCollection.class, credential))
                .thenReturn(response);

        List<Whitelabel> whitelabels = resource.list();

        assertThat(whitelabels, sameInstance(response.getData()));
    }

    @Test
    public void create_shouldPostAndReturnWhitelabel() throws Exception {
        Whitelabel response = JsonUtils.fromJson(readFile("/responses/domain-whitelabel.json"), Whitelabel.class);
        Whitelabel whitelabel = new Whitelabel();
        whitelabel.setDomain("email.com");
        whitelabel.setSubdomain("m");
        whitelabel.setAutomaticSecurity(true);
        whitelabel.setDefault(true);

        when(client.post("https://api.sendgrid.com/v3/whitelabel/domains",
                Whitelabel.class, credential, whitelabel, RequestType.JSON)).thenReturn(response);

        Whitelabel whitelabel1 = resource.create(whitelabel);

        assertThat(whitelabel1, sameInstance(response));
    }
}
