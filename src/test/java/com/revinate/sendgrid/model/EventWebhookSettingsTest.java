package com.revinate.sendgrid.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class EventWebhookSettingsTest {

    EventWebhookSettings eventWebhookSettings;

    @Before
    public void setUp() throws Exception {
        eventWebhookSettings = new EventWebhookSettings();
    }

    @Test
    public void setAllEnabled_shouldEnableAll() throws Exception {
        eventWebhookSettings.setAllEnabled(true);
        assertThat(eventWebhookSettings.getAllEnabled(), equalTo(true));
    }

    @Test
    public void setAllEnabled_shouldDisableAll() throws Exception {
        eventWebhookSettings.setAllEnabled(false);
        assertThat(eventWebhookSettings.getAllDisabled(), equalTo(true));
    }
}