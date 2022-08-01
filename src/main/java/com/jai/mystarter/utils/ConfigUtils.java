package com.jai.mystarter.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.util.Properties;

@Slf4j
public class ConfigUtils {
    public static String getConfigValue(String key,String defaultValue){
        Properties properties=null;
        try {
            properties=PropertiesLoader.loadProperties("config.properties");
        }catch (Exception e){
            log.error("config.properties is not present in class path",e);
        }
        if(properties!=null){
            if(StringUtils.isEmpty(properties.getProperty(key)))
                return defaultValue;
            else
                return properties.getProperty(key);
        }
        return defaultValue;
    }
}
