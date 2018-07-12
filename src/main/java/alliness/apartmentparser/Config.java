package alliness.apartmentparser;

import alliness.apartmentparser.dto.AppConfig;
import alliness.core.convertor.Serializable;
import alliness.core.helpers.FReader;

import java.io.IOException;

public class Config {
    private static Config    ourInstance = new Config();
    private        AppConfig config;

    public static AppConfig get() {
        return ourInstance.config;
    }

    private Config() {
        try {
            config = Serializable.deserializeYaml(FReader.readFile("config/application.yaml"), AppConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

