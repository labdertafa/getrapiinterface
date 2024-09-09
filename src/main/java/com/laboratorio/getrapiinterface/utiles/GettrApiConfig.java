package com.laboratorio.getrapiinterface.utiles;

import java.io.FileReader;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rafael
 * @version 1.0
 * @created 16/08/2024
 * @updated 09/09/2024
 */
public class GettrApiConfig {
    private static final Logger log = LogManager.getLogger(GettrApiConfig.class);
    private static GettrApiConfig instance;
    private final Properties properties;

    private GettrApiConfig() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try {
            this.properties.load(new FileReader("config//gettr_api.properties"));
        } catch (Exception e) {
            log.error("Ha ocurrido un error leyendo el fichero de configuración del API de GETTR. Finaliza la aplicación!");
            log.error(String.format("Error: %s", e.getMessage()));
            if (e.getCause() != null) {
                log.error(String.format("Causa: %s", e.getCause().getMessage()));
            }
            System.exit(-1);
        }
    }

    public static GettrApiConfig getInstance() {
        if (instance == null) {
            synchronized (GettrApiConfig.class) {
                if (instance == null) {
                    instance = new GettrApiConfig();
                }
            }
        }
        
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}