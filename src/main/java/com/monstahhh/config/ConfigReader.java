package com.monstahhh.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class ConfigReader {

    public void read() throws IOException {
        // Loading the YAML file from the /resources folder
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource("config.yml").getFile());

        // Instantiating a new ObjectMapper as a YAMLFactory
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        // Mapping the employee from the YAML file to the Employee class
        Config config = om.readValue(file, Config.class);

        String token = config.getToken();
        System.out.println(token);
    }

}
