package com.monstahhh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Config {

    private boolean debug;
    private String token;
    private String weatherToken;
    private String currencyToken;
    private String apiToken;
    private String dblToken;
    private String serverToken;

    public Config() {
    }

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

    public Config read() throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("config.yml");

        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        return om.readValue(stream, Config.class);
    }

}
