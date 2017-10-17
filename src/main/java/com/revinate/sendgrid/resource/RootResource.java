package com.revinate.sendgrid.resource;

import com.revinate.sendgrid.model.*;
import com.revinate.sendgrid.net.SendGridHttpClient;
import com.revinate.sendgrid.net.auth.Credential;

public class RootResource extends SendGridResource {

    public RootResource(String baseUrl, SendGridHttpClient client, Credential credential) {
        super(baseUrl, client, credential);
    }

    public AccountResource account() {
        return new AccountResource(getApiUrl(AccountResource.API_VERSION), client, credential);
    }

    public ApiKeysResource apiKeys() {
        return new ApiKeysResource(getApiUrl(ApiKeysResource.API_VERSION), client, credential);
    }

    public ApiKeyResource apiKey(ApiKey apiKey) {
        return apiKeys().entity(apiKey);
    }

    public ApiKeyResource apiKey(String id) {
        return apiKeys().entity(id);
    }

    public DomainWhitelabelsResource domainWhitelabels() {
        return new DomainWhitelabelsResource(getApiUrl(DomainWhitelabelsResource.API_VERSION), client, credential);
    }

    public DomainWhitelabelResource domainWhitelabel(Whitelabel whitelabel) {
        return domainWhitelabels().entity(whitelabel);
    }

    public DomainWhitelabelResource domainWhitelabel(String id) {
        return domainWhitelabels().entity(id);
    }

    public IpWhitelabelsResource ipWhitelabels() {
        return new IpWhitelabelsResource(getApiUrl(IpWhitelabelsResource.API_VERSION), client, credential);
    }

    public IpWhitelabelResource ipWhitelabel(IpWhitelabel ipWhitelabel) {
        return ipWhitelabels().entity(ipWhitelabel);
    }

    public IpWhitelabelResource ipWhitelabel(String id) { return ipWhitelabels().entity(id); }


    public EventWebhookSettingsResource eventWebhookSettings() {
        return new EventWebhookSettingsResource(getApiUrl(EventWebhookSettingsResource.API_VERSION), client, credential);
    }

    public IpsResource ips() {
        return new IpsResource(getApiUrl(IpsResource.API_VERSION), client, credential);
    }

    public IpResource ip(Ip ip) {
        return ips().entity(ip);
    }

    public IpResource ip(String id) {
        return ips().entity(id);
    }

    public IpPoolsResource ipPools() {
        return new IpPoolsResource(getApiUrl(IpPoolsResource.API_VERSION), client, credential);
    }

    public IpPoolResource ipPool(IpPool ipPool) {
        return ipPools().entity(ipPool);
    }

    public IpPoolResource ipPool(String id) {
        return ipPools().entity(id);
    }

    public LinkWhitelabelsResource linkWhitelabels() {
        return new LinkWhitelabelsResource(getApiUrl(LinkWhitelabelsResource.API_VERSION), client, credential);
    }

    public LinkWhitelabelResource linkWhitelabel(Whitelabel whitelabel) {
        return linkWhitelabels().entity(whitelabel);
    }

    public LinkWhitelabelResource linkWhitelabel(String id) {
        return linkWhitelabels().entity(id);
    }

    public MailResource mail() {
        return new MailResource(getApiUrl(MailResource.API_VERSION), client, credential);
    }

    public MailSettingsResource mailSettings() {
        return new MailSettingsResource(getApiUrl(MailSettingsResource.API_VERSION), client, credential);
    }

    public MailSettingResource mailSetting(MailSetting setting) {
        return mailSettings().entity(setting);
    }

    public MailSettingResource mailSetting(String id) {
        return mailSettings().entity(id);
    }

    public SubusersResource subusers() {
        return new SubusersResource(getApiUrl(SubusersResource.API_VERSION), client, credential);
    }

    public SubuserResource subuser(Subuser subuser) {
        return subusers().entity(subuser);
    }

    public SubuserResource subuser(String id) {
        return subusers().entity(id);
    }
}
