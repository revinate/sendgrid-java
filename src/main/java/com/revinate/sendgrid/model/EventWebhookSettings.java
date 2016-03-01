package com.revinate.sendgrid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EventWebhookSettings extends SendGridModel {

    private String url;
    private Boolean enabled;
    private Boolean bounce;
    private Boolean click;
    private Boolean deferred;
    private Boolean delivered;
    private Boolean dropped;
    private Boolean groupResubscribe;
    private Boolean groupUnsubscribe;
    private Boolean open;
    private Boolean processed;
    private Boolean spamReport;
    private Boolean unsubscribe;

    public EventWebhookSettings() {
        // no args constructor for Jackson
    }

    public EventWebhookSettings(String url, Boolean allEnabled) {
        this.url = url;
        setAllEnabled(allEnabled);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getBounce() {
        return bounce;
    }

    public void setBounce(Boolean bounce) {
        this.bounce = bounce;
    }

    public Boolean getClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }

    public Boolean getDeferred() {
        return deferred;
    }

    public void setDeferred(Boolean deferred) {
        this.deferred = deferred;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getDropped() {
        return dropped;
    }

    public void setDropped(Boolean dropped) {
        this.dropped = dropped;
    }

    public Boolean getGroupResubscribe() {
        return groupResubscribe;
    }

    public void setGroupResubscribe(Boolean groupResubscribe) {
        this.groupResubscribe = groupResubscribe;
    }

    public Boolean getGroupUnsubscribe() {
        return groupUnsubscribe;
    }

    public void setGroupUnsubscribe(Boolean groupUnsubscribe) {
        this.groupUnsubscribe = groupUnsubscribe;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Boolean getSpamReport() {
        return spamReport;
    }

    public void setSpamReport(Boolean spamReport) {
        this.spamReport = spamReport;
    }

    public Boolean getUnsubscribe() {
        return unsubscribe;
    }

    public void setUnsubscribe(Boolean unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    @JsonIgnore
    public Boolean getAllEnabled() {
        return enabled
                && bounce
                && click
                && deferred
                && delivered
                && dropped
                && groupResubscribe
                && groupUnsubscribe
                && open
                && processed
                && spamReport
                && unsubscribe;
    }

    @JsonIgnore
    public Boolean getAllDisabled() {
        return !(enabled
                || bounce
                || click
                || deferred
                || delivered
                || dropped
                || groupResubscribe
                || groupUnsubscribe
                || open
                || processed
                || spamReport
                || unsubscribe);
    }

    @JsonIgnore
    public void setAllEnabled(boolean allEnabled) {
        enabled = allEnabled;
        bounce = allEnabled;
        click = allEnabled;
        deferred = allEnabled;
        delivered = allEnabled;
        dropped = allEnabled;
        groupResubscribe = allEnabled;
        groupUnsubscribe = allEnabled;
        open = allEnabled;
        processed = allEnabled;
        spamReport = allEnabled;
        unsubscribe = allEnabled;
    }
}
