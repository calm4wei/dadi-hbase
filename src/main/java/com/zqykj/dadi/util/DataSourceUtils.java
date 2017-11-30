package com.zqykj.dadi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/23.
 */
public class DataSourceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceUtils.class);
    private static String url = CONFIG.getProperty("mysql.url", "");
    private static String userName = CONFIG.getProperty("mysql.username", "");
    private static String password = CONFIG.getProperty("mysql.password", "");

    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            LOGGER.error("获取连接出错", e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("获取连接出错", e);
        }
        return conn;
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("关闭连接错误 {}", e);
            }
        }
    }

    public static void close(Connection conn, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.error("关闭statement错误 {}", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("关闭连接错误 {}", e);
            }
        }
    }

}
