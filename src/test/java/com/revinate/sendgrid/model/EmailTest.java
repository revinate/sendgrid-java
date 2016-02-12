package com.revinate.sendgrid.model;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class EmailTest {

    private static final String ADDRESS = "test@email.com";
    private static final String NAME = "Test User";

    Email email;

    @Before
    public void setUp() throws Exception {
        email = new Email();
    }

    @Test
    public void addTo_shouldAddTo() {
        email.addTo(ADDRESS);
        assertThat(email.getTos(), contains(ADDRESS));
    }

    @Test
    public void addToName_shouldAddToName() {
        email.addToName(NAME);
        assertThat(email.getToNames(), contains(NAME));
    }

    @Test
    public void addCc_shouldAddCc() {
        email.addCc(ADDRESS);
        assertThat(email.getCcs(), contains(ADDRESS));
    }

    @Test
    public void addCcName_shouldAddCcName() {
        email.addCcName(NAME);
        assertThat(email.getCcNames(), contains(NAME));
    }

    @Test
    public void addBcc_shouldAddBcc() {
        email.addBcc(ADDRESS);
        assertThat(email.getBccs(), contains(ADDRESS));
    }

    @Test
    public void addBccName_shouldAddBccName() {
        email.addBccName(NAME);
        assertThat(email.getBccNames(), contains(NAME));
    }

    @Test
    public void setFrom_shouldSetFrom() {
        email.setFrom(ADDRESS);
        assertThat(email.getFrom(), equalTo(ADDRESS));
    }

    @Test
    public void setFromName_shouldSetFromName() {
        email.setFromName(NAME);
        assertThat(email.getFromName(), equalTo(NAME));
    }

    @Test
    public void setReplyTo_shouldSetReplyTo() {
        email.setReplyTo(ADDRESS);
        assertThat(email.getReplyTo(), equalTo(ADDRESS));
    }

    @Test
    public void setSubject_shouldSetSubject() {
        String subject = "This is a subject";
        email.setSubject(subject);
        assertThat(email.getSubject(), equalTo(subject));
    }

    @Test
    public void setText_shouldSetText() {
        String text = "This is some email text.";
        email.setText(text);
        assertThat(email.getText(), equalTo(text));
    }

    @Test
    public void setHtml_shouldSetHtml() {
        String html = "This is some email text.";
        email.setHtml(html);
        assertThat(email.getHtml(), equalTo(html));
    }

    @Test
    public void setAttachment_shouldSetAttachment() throws Exception {
        File file = new File(getClass().getResource("/test.txt").getFile());
        email.setAttachment("test.txt", file);

        InputStream inputStream = getClass().getResourceAsStream("/image.png");
        email.setAttachment("image.png", inputStream);

        assertThat(email.getAttachments(), notNullValue());
        assertThat(email.getAttachments().size(), equalTo(2));
        assertThat(email.getAttachments(), hasKey("test.txt"));
        assertThat(email.getAttachments(), hasKey("image.png"));
    }

    @Test
    public void setHeader_shouldSetHeader() throws Exception {
        email.setHeader("key", "value");
        email.setHeader("key2", "value2");

        assertThat(email.getHeaders(), notNullValue());
        assertThat(email.getHeaders().size(), equalTo(2));
        assertThat(email.getHeaders(), hasEntry("key", "value"));
        assertThat(email.getHeaders(), hasEntry("key2", "value2"));
    }

    @Test
    public void setContentId_shouldSetContentId() throws Exception {
        email.setContentId("key", "value");

        assertThat(email.getContentIds(), notNullValue());
        assertThat(email.getContentIds().size(), equalTo(1));
        assertThat(email.getContentIds(), hasEntry("key", "value"));
    }

    @Test
    public void setTemplateId_shouldSetFilter() throws Exception {
        email.setTemplateId("abc-123");
        Map<String, Object> filter = email.getFilter("templates");

        assertThat(filter, notNullValue());
        assertThat(filter.size(), equalTo(2));
        assertThat(filter, hasEntry("enable", (Object) 1));
        assertThat(filter, hasEntry("template_id", (Object) "abc-123"));
    }

    @Test
    public void addSmtpApiTo_shouldAddSmtpApiTo() throws Exception {
        email.addSmtpApiTo(ADDRESS);
        assertThat(email.getSmtpApiTos(), contains(ADDRESS));
    }

    @Test
    public void setIpPool_shouldSetIpPool() throws Exception {
        email.setIpPool("transactional");
        assertThat(email.getIpPool(), equalTo("transactional"));
    }
}
