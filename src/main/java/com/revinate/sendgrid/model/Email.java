package com.revinate.sendgrid.model;

import com.revinate.sendgrid.smtpapi.SmtpApi;
import com.revinate.sendgrid.smtpapi.SmtpApiException;
import com.revinate.sendgrid.smtpapi.SmtpApiImpl;
import com.revinate.sendgrid.util.JsonUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Email extends SendGridModel implements SmtpApi {

    private List<String> tos = new ArrayList<String>();
    private List<String> toNames = new ArrayList<String>();
    private List<String> ccs = new ArrayList<String>();
    private List<String> ccNames = new ArrayList<String>();
    private List<String> bccs = new ArrayList<String>();
    private List<String> bccNames = new ArrayList<String>();
    private String from;
    private String fromName;
    private String replyTo;
    private String subject;
    private String text;
    private String html;
    private Map<String, InputStream> attachments = new HashMap<String, InputStream>();
    private Map<String, String> contentIds = new HashMap<String, String>();
    private Map<String, String> headers = new HashMap<String, String>();

    private SmtpApi smtpApi = new SmtpApiImpl();

    public List<String> getTos() {
        return new ArrayList<String>(tos);
    }

    public Email setTos(List<String> tos) {
        this.tos = new ArrayList<String>(tos);
        return this;
    }

    public Email addTo(String to) {
        tos.add(to);
        return this;
    }

    public Email addTo(String to, String name) {
        addTo(to);
        return addToName(name);
    }

    public List<String> getToNames() {
        return new ArrayList<String>(toNames);
    }

    public Email setToNames(List<String> toNames) {
        this.toNames = new ArrayList<String>(toNames);
        return this;
    }

    public Email addToName(String toName) {
        toNames.add(toName);
        return this;
    }

    public List<String> getCcs() {
        return new ArrayList<String>(ccs);
    }

    public Email setCcs(List<String> ccs) {
        this.ccs = new ArrayList<String>(ccs);
        return this;
    }

    public Email addCc(String cc) {
        ccs.add(cc);
        return this;
    }

    public List<String> getCcNames() {
        return new ArrayList<String>(ccNames);
    }

    public Email setCcNames(List<String> ccNames) {
        this.ccNames = new ArrayList<String>(ccNames);
        return this;
    }

    public Email addCcName(String ccName) {
        ccNames.add(ccName);
        return this;
    }

    public List<String> getBccs() {
        return new ArrayList<String>(bccs);
    }

    public Email setBccs(List<String> bccs) {
        this.bccs = new ArrayList<String>(bccs);
        return this;
    }

    public Email addBcc(String bcc) {
        bccs.add(bcc);
        return this;
    }

    public List<String> getBccNames() {
        return new ArrayList<String>(bccNames);
    }

    public Email setBccNames(List<String> bccNames) {
        this.bccNames = new ArrayList<String>(bccNames);
        return this;
    }

    public Email addBccName(String bccName) {
        bccNames.add(bccName);
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Email setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getFromName() {
        return fromName;
    }

    public Email setFromName(String fromName) {
        this.fromName = fromName;
        return this;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public Email setReplyTo(String replyTo) {
        this.replyTo = replyTo;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Email setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getText() {
        return text;
    }

    public Email setText(String text) {
        this.text = text;
        return this;
    }

    public String getHtml() {
        return html;
    }

    public Email setHtml(String html) {
        this.html = html;
        return this;
    }

    public Map<String, InputStream> getAttachments() {
        return new HashMap<String, InputStream>(attachments);
    }

    public Email setAttachments(Map<String, InputStream> attachments) {
        this.attachments = new HashMap<String, InputStream>(attachments);
        return this;
    }

    public InputStream getAttachment(String name) {
        return attachments.get(name);
    }

    public Email setAttachment(String name, InputStream file) {
        attachments.put(name, file);
        return this;
    }

    public Email setAttachment(String name, File file) throws IOException {
        return setAttachment(name, new FileInputStream(file));
    }

    public Email setAttachment(String name, String file) throws IOException {
        return setAttachment(name, new ByteArrayInputStream(file.getBytes()));
    }

    public Map<String, String> getContentIds() {
        return new HashMap<String, String>(contentIds);
    }

    public Email setContentIds(Map<String, String> contentIds) {
        this.contentIds = new HashMap<String, String>(contentIds);
        return this;
    }

    public String getContentId(String key) {
        return contentIds.get(key);
    }

    public Email setContentId(String key, String val) {
        contentIds.put(key, val);
        return this;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<String, String>(headers);
    }

    public Email setHeaders(Map<String, String> headers) {
        this.headers = new HashMap<String, String>(headers);
        return this;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Email setHeader(String key, String val) {
        headers.put(key, val);
        return this;
    }

    public String toHeaders() {
        try {
            return JsonUtils.toJson(headers);
        } catch (IOException e) {
            return "{}";
        }
    }

    /**
     * Convenience method to set the template
     *
     * @param templateId The ID string of your template
     * @return this
     */
    public Email setTemplateId(String templateId) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("enable", 1);
        filter.put("template_id", templateId);
        setFilter("templates", filter);
        return this;
    }

    // Implementations of SMTP API methods: delegate to the SmtpApiImpl

    @Override
    public String getVersion() {
        return smtpApi.getVersion();
    }

    @Override
    public String toSmtpApiHeader() {
        return smtpApi.toSmtpApiHeader();
    }

    @Override
    public String toRawSmtpApiHeader() {
        return smtpApi.toRawSmtpApiHeader();
    }

    @Override
    public Integer getAsmGroupId() {
        return smtpApi.getAsmGroupId();
    }

    @Override
    public Email setAsmGroupId(Integer val) {
        smtpApi.setAsmGroupId(val);
        return this;
    }

    @Override
    public Integer getSendAt() {
        return smtpApi.getSendAt();
    }

    @Override
    public Email setSendAt(Integer val) {
        smtpApi.setSendAt(val);
        return this;
    }

    @Override
    public String getIpPool() {
        return smtpApi.getIpPool();
    }

    @Override
    public Email setIpPool(String ipPool) {
        smtpApi.setIpPool(ipPool);
        return this;
    }

    @Override
    public List<String> getSmtpApiTos() {
        return smtpApi.getSmtpApiTos();
    }

    @Override
    public Email setSmtpApiTos(List<String> tos) {
        smtpApi.setSmtpApiTos(tos);
        return this;
    }

    @Override
    public Email addSmtpApiTo(String to) {
        smtpApi.addSmtpApiTo(to);
        return this;
    }

    @Override
    public Email addSmtpApiTo(String to, String name) {
        smtpApi.addSmtpApiTo(to, name);
        return this;
    }

    @Override
    public List<String> getCategories() {
        return smtpApi.getCategories();
    }

    @Override
    public Email setCategories(List<String> categories) {
        smtpApi.setCategories(categories);
        return this;
    }

    @Override
    public Email addCategory(String category) {
        smtpApi.addCategory(category);
        return this;
    }

    @Override
    public Map<String, String> getUniqueArgs() {
        return smtpApi.getUniqueArgs();
    }

    @Override
    public Email setUniqueArgs(Map<String, String> args) {
        smtpApi.setUniqueArgs(args);
        return this;
    }

    @Override
    public String getUniqueArg(String key) {
        return smtpApi.getUniqueArg(key);
    }

    @Override
    public Email setUniqueArg(String key, String val) {
        smtpApi.setUniqueArg(key, val);
        return this;
    }

    @Override
    public Map<String, String> getSections() {
        return smtpApi.getSections();
    }

    @Override
    public Email setSections(Map<String, String> sections) {
        smtpApi.setSections(sections);
        return this;
    }

    @Override
    public String getSection(String key) {
        return smtpApi.getSection(key);
    }

    @Override
    public Email setSection(String key, String val) {
        smtpApi.setSection(key, val);
        return this;
    }

    @Override
    public Map<String, List<String>> getSubstitutions() {
        return smtpApi.getSubstitutions();
    }

    @Override
    public Email setSubstitutions(Map<String, List<String>> substitutions) {
        smtpApi.setSubstitutions(substitutions);
        return this;
    }

    @Override
    public List<String> getSubstitution(String key) {
        return smtpApi.getSubstitution(key);
    }

    @Override
    public Email setSubstitution(String key, List<String> vals) {
        smtpApi.setSubstitution(key, vals);
        return this;
    }

    @Override
    public Email addValueToSubstitution(String key, String val) {
        smtpApi.addValueToSubstitution(key, val);
        return this;
    }

    @Override
    public Map<String, Map<String, Object>> getFilters() {
        return smtpApi.getFilters();
    }

    @Override
    public Email setFilters(Map<String, Map<String, Object>> filters) throws SmtpApiException {
        smtpApi.setFilters(filters);
        return this;
    }

    @Override
    public Map<String, Object> getFilter(String filterName) {
        return smtpApi.getFilter(filterName);
    }

    @Override
    public Email setFilter(String filterName, Map<String, Object> filter) throws SmtpApiException {
        smtpApi.setFilter(filterName, filter);
        return this;
    }

    @Override
    public Email setSettingInFilter(String filterName, String settingName, Object settingVal) throws SmtpApiException {
        smtpApi.setSettingInFilter(filterName, settingName, settingVal);
        return this;
    }

    // Override accept from SendGridModel - Email needs custom HttpEntity construction

    @Override
    public void accept(SendGridModelVisitor visitor) {
        visitor.visit(this);
    }
}
