package com.nairbspace.octoandroid.model;

import javax.inject.Inject;

public class OctoAccount {
    public static final String DEFAULT_ACCOUNT_TYPE = "com.nairbspace.octoandroid";
    public static final String API_KEY_KEY = "API_KEY_KEY";
    public static final String SCHEME_KEY = "SCHEME_KEY";
    public static final String HOST_KEY = "HOST_KEY";
    public static final String PORT_KEY = "PORT_KEY";

    private String mAccountType;
    private String mAccountName;
    private String mApiKey;
    private String mScheme;
    private String mHost;
    private int mPort;

    @Inject
    public OctoAccount() {
    }

    public void setAccount(String accountType, String accountName,
                      String apiKey, String scheme, String host, int port) {
        mAccountType = accountType;
        mAccountName = accountName;
        mApiKey = apiKey;
        mScheme = scheme;
        mHost = host;
        mPort = port;
    }

    public String getAccountType() {
        return mAccountType;
    }

    public void setAccountType(String accountType) {
        mAccountType = accountType;
    }

    public String getAccountName() {
        return mAccountName;
    }

    public void setAccountName(String accountName) {
        mAccountName = accountName;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }

    public String getScheme() {
        return mScheme;
    }

    public void setScheme(String scheme) {
        mScheme = scheme;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
    }
}
