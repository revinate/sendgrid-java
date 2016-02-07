package com.revinate.sendgrid;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class BaseSendGridTest {

    protected String readFile(String path) throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream(path));
    }
}
