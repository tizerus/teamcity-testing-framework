package com.example.teamcity.api.rest.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private final static String CONFIG_PROPERTIES = "config.properties";
    private static Config instance;
    private Properties properties;

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    private static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void loadProperties(String fileName) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                System.err.println("File not found " + fileName);
            }
            properties.load(stream);
        } catch (IOException e) {
            System.err.println("Error during file reading " + fileName);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return getInstance().properties.getProperty(key);
    }

}
