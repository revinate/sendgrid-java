package com.revinate.sendgrid.model;

import com.revinate.sendgrid.net.auth.Credential;
import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import com.revinate.sendgrid.smtpapi.SMTPAPI;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class Email {

    private static final String PARAM_TO = "to[]";
    private static final String PARAM_TONAME = "toname[]";
    private static final String PARAM_CC = "cc[]";
    private static final String PARAM_BCC = "bcc[]";

    private static final String PARAM_FROM = "from";
    private static final String PARAM_FROMNAME = "fromname";
    private static final String PARAM_REPLYTO = "replyto";
    private static final String PARAM_SUBJECT = "subject";
    private static final String PARAM_HTML = "html";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_FILES = "files[%s]";
    private static final String PARAM_CONTENTS = "content[%s]";
    private static final String PARAM_XSMTPAPI = "x-smtpapi";
    private static final String PARAM_HEADERS = "headers";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String UTF_8 = "UTF-8";

    private SMTPAPI smtpapi;
    private List<String> to;
    private List<String> toname;
    private List<String> cc;
    private String from;
    private String fromname;
    private String replyto;
    private String subject;
    private String text;
    private String html;
    private List<String> bcc;
    private Map<String, InputStream> attachments;
    private Map<String, String> contents;
    private Map<String, String> headers;

    public Email() {
        this.smtpapi = new SMTPAPI();
        this.to = new ArrayList<String>();
        this.toname = new ArrayList<String>();
        this.cc = new ArrayList<String>();
        this.bcc = new ArrayList<String>();
        this.attachments = new HashMap<String, InputStream>();
        this.contents = new HashMap<String, String>();
        this.headers = new HashMap<String, String>();
    }

    public Email addTo(String to) {
        this.to.add(to);
        return this;
    }

    public Email addTo(String[] tos) {
        this.to.addAll(Arrays.asList(tos));
        return this;
    }

    public Email addTo(String to, String name) {
        this.addTo(to);
        return this.addToName(name);
    }

    public Email setTo(String[] tos) {
        this.to = new ArrayList<String>(Arrays.asList(tos));
        return this;
    }

    public String[] getTos() {
        return this.to.toArray(new String[this.to.size()]);
    }

    public Email addSmtpApiTo(String to) {
        this.smtpapi.addTo(to);
        return this;
    }

    public Email addSmtpApiTo(String[] to) {
        this.smtpapi.addTos(to);
        return this;
    }

    public Email addToName(String toname) {
        this.toname.add(toname);
        return this;
    }

    public Email addToName(String[] tonames) {
        this.toname.addAll(Arrays.asList(tonames));
        return this;
    }

    public Email setToName(String[] tonames) {
        this.toname = new ArrayList<String>(Arrays.asList(tonames));
        return this;
    }

    public String[] getToNames() {
        return this.toname.toArray(new String[this.toname.size()]);
    }

    public Email addCc(String cc) {
        this.cc.add(cc);
        return this;
    }

    public Email addCc(String[] ccs) {
        this.cc.addAll(Arrays.asList(ccs));
        return this;
    }

    public Email setCc(String[] ccs) {
        this.cc = new ArrayList<String>(Arrays.asList(ccs));
        return this;
    }

    public String[] getCcs() {
        return this.cc.toArray(new String[this.cc.size()]);
    }

    public Email setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getFrom() {
        return this.from;
    }

    public Email setFromName(String fromname) {
        this.fromname = fromname;
        return this;
    }

    public String getFromName() {
        return this.fromname;
    }

    public Email setReplyTo(String replyto) {
        this.replyto = replyto;
        return this;
    }

    public String getReplyTo() {
        return this.replyto;
    }

    public Email addBcc(String bcc) {
        this.bcc.add(bcc);
        return this;
    }

    public Email addBcc(String[] bccs) {
        this.bcc.addAll(Arrays.asList(bccs));
        return this;
    }

    public Email setBcc(String[] bccs) {
        this.bcc = new ArrayList<String>(Arrays.asList(bccs));
        return this;
    }

    public String[] getBccs() {
        return this.bcc.toArray(new String[this.bcc.size()]);
    }

    public Email setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getSubject() {
        return this.subject;
    }

    public Email setText(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public Email setHtml(String html) {
        this.html = html;
        return this;
    }

    public String getHtml() {
        return this.html;
    }

    public Email addSubstitution(String key, String[] val) {
        this.smtpapi.addSubstitutions(key, val);
        return this;
    }

    public JSONObject getSubstitutions() {
        return this.smtpapi.getSubstitutions();
    }

    public Email addUniqueArg(String key, String val) {
        this.smtpapi.addUniqueArg(key, val);
        return this;
    }

    public JSONObject getUniqueArgs() {
        return this.smtpapi.getUniqueArgs();
    }

    public Email addCategory(String category) {
        this.smtpapi.addCategory(category);
        return this;
    }

    public String[] getCategories() {
        return this.smtpapi.getCategories();
    }

    public Email addSection(String key, String val) {
        this.smtpapi.addSection(key, val);
        return this;
    }

    public JSONObject getSections() {
        return this.smtpapi.getSections();
    }

    public Email addFilter(String filterName, String parameterName, String parameterValue) {
        this.smtpapi.addFilter(filterName, parameterName, parameterValue);
        return this;
    }

    public JSONObject getFilters() {
        return this.smtpapi.getFilters();
    }

    public Email setASMGroupId(int val) {
        this.smtpapi.setASMGroupId(val);
        return this;
    }

    public Integer getASMGroupId() {
        return this.smtpapi.getASMGroupId();
    }

    public Email setSendAt(int sendAt) {
        this.smtpapi.setSendAt(sendAt);
        return this;
    }

    public int getSendAt() {
        return this.smtpapi.getSendAt();
    }

    public Email setIpPool(String ipPool) {
        this.smtpapi.setIpPool(ipPool);
        return this;
    }

    public String getIpPool() {
        return this.smtpapi.getIpPool();
    }

    /**
     * Convenience method to set the template
     *
     * @param templateId The ID string of your template
     * @return this
     */
    public Email setTemplateId(String templateId) {
        this.getSMTPAPI().addFilter("templates", "enable", 1);
        this.getSMTPAPI().addFilter("templates", "template_id", templateId);
        return this;
    }

    public Email addAttachment(String name, File file) throws IOException {
        return this.addAttachment(name, new FileInputStream(file));
    }

    public Email addAttachment(String name, String file) throws IOException {
        return this.addAttachment(name, new ByteArrayInputStream(file.getBytes()));
    }

    public Email addAttachment(String name, InputStream file) {
        this.attachments.put(name, file);
        return this;
    }

    public Map<String, InputStream> getAttachments() {
        return this.attachments;
    }

    public Email addContentId(String attachmentName, String cid) {
        this.contents.put(attachmentName, cid);
        return this;
    }

    public Map<String, String> getContentIds() {
        return this.contents;
    }

    public Email addHeader(String key, String val) {
        this.headers.put(key, val);
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public SMTPAPI getSMTPAPI() {
        return this.smtpapi;
    }

    public HttpEntity toHttpEntity(Credential credential) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (credential != null && credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            builder.addTextBody("api_user", usernamePasswordCredential.getUsername());
            builder.addTextBody("api_key", usernamePasswordCredential.getPassword());
        }

        String[] tos = getTos();
        String[] tonames = getToNames();
        String[] ccs = getCcs();
        String[] bccs = getBccs();

        // If SMTPAPI Header is used, To is still required. #workaround.
        if (tos.length == 0) {
            builder.addTextBody(PARAM_TO, getFrom(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        for (String to : tos) {
            builder.addTextBody(PARAM_TO, to, ContentType.create(TEXT_PLAIN, UTF_8));
        }

        for (String toname : tonames) {
            builder.addTextBody(PARAM_TONAME, toname, ContentType.create(TEXT_PLAIN, UTF_8));
        }

        for (String cc : ccs) {
            builder.addTextBody(PARAM_CC, cc, ContentType.create(TEXT_PLAIN, UTF_8));
        }

        for (String bcc : bccs) {
            builder.addTextBody(PARAM_BCC, bcc, ContentType.create(TEXT_PLAIN, UTF_8));
        }

        // Files
        if (getAttachments().size() > 0) {
            for (Map.Entry<String, InputStream> entry : getAttachments().entrySet()) {
                builder.addBinaryBody(String.format(PARAM_FILES, entry.getKey()), entry.getValue());
            }
        }

        if (getContentIds().size() > 0) {
            for (Map.Entry<String, String> entry : getContentIds().entrySet()) {
                builder.addTextBody(String.format(PARAM_CONTENTS, entry.getKey()), entry.getValue());
            }
        }

        if (getHeaders().size() > 0) {
            builder.addTextBody(PARAM_HEADERS, new JSONObject(getHeaders()).toString(),
                    ContentType.create(TEXT_PLAIN, UTF_8));
        }

        if (getFrom() != null && !getFrom().isEmpty()) {
            builder.addTextBody(PARAM_FROM, getFrom(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        if (getFromName() != null && !getFromName().isEmpty()) {
            builder.addTextBody(PARAM_FROMNAME, getFromName(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        if (getReplyTo() != null && !getReplyTo().isEmpty()) {
            builder.addTextBody(PARAM_REPLYTO, getReplyTo(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        if (getSubject() != null && !getSubject().isEmpty()) {
            builder.addTextBody(PARAM_SUBJECT, getSubject(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        if (getHtml() != null && !getHtml().isEmpty()) {
            builder.addTextBody(PARAM_HTML, getHtml(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        if (getText() != null && !getText().isEmpty()) {
            builder.addTextBody(PARAM_TEXT, getText(), ContentType.create(TEXT_PLAIN, UTF_8));
        }

        String tmpString = smtpapi.jsonString();
        if (!tmpString.equals("{}")) {
            builder.addTextBody(PARAM_XSMTPAPI, tmpString, ContentType.create(TEXT_PLAIN, UTF_8));
        }

        return builder.build();
    }
}
