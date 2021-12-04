package com.monstahhh.config;

public class Config {

    private boolean debug;
    private String token;
    private String weatherToken;
    private String currencyToken;
    private String apiToken;
    private String dblToken;
    private String serverToken;

    public Config() {}

    public Config(boolean debug, String token, String weatherToken, String currencyToken, String apiToken, String dblToken, String serverToken) {
        this.debug = debug;
        this.token = token;
        this.weatherToken = weatherToken;
        this.currencyToken = currencyToken;
        this.apiToken = apiToken;
        this.dblToken = dblToken;
        this.serverToken = serverToken;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getToken() {
        return this.token;
    }

    public String getWeatherToken() {
        return weatherToken;
    }

    public String getCurrencyToken() {
        return currencyToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getDblToken() {
        return dblToken;
    }

    public String getServerToken() {
        return serverToken;
    }

}
