package io.github.seujorgenochurras.p2pcraftmod.client.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.seujorgenochurras.p2pcraftmod.client.P2pCraftConnectModClient;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ConfigFile {

    private static final File configFile = new File(P2pCraftConnectModClient.getResourcesDirPath() + "/config.yaml");
    private static Map<String, Object> configMap;

    static {
        configMap = loadYaml();
    }

    public static String get(String configKey) {
        configMap = loadYaml();
        return (String) configMap.get(configKey);
    }

    private static Map<String, Object> loadYaml() {
        try {
            File configFile = new File(P2pCraftConnectModClient.getResourcesDirPath() + "/config.yaml");
            if (!configFile.exists()) {
                Dotenv dotenv = Dotenv.configure()
                    .directory("../")
                    .load();
                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write("P2PCRAFT_API_URL: \"http://127.0.0.1:8080\"\n");
                fileWriter.write("P2PCRAFT_GITHUB_BOT_TOKEN: \"" + dotenv.get("P2PCRAFT_GITHUB_BOT_TOKEN") + "\"\n");
                fileWriter.write("NGROK_TOKEN: \"" + dotenv.get("NGROK_TOKEN") + "\"\n");
                fileWriter.close();
            }
            FileReader fileReader = new FileReader(configFile);
            Yaml yaml = new Yaml();
            return yaml.load(fileReader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
