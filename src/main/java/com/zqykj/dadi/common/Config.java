package com.zqykj.dadi.common;

import com.zqykj.dadi.util.HBaseUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by zqykj on 2017/5/17.
 */
public class Config {

    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    public static Properties CONFIG = null;
    private static String configPath = "config/zqy-hbase.properties";

    static {
        PropertyConfigurator.configure("config/log4j.properties");
        CONFIG = new Properties();
        try {
            CONFIG.load(new FileReader(configPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration buildHBaseConfiguration() {
        Configuration configuration = new Configuration();
        // fs.default.name
        configuration.set("fs.defaultFS", CONFIG.getProperty("fs.default.name", "hdfs://10.1.77.61:8020"));
        Configuration conf = HBaseConfiguration.create(configuration);
        conf.set("hbase.zookeeper.quorum",
                CONFIG.getProperty("hbase.zookeeper.quorum", "10.1.77.61,10.1.77.62,10.1.77.63"));
        conf.set("hbase.zookeeper.property.clientPort",
                CONFIG.getProperty("hbase.zookeeper.property.clientPort", "2181"));
        conf.set("zookeeper.znode.parent", CONFIG.getProperty("zookeeper.znode.parent", "/hbase"));
        try {
            if (Boolean.valueOf(CONFIG.getProperty("hbase.user.kerberos", "true"))) {
                HBaseUtils.kerberosByConfig(conf);
            }
        } catch (IOException e) {
            LOGGER.error("组装kerberos认证出错 {}", e);
        }
        LOGGER.info("configuration info: {}", conf);
        return conf;
    }

    public static void main(String[] args) {
        System.out.println(CONFIG.getProperty("fs.default.name", "hdfs://10.1.77.61:8020"));
    }
}
