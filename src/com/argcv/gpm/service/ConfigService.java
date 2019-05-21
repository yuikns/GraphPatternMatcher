package com.argcv.gpm.service;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigService {
    public enum OS_TYPE {
        windows_x86, windows_x64, linux_x86, linux_x64
    }


    private static Properties properties;
    private final static String configResourceName = "/config.properties";
    private static URL propURL = ConfigService.class
            .getResource(configResourceName);
    private static Map<String, String> cfgMap;
    public static OS_TYPE osType;

    static {
        properties = new Properties();
        InputStream is = null;
        try {
            is = propURL.openStream();
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cfgMap = new HashMap<String, String>();

        Properties osProp = System.getProperties();
        String osName = osProp.get("os.name").toString().toLowerCase();
        String osArch = osProp.get("sun.arch.data.model").toString()
                .toLowerCase();
        // System.out.println(osName + "\n" + osArch);
        boolean is32 = osArch.contains("32");
        if (osName.contains("windows")) {
            osType = is32 ? OS_TYPE.windows_x86 : OS_TYPE.windows_x64;
        } else {
            osType = is32 ? OS_TYPE.linux_x86 : OS_TYPE.linux_x64;
        }
    }

    public static String getString(String prop) {
        return get(prop);
    }

    public static Integer getInt(String prop) {
        return Integer.parseInt(get(prop));
    }

    public static String get(String prop) {
        return properties.getProperty(prop);
    }

    public static void setToMap(String key, String value) {
        cfgMap.put(key, value);
    }

    public static void setToMap(String key, Integer value) {
        cfgMap.put(key, String.valueOf(value));
    }

    public static String getFromMap(String key) {
        return cfgMap.get(key);
    }

    public static Integer getIntFromMap(String key) {
        return Integer.parseInt(getFromMap(key));
    }

}
