# SendGrid Java Bindings

[![BuildStatus](https://travis-ci.org/revinate/sendgrid-java.svg?branch=master)](https://travis-ci.org/revinate/sendgrid-java)
[![BuildStatus](https://maven-badges.herokuapp.com/maven-central/com.revinate/sendgrid-java/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.revinate/sendgrid-java)

This library allows Java developers to easily send emails through SendGrid and
programmatically manage their SendGrid accounts.

Visit [https://sendgrid.com](https://sendgrid.com) to create a SendGrid account.

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

To use this library, first create a new `SendGrid` object with your SendGrid API key:

```java
SendGrid sendGrid = SendGrid.create("API_KEY").build();
```

If you don't have an API key, you can use your SendGrid username and password instead.
You'll see how to create an API key using this library later in this document.

```java
SendGrid sendGrid = SendGrid.create("USERNAME", "PASSWORD").build();
```

Behid the scenes, this library connects to the SendGrid APIs via HTTP. You can
optionally specify the size of the HTTP connection pool:

```java
SendGrid sendGrid = SendGrid.create("API_KEY").setMaxConnections(100).build();
```

Alternatively, you can supply your own HttpComponents `CloseableHttpClient`. This is useful,
for example, if you want to specify a proxy server:

```java
HttpHost proxy = new HttpHost("server", 8000);
CloseableHttpClient client = HttpClients.custom().setProxy(proxy).setUserAgent(SendGrid.USER_AGENT).build();
SendGrid sendGrid = SendGrid.create("API_KEY").setHttpClient(client).build();
```

### Using the library

The `SendGrid` object has a number of methods, each corresponding to a resource
you can interact with. Examples of resources are API keys, subusers, and IPs.
Methods on a resource correspond to the operations you can perform on that resource.
Common operations include:

- `list` - retrieve all entities under this resource in your account
- `create` - add a new entity under this resource to your account

For example:

```java
List<ApiKey> apiKeys = sendGrid.apiKeys().list();
ApiKey apiKey = new ApiKey("My API Key");
ApiKey result = sendGrid.apiKeys().create(apiKey);
```

Some resources have subresources nested under them. Resources that correspond to
a collection of entities, such as the API keys resource, each have a subresource
that corresponds to a single entity in the collection. These collection-type
resources have an `entity` method that returns the subresource. The `entity` method
takes a single argument, which can be either the entity identifier or the entity itself.
For example:

```java
sendGrid.apiKeys().entity("api.key.id");
```

The `SendGrid` object also has some convenience methods that return some subresources
directly, for example:

```java
sendGrid.apiKey("api.key.id");
```

Common operations for resources that correspond to single entities include:

- `retrieve` - retrieve the entity
- `update` - update the entity
- `partialUpdate` - update some fields of the entity
- `delete` - delete the entity from your account

For example:

```java
ApiKey apiKey = sendGrid.apiKey("api.key.id").retrieve();
sendGrid.apiKey(apiKey).delete();
```

Not every resource support all of the above operations. And some resources support
additional operations or have additional subresources. More detailed examples of
performing operations on each resource supported by this library can be found
later in this document.

### Acting on behalf of subusers

If the library was initialized with the credentials of a parent SendGrid account,
it's possible to use the same `SendGrid` object to perform operations on behalf of any
of the subusers under the parent account. This is useful, for example, if you would
like to create some API keys for a newly created subuser. For certain API operations,
such as adding an IP to a subuser's IP pool, it is necessary to use the parent account
credentials to initialize the library and invoke the operations on behalf of the subuser.

To act on behalf of a subuser, call `onBehalfOf("SUBUSERNAME")` on the `SendGrid`
object before invoking the action. For example:

```java
ApiKey apiKey = new ApiKey("Subuser API Key");
ApiKey result = sendGrid.onBehalfOf("subuser1").apiKeys().create(apiKey);
List<ApiKey> apiKeys = sendGrid.onBehalfOf("subuser1").apiKeys().list();
sendGrid.onBehalfOf("subuser1").apiKey(result).delete();
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

##### Custom SMTP headers

```java
email.setHeader("x-custom-header-1", "example");
```

#### [SendGrid SMTP API](http://sendgrid.com/docs/API_Reference/SMTP_API/index.html)

The `Email` object implements the SendGrid SMTP API interface and supports all the SMTP
API operations. For the full list of supported operations, refer to the
[SendGrid SMTP API Java Bindings](https://github.com/revinate/sendgrid-smtpapi-java) project.

##### Template ID

The template filter can be configured with a template ID using the following convenience method:

```java
email.setTemplateId("abc123-def456");
```

### Managing API keys

```java
// retrieve existing API keys
List<ApiKey> apiKeys = sendGrid.apiKeys().list();

// retrieve a single API key
ApiKey existing = sendGrid.apiKey("api.key.id").retrieve();

// create new API key, the result contains the API key value
ApiKey apiKey = new ApiKey("Mail API Key");
apiKey.addScope("mail.send");
ApiKey result = sendGrid.apiKeys().create(apiKey);

// update API key
result.addScope("stats.read");
sendGrid.apiKey(result).update(result);

// partially update API key
Map<String, String> request = Collections.singletonMap("name", "Mail and Stats API Key");
sendGrid.apiKey(result).partialUpdate(request);

// delete API key
sendGrid.apiKey(result).delete();
```

### Managing subusers

```java
// retrieve existing subusers
List<Subuser> subusers = sendGrid.subusers().list();

// create new subuser
Subuser subuser = new Subuser("subuser1", "subuser1@email.com", "secretpassword");
subuser.addIp("127.0.0.1");
sendGrid.subusers().create(subuser);

// create subuser monitor
Monitor monitor = new Monitor("monitor@email.com", 5000);
sendGrid.subuser("subuser1").monitor().create(monitor);

// delete subuser monitor
sendGrid.subuser("subuser1").monitor().delete();

// delete subuser
sendGrid.subuser("subuser1").delete();
```

### Managing IPs and IP pools

```java
// retrieve existing IPs
List<Ip> ips = sendGrid.ips().list();
Ip ip = ips.get(0);

// retrieve existing IP pools under the subuser
List<IpPool> pools = sendGrid.onBehalfOf("subuser1").ipPools().list();

// create new IP pool for subuser
IpPool pool = new IpPool("transactional");
sendGrid.onBehalfOf("subuser1").ipPools().create(pool);

// add IP to IP pool
sendGrid.onBehalfOf("subuser1").ipPool(pool).ips().create(ip);

// remove IP from IP pool
sendGrid.onBehalfOf("subuser1").ipPool(pool).ip(ip).delete();

// delete IP pool
sendGrid.onBehalfOf("subuser1").ipPool(pool).delete();
```

### Managing domain whitelabels

```java
// create new domain whitelabel
Whitelabel whitelabel = new Whitelabel("email.com", "em");
whitelabel.setAutomaticSecurity(true);
whitelabel.setDefault(true);
Whitelabel result = sendGrid.domainWhitelabels().create(whitelabel);

// validate domain whitelabel
WhitelabelValidation validation = sendGrid.domainWhitelabel(result).validate();
```

### Managing link whitelabels

```java
// create new link whitelabel
Whitelabel whitelabel = new Whitelabel("email.com", "click");
whitelabel.setDefault(true);
Whitelabel result = sendGrid.linkWhitelabels().create(whitelabel);

// validate link whitelabel
WhitelabelValidation validation = sendGrid.linkWhitelabel(result).validate();
```

### Managing mail settings

```java
// retrieve all mail settings
List<MailSetting> settings = sendGrid.mailSettings().list();

// update spam forwarding setting
MailSetting setting = new MailSetting("forward_spam", true, "spam@email.com");
sendGrid.mailSetting(setting).update(setting);
```

### Managing event webhook settings

```java
EventWebhookSettings settings = new EventWebhookSettings("http://event.webhook.receiver", true);
sendGrid.eventWebhookSettings().update(settings);
```

### Retrieving account reputation

```java
Account parentAccount = sendGrid.account().retrieve();
Account subuserAccount = sendGrid.onBehalfOf("subuser1").account().retrieve();
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

This library is under the MIT License.
