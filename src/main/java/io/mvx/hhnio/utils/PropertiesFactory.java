package io.mvx.hhnio.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFactory {

    public static Properties load() throws IOException {
        ClassLoader classLoader = PropertiesFactory.class.getClassLoader();

        Properties properties = new Properties();
        try (InputStream inputStream = classLoader.getResourceAsStream("server.conf")) {
            properties.load(inputStream);
        }

        return properties;
    }

    private PropertiesFactory() {
    }
}

