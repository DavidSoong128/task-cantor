package com.horizon.cantor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * 配置加载管理器
 * @author : David.Song/Java Engineer
 * @date : 2016/1/27 16:23
 * @see
 * @since : 1.0.0
 */
public class CantorConfig {

    private static Logger logger = LoggerFactory.getLogger(CantorConfig.class);

    private final static String CONFIG_PATH = "/cantor.properties";

    private String redisHost ;
    private int redisPort;

    private String monitorTopic;
    private String zkRegisterAddress;
    private String zkMasterPath;
    private int    redisDbDefault;
    private int    redisDb;

    private CantorConfig() {
        this.getProperties(CONFIG_PATH);
    }

    private static class Holder {
        private static CantorConfig config	= new CantorConfig();
    }

    public static CantorConfig configHolder() {
        return Holder.config;
    }

    private Properties getProperties(String url) {
        try {
            Properties prop = new Properties();
            InputStream in = CantorConfig.class.getResourceAsStream(url);
            prop.load(in);
            in.close();
            redisHost = prop.getProperty("redis.host");
            redisPort = Integer.valueOf(prop.getProperty("redis.port"));
            monitorTopic = prop.getProperty("monitor.topic");
            zkRegisterAddress = prop.getProperty("zk.register.address");
            zkMasterPath = prop.getProperty("zk.master.path");
            if (prop.getProperty("redis.db") != null) {
                redisDb = Integer.valueOf(prop.getProperty("redis.db"));
            }
            if (prop.getProperty("redis.db.default") != null) {
                redisDbDefault = Integer.valueOf(prop.getProperty("redis.db.default"));
            }
            return prop;
        } catch (Exception ex) {
            logger.error("load properties error",ex);
        }
        return null;
    }

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getMonitorTopic() {
        return monitorTopic;
    }

    public void setMonitorTopic(String monitorTopic) {
        this.monitorTopic = monitorTopic;
    }

    public String getZkRegisterAddress() {
        return zkRegisterAddress;
    }

    public void setZkRegisterAddress(String zkRegisterAddress) {
        this.zkRegisterAddress = zkRegisterAddress;
    }

    public String getZkMasterPath() {
        return zkMasterPath;
    }

    public void setZkMasterPath(String zkMasterPath) {
        this.zkMasterPath = zkMasterPath;
    }

    public int getRedisDbDefault() {
        return redisDbDefault;
    }

    public void setRedisDbDefault(int redisDbDefault) {
        this.redisDbDefault = redisDbDefault;
    }

    public int getRedisDb() {
        return redisDb;
    }

    public void setRedisDb(int redisDb) {
        this.redisDb = redisDb;
    }
}
