package com.revinate.sendgrid.net;

import com.revinate.sendgrid.net.auth.UsernamePasswordCredential;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
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
    private static final String PARAM_HTML = "html";
    private static final String PARAM_TEXT = "text";
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

    @Override
    public List<Header> getHeaders() {
        return Collections.emptyList();
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

    private HttpEntity buildMap() {
        MultipartEntityBuilder builder = multipartEntityBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue().toString());
        }
        return builder.build();
    }

    private HttpEntity buildEmail() {
        MultipartEntityBuilder builder = multipartEntityBuilder();

        String[] tos = email.getTos();
        String[] tonames = email.getToNames();
        String[] ccs = email.getCcs();
        String[] bccs = email.getBccs();

        // If SMTPAPI Header is used, To is still required. #workaround.
        if (tos.length == 0) {
            builder.addTextBody(PARAM_TO, email.getFrom(), TEXT_PLAIN_UTF8);
        }

        for (String to : tos) {
            builder.addTextBody(PARAM_TO, to, TEXT_PLAIN_UTF8);
        }

        for (String toname : tonames) {
            builder.addTextBody(PARAM_TONAME, toname, TEXT_PLAIN_UTF8);
        }

        for (String cc : ccs) {
            builder.addTextBody(PARAM_CC, cc, TEXT_PLAIN_UTF8);
        }

        for (String bcc : bccs) {
            builder.addTextBody(PARAM_BCC, bcc, TEXT_PLAIN_UTF8);
        }

        // Files
        if (email.getAttachments().size() > 0) {
            for (Map.Entry<String, InputStream> entry : email.getAttachments().entrySet()) {
                builder.addBinaryBody(String.format(PARAM_FILES, entry.getKey()), entry.getValue());
            }
        }

        if (email.getContentIds().size() > 0) {
            for (Map.Entry<String, String> entry : email.getContentIds().entrySet()) {
                builder.addTextBody(String.format(PARAM_CONTENTS, entry.getKey()), entry.getValue());
            }
        }

        if (email.getHeaders().size() > 0) {
            builder.addTextBody(PARAM_HEADERS, new JSONObject(email.getHeaders()).toString(), TEXT_PLAIN_UTF8);
        }

        if (email.getFrom() != null && !email.getFrom().isEmpty()) {
            builder.addTextBody(PARAM_FROM, email.getFrom(), TEXT_PLAIN_UTF8);
        }

        if (email.getFromName() != null && !email.getFromName().isEmpty()) {
            builder.addTextBody(PARAM_FROMNAME, email.getFromName(), TEXT_PLAIN_UTF8);
        }

        if (email.getReplyTo() != null && !email.getReplyTo().isEmpty()) {
            builder.addTextBody(PARAM_REPLYTO, email.getReplyTo(), TEXT_PLAIN_UTF8);
        }

        if (email.getSubject() != null && !email.getSubject().isEmpty()) {
            builder.addTextBody(PARAM_SUBJECT, email.getSubject(), TEXT_PLAIN_UTF8);
        }

        if (email.getHtml() != null && !email.getHtml().isEmpty()) {
            builder.addTextBody(PARAM_HTML, email.getHtml(), TEXT_PLAIN_UTF8);
        }

        if (email.getText() != null && !email.getText().isEmpty()) {
            builder.addTextBody(PARAM_TEXT, email.getText(), TEXT_PLAIN_UTF8);
        }

        String tmpString = email.getSMTPAPI().jsonString();
        if (!tmpString.equals("{}")) {
            builder.addTextBody(PARAM_XSMTPAPI, tmpString, TEXT_PLAIN_UTF8);
        }

        return builder.build();
    }
}
