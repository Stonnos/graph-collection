package graphcollection.gui.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Configuration service.
 *
 * @author Roman Batygin
 */
@Slf4j
public class ConfigurationService {

    private static final String ERROR_FORMAT = "There was an error while loading config from '%s': %s";
    private static final String UI_TEXT_PROPERTIES_PATH = "ui-text-properties.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static ConfigurationService applicationConfigService;

    private ConfigurationService() {
    }

    /**
     * Creates application config service singleton instance.
     *
     * @return application config service singleton instance
     */
    public static synchronized ConfigurationService getApplicationConfigService() {
        if (applicationConfigService == null) {
            applicationConfigService = new ConfigurationService();
        }
        return applicationConfigService;
    }

    /**
     * Loads ui text properties
     */
    public void loadUiTextProperties() {
        Map<String, String> uiTextMap = loadConfig(UI_TEXT_PROPERTIES_PATH, new TypeReference<>() {
        });
        uiTextMap.forEach(UIManager::put);
    }

    private <T> T loadConfig(String fileName, TypeReference<T> tTypeReference) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                fileName)) {
            log.info("Loads config from file [{}]", fileName);
            T config =  OBJECT_MAPPER.readValue(inputStream, tTypeReference);
            log.info("Config has been loaded from file [{}]", fileName);
            return config;
        } catch (IOException ex) {
            log.error(String.format(ERROR_FORMAT, fileName, ex.getMessage()));
            throw new IllegalStateException(ex);
        }
    }
}
