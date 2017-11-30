package com.zqykj.dadi.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zqykj.dadi.common.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by weifeng on 2017/11/27.
 */
public class DataSourceC3p0 {

    private static ComboPooledDataSource cpds = null;

    static {
        cpds = new ComboPooledDataSource(Config.CONFIG.getProperty("mysql.pool", "mysql"));
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
