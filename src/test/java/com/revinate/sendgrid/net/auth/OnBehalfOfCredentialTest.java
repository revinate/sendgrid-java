package com.revinate.sendgrid.net.auth;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnBehalfOfCredentialTest {

    @Mock
    Credential baseCredential;

    @Test
    public void toHttpHeaders_shouldAppendExtraHeader() throws Exception {
        BasicHeader baseAuth = new BasicHeader("Authorization", "Bearer mytoken");
        when(baseCredential.toHttpHeaders()).thenReturn(new Header[]{baseAuth});

        Header[] headers = new OnBehalfOfCredential(baseCredential, "janedoe").toHttpHeaders();
        List<Header> headerList = Arrays.asList(headers);

        assertThat(headers, arrayWithSize(2));
        assertThat(headerList, Matchers.<Header>hasItem(hasProperty("name", is("Authorization"))));
        assertThat(headerList, Matchers.<Header>hasItem(
                allOf(
                        hasProperty("name", is("On-Behalf-Of")),
                        hasProperty("value", is("janedoe"))
                )));
    }
}