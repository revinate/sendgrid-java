package com.revinate.sendgrid;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.fail;

public class SendGridTest {

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Test
    public void testBuildGradleVersion() {
        try {
            SendGrid client = new SendGrid(USERNAME, PASSWORD);
            BufferedReader br = new BufferedReader(new FileReader("./build.gradle"));
            String line = br.readLine();
            String regex = "version\\s*=\\s*'" + client.getVersion() + "'";

            while (line != null) {
                if (line.matches(regex)) {
                    br.close();
                    return;
                }
                line = br.readLine();
            }
            br.close();
            fail("build.gradle version does not match");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUsernamePasswordConstructor() {
        SendGrid client = new SendGrid(USERNAME, PASSWORD);
    }

    @Test
    public void testApiKeyConstructor() {
        SendGrid client = new SendGrid(PASSWORD);
    }
}
