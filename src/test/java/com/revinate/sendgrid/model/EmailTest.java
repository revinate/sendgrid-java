package com.revinate.sendgrid.model;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EmailTest {

    Email email;

    @Before
    public void setUp() throws Exception {
        email = new Email();
    }

    @Test
    public void testAddTo() {
        String address = "email@example.com";
        String address2 = "email2@example.com";
        email.addTo(address);
        email.addTo(address2);

        String[] correct = {address, address2};

        assertArrayEquals(correct, email.getTos());
    }

    @Test
    public void testAddToWithAFrom() {
        String address = "email@example.com";
        String fromaddress = "from@mailinator.com";
        email.addTo(address);
        email.setFrom(fromaddress);

        String[] correct = {address};

        assertArrayEquals(correct, email.getTos());
        assertEquals(fromaddress, email.getFrom());
    }

    @Test
    public void testAddToName() {
        String name = "John";
        email.addToName(name);

        String[] correct = {name};

        assertArrayEquals(correct, email.getToNames());
    }

    @Test
    public void testAddCc() {
        String address = "email@example.com";
        email.addCc(address);

        String[] correct = {address};

        assertArrayEquals(correct, email.getCcs());
    }

    @Test
    public void testSetFrom() {
        String address = "email@example.com";
        email.setFrom(address);

        assertEquals(address, email.getFrom());
    }

    @Test
    public void testSetFromName() {
        String fromname = "Uncle Bob";
        email.setFromName(fromname);

        assertEquals(fromname, email.getFromName());
    }

    @Test
    public void testSetReplyTo() {
        String address = "email@example.com";
        email.setReplyTo(address);

        assertEquals(address, email.getReplyTo());
    }

    @Test
    public void testAddBcc() {
        String address = "email@example.com";
        email.addBcc(address);

        String[] correct = {address};

        assertArrayEquals(correct, email.getBccs());
    }

    @Test
    public void testSetSubject() {
        String subject = "This is a subject";
        email.setSubject(subject);

        assertEquals(subject, email.getSubject());
    }

    @Test
    public void testSetText() {
        String text = "This is some email text.";
        email.setText(text);

        assertEquals(text, email.getText());
    }

    @Test
    public void testSetHtml() {
        String html = "This is some email text.";
        email.setHtml(html);

        assertEquals(html, email.getHtml());
    }

    @Test
    public void testAddHeader() {
        email.addHeader("key", "value");
        email.addHeader("other", "other-value");

        Map<String, String> correct = new HashMap<String, String>();
        correct.put("key", "value");
        correct.put("other", "other-value");

        assertEquals(correct, email.getHeaders());
    }

    @Test
    public void testSetTemplateId() {
        email.setTemplateId("abc-123");

        String filters = email.getSMTPAPI().jsonString();

        JSONObject obj = new JSONObject();
        obj.put("filters", new JSONObject().put("templates", new JSONObject()
                .put("settings", new JSONObject().put("enable", 1)
                        .put("template_id", "abc-123"))));

        JSONAssert.assertEquals(filters, obj.toString(), false);
    }

    @Test
    public void testSmtpapiToHeader() {
        String[] expected = {"example@email.com"};

        email.getSMTPAPI().addTo(expected[0]);
        String[] result = email.getSMTPAPI().getTos();

        assertArrayEquals(expected, result);
    }

    @Test
    public void testSetIpPool() {
        String expected = "transactional";

        email.setIpPool(expected);
        String result = email.getIpPool();

        assertEquals(expected, result);
    }
}
