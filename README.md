# SendGrid Java Bindings

This library allows you to easily interact with the SendGrid web APIs from Java
code. Its features include email sending, subuser management, IP management, API
key management, domain and link whitelabeling, mail settings, and webhook settings.

Visit [https://sendgrid.com](https://sendgrid.com) to create a SendGrid account.

[![BuildStatus](https://travis-ci.org/revinate/sendgrid-java.svg?branch=master)](https://travis-ci.org/revinate/sendgrid-java)
[![BuildStatus](https://maven-badges.herokuapp.com/maven-central/com.revinate/sendgrid-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.revinate/sendgrid-java)

## Requirements

Java 1.6 and later.

## Installation

### Maven

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.revinate</groupId>
  <artifactId>sendgrid-java</artifactId>
  <version>3.0.0</version>
</dependency>
```

### Gradle

Add this dependency to your project's build script:

```groovy
compile 'com.revinate:sendgrid-java:3.0.0'
```

## Usage

### Quick start

```java
// SendGridExample.java
import com.revinate.sendgrid.SendGrid;
import com.revinate.sendgrid.exception.*;
import com.revinate.sendgrid.model.*;

public class SendGridExample {
    public static void main(String[] args) {
        SendGrid sendGrid = SendGrid.create("API_KEY").build();

        Email email = new Email();
        email.addTo("example@email.com");
        email.setFrom("test@email.com");
        email.setSubject("Hello World");
        email.setText("My first email with SendGrid Java.");

        try {
            Response response = sendGrid.mail().send(email);
            System.out.println(response);
        } catch (SendGridException e) {
            e.printStackTrace();
        }
    }
}
```

### Initializing the library

To use this library, create a new SendGrid object with your username/password or
your API key:

```java
SendGrid sendGrid = SendGrid.create("API_KEY").build();
```

```java
SendGrid sendGrid = SendGrid.create("USERNAME", "PASSWORD").build();
```

You can optionally specify the size of the HTTP connection pool used to connect
to the SendGrid API:

```java
SendGrid sendGrid = SendGrid.create("API_KEY").setMaxConnections(100).build();
```

Alternatively, you can supply your own Apache `CloseableHttpClient`. This is useful,
for example, if you want to specify a proxy server:

```java
HttpHost proxy = new HttpHost("server", 8000);
CloseableHttpClient client = HttpClients.custom().setProxy(proxy).setUserAgent(SendGrid.USER_AGENT).build();
SendGrid sendGrid = SendGrid.create("API_KEY").setHttpClient(client).build();
```

### Sending email

```java
Email email = new Email();
email.addTo("example@email.com", "Example User");
email.setFrom("test@email.com", "Test User");
email.setSubject("Hello Again");
email.setText("My second email with SendGrid Java.");
sendGrid.mail().send(email);
```

#### Email fields

Many methods are available on the [Email](src/main/java/com/revinate/sendgrid/model/Email.java)
object to set the fields of the email:

##### Recipients

```java
email.addTo("example@email.com");
email.addToName("Example User");

email.addTo("example@email.com", "Example User");

email.setTos(Arrays.asList("example1@email.com", "example2@email.com"));
email.setToNames(Arrays.asList("Example One", "Example Two"))

email.addCc("example@email.com");
email.addCc("example@email.com", "Example User");

email.addBcc("example@email.com");
email.addBcc("example@email.com", "Example User");
```

##### Sender

```java
email.setFrom("test@email.com");
email.setFromName("Test User");

email.setFrom("test@email.com", "Test User");

email.setReplyTo("no-reply@email.com");
```

##### Subject

```java
email.setSubject("Hello World");
```

##### Contents

```java
email.setText("This is some text of the email.");
email.setHtml("<h1>This is some text of the email.</h1>");
```

##### Attachments

```java
email.setAttachment("text.txt", "contents");
email.setAttachment("image.png", new File("./image.png"));
email.setAttachment("document.pdf", new InputStream(new File("./document.pdf")));
```

##### Content IDs

Attachments can be inlined in the email using content IDs:

```java
email.setAttachment("image.png", new File("./image.png"), "ID_IN_HTML");
email.setHtml("<html><body><img src=\"cid:ID_IN_HTML\"></img></body></html>")
```

#### [SMTP API](http://sendgrid.com/docs/API_Reference/SMTP_API/index.html)

The email object implements the SMTP API interface and supports all the SMTP
API operations. For the full list of supported operations, refer to the
[SMTP API Java Bindings](https://github.com/revinate/sendgrid-smtpapi-java) project.

##### Template ID

The template filter can be configured using the following convenience method:

```java
email.setTemplateId("abc123-def456");
```

### Managing subusers

This library makes it easy to list, create, update, and delete subusers.

```java
List<Subuser> subusers = sendGrid.subusers().list();

Subuser subuser = new Subuser("test", "test@mail.com", "secretpassword");
subuser.addIp("127.0.0.1");
sendGrid.subusers().create(subuser);

sendGrid.subuser("test").delete();
```

### Managing API keys

```java
List<ApiKey> apiKeys = sendGrid.apiKeys().list();

ApiKey apiKey = new ApiKey("testapikey");
apiKey.addScope("mail.send");

ApiKey result = sendGrid.apiKeys().create(apiKey);

sendGrid.apiKey(result).delete();
```

### Managing IP addresses and IP pools

```java
List<Ip> ips = sendGrid.ips().list();

List<IpPool> ipPools = sendGrid.ipPools().list();

sendGrid.ipPool("transactional").ips().create(ips.get(0));
```

### Domain and link whitelabeling

```java
Whitelabel whitelabel = new Whitelabel("email.com", "em");
whitelabel.setAutomaticSecurity(true);
whitelabel.setDefault(true);

Whitelabel result = sendGrid.domainWhitelabels().create(whitelabel);

WhitelabelValidation validation = sendGrid.domainWhitelabel(result).validate();
```

### Mail and webhook settings

```java
Monitor monitor = new Monitor("monitor@email.com", 5000);

sendGrid.subuser("subuser1").monitor().create(monitor);

MailSetting setting = new MailSetting("forward_spam", true, "spam@email.com");

sendGrid.mailSetting(setting).update(setting);

EventWebhookSettings settings = new EventWebhookSettings("http://event.webhook.receiver", true);

sendGrid.eventWebhookSettings().update(settings);
```

### Account info and reputation

```java
Account account = sendGrid.account().retrieve();
```

### Acting on behalf of subusers

If the library was initialized with the credentials for a parent SendGrid account,
it's possible to use the same SendGrid object to perform actions on behalf of any
of the subusers under the parent account. This is useful, for example, if you would
like to create some API keys for a newly created subuser. For certain API operations,
such as managing the IP pools for subusers, using parent user credentials but acting
on behalf of the subuser is necessary.

```java
List<ApiKey> apiKeys = sendGrid.onBehalfOf("subuser1").apiKeys().list();

ApiKey result = sendGrid.onBehalfOf("subuser1").apiKeys().create(apiKey);

sendGrid.onBehalfOf("subuser1").apiKey(result).delete();

IpPool ipPool = sendGrid.onBehalfOf("subuser1").ipPool("transactional").retrieve();
```

## Testing

The tests in the [src/test](src/test) directory can be run using Gradle with the following command:

```bash
$ ./gradlew test
```

The tests in the file [ApiTest.java](src/test/java/com/revinate/sendgrid/ApiTest.java)
are designed to be run against the live SendGrid API. They are disabled unless a
environment variable named `SENDGRID_API_KEY` is set. To enable these tests, set the
`SENDGRID_API_KEY` environment variable to your SendGrid API key before runing the
Gradle task. **Caution:** these tests make several assumptions about the state
of your SendGrid account and perform many modifications to the account. They
should not be enabled unless you have a thorough understanding of the actions they
perform, and have made adjustments to the tests to allow them to work with your
live account.

## License

Licensed under the MIT License.
