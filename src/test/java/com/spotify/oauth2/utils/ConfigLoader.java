package com.spotify.oauth2.utils;

import java.util.Properties;

public class ConfigLoader {

    private final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader() {

        properties = PropertyUtils.propertyLoader("/home/guy/Projects/RestAssuredFramework/src/test/resources/config.properties");

    }

    public static ConfigLoader getInstance() {

        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }

        return configLoader;
    }

    public String getGrantType() {
        String prop = properties.getProperty("grant_type");
        if (prop != null) return prop;
        else throw new RuntimeException("property grant_type is not specified in config.properties file");
    }

    public String getRefreshToken() {
        String prop = properties.getProperty("refresh_token");
        if (prop != null) return prop;
        else throw new RuntimeException("property refresh_token is not specified in config.properties file");
    }

    public String getAuthorization() {
        String prop = properties.getProperty("Authorization");
        if (prop != null) return prop;
        else throw new RuntimeException("property Authorization is not specified in config.properties file");
    }

    public String getContentType() {
        String prop = properties.getProperty("Content-Type");
        if (prop != null) return prop;
        else throw new RuntimeException("property Content-Type is not specified in config.properties file");
    }

    public String getUserId() {
        String prop = properties.getProperty("user_id");
        if (prop != null) return prop;
        else throw new RuntimeException("property user_id is not specified in config.properties file");
    }
}
