package com.revinate.sendgrid.net;

import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class MultipartHttpEntityBuilder extends HttpEntityBuilder {

    private static final String PARAM_TO = "to[]";
    private static final String PARAM_TONAME = "toname[]";
    private static final String PARAM_CC = "cc[]";
    private static final String PARAM_CCNAME = "ccname[]";
    private static final String PARAM_BCC = "bcc[]";
    private static final String PARAM_BCCNAME = "bccname[]";
    private static final String PARAM_FROM = "from";
    private static final String PARAM_FROMNAME = "fromname";
    private static final String PARAM_REPLYTO = "replyto";
    private static final String PARAM_SUBJECT = "subject";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_HTML = "html";
    private static final String PARAM_FILES = "files[%s]";
    private static final String PARAM_CONTENTS = "content[%s]";
    private static final String PARAM_HEADERS = "headers";
    private static final String PARAM_XSMTPAPI = "x-smtpapi";

    private static final ContentType TEXT_PLAIN_UTF8 = ContentType.create("text/plain", "UTF-8");

    @Override
    public HttpEntity build() throws IOException {
        if (email != null) {
            return buildEmail();
        } else if (map != null) {
            return buildMap();
        } else if (model != null || list != null) {
            throw new IOException("Content is of unsupported type");
        }

        throw new IOException("Content is null");
    }

    private HttpEntity buildMap() {
        MultipartEntityBuilder builder = multipartEntityBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue().toString());
        }
        return builder.build();
    }

    private HttpEntity buildEmail() {
        MultipartEntityBuilder builder = multipartEntityBuilder();

        // If SMTP API Header is used, To is still required. #workaround.
        if (email.getTos().isEmpty()) {
            addTextBody(builder, PARAM_TO, email.getFrom());
        }

        addTextBodies(builder, PARAM_TO, email.getTos());
        addTextBodies(builder, PARAM_TONAME, email.getToNames());
        addTextBodies(builder, PARAM_CC, email.getCcs());
        addTextBodies(builder, PARAM_CCNAME, email.getCcNames());
        addTextBodies(builder, PARAM_BCC, email.getBccs());
        addTextBodies(builder, PARAM_BCCNAME, email.getBccNames());
        addTextBody(builder, PARAM_FROM, email.getFrom());
        addTextBody(builder, PARAM_FROMNAME, email.getFromName());
        addTextBody(builder, PARAM_REPLYTO, email.getReplyTo());
        addTextBody(builder, PARAM_SUBJECT, email.getSubject());
        addTextBody(builder, PARAM_TEXT, email.getText());
        addTextBody(builder, PARAM_HTML, email.getHtml());

        for (Map.Entry<String, InputStream> entry : email.getAttachments().entrySet()) {
            builder.addBinaryBody(String.format(PARAM_FILES, entry.getKey()), entry.getValue());
        }

        for (Map.Entry<String, String> entry : email.getContentIds().entrySet()) {
            builder.addTextBody(String.format(PARAM_CONTENTS, entry.getKey()), entry.getValue());
        }

        String headers = email.toHeaders();
        if (!"{}".equals(headers)) {
            builder.addTextBody(PARAM_HEADERS, headers, TEXT_PLAIN_UTF8);
        }

        String smtpApiHeader = email.toSmtpApiHeader();
        if (!"{}".equals(smtpApiHeader)) {
            builder.addTextBody(PARAM_XSMTPAPI, smtpApiHeader, TEXT_PLAIN_UTF8);
        }

        return builder.build();
    }

    private MultipartEntityBuilder multipartEntityBuilder() {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (credential != null && credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            builder.addTextBody("api_user", usernamePasswordCredential.getUsername());
            builder.addTextBody("api_key", usernamePasswordCredential.getPassword());
        }

        return builder;
    }

    private void addTextBodies(MultipartEntityBuilder builder, String name, List<String> texts) {
        for (String text : texts) {
            addTextBody(builder, name, text);
        }
    }

    private void addTextBody(MultipartEntityBuilder builder, String name, String text) {
        if (text != null && !text.isEmpty()) {
            builder.addTextBody(name, text, TEXT_PLAIN_UTF8);
        }
    }
}
