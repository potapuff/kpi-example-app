package nc.edu.kpi.l3.helper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Keys {

    public static final String[] PARAMS = {
            "APP.URL", "APP.PORT", "ENV", "DB.NAME",
            "DEFAULT_MESSAGE", "NOT_FOUND_MESSAGE", "ERROR_MESSAGE",
            "DEFAULT_ICON"
    };

    private static HashMap<String, String> keys = null;

    public static void loadParams(File properties) {
        Map<String, String> env = System.getenv();
        keys = new HashMap<>();
        try {
            var is = FileUtils.openInputStream(properties);
            var app_properties = new Properties();
            app_properties.load(is);
            for (String key : PARAMS) {
                String value = env.getOrDefault(key, app_properties.getProperty(key));
                if (value == null) {
                    throw new IllegalArgumentException(String.format("Property %s not found in %s and or system environment", key, properties.getPath()));
                }
                keys.put(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        if (keys == null) {
            throw new RuntimeException("Add path to config.property to your application on init");
        }

        if (!keys.containsKey(key)) {
            throw new RuntimeException(String.format("Unknown key %s in app properties", key));
        }

        String value = keys.get(key);
        if (value.isEmpty()) {
            throw new RuntimeException(String.format("No value for key %s", key));
        }
        return value;
    }

    public static boolean isProduction() {
        return Keys.get("ENV").equalsIgnoreCase("production");
    }

}
