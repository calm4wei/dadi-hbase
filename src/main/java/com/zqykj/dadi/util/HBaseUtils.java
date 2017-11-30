package com.zqykj.dadi.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/23.
 */
public class HBaseUtils {

    public static void kerberosByConfig(Configuration conf) throws IOException {
        // 这个配置文件主要是记录 kerberos的相关配置信息，例如KDC是哪个IP？默认的realm是哪个？
        // 如果没有这个配置文件这边认证的时候肯定不知道KDC的路径喽
        // 这个文件也是从远程服务器上copy下来的
        //System.setProperty("java.security.krb5.conf", "/home/bigdata_query/krb5.conf");

        // 这个hbase.keytab也是从远程服务器上copy下来的, 里面存储的是密码相关信息
        // 这样我们就不需要交互式输入密码了
        conf.set("keytab.file", "/home/bigdata_query/bigdata_query.keytab");
        // 这个可以理解成用户名信息，也就是Principal hbase/1722.myip.domain@HADOOP.COM
        conf.set("kerberos.principal", "bigdata_query@DDCX.COM");
        // hbase/_HOST@DDCX.COM
        conf.set("hbase.master.kerberos.principal", "hbase/_HOST@DDCX.COM");
        // hbase/_HOST@DDCX.COM
        conf.set("hbase.regionserver.kerberos.principal", "hbase/_HOST@DDCX.COM");
        // kerberos
        conf.set("hbase.security.authentication", "kerberos");
        conf.set("hadoop.security.authentication", CONFIG.getProperty("hadoop.security.authentication", "kerberos"));

        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(CONFIG.getProperty("hbase.kerberos.user", "bigdata_query@DDCX.COM"),
                CONFIG.getProperty("hbase.kerberos.path", "/home/bigdata_query/bigdata_query.keytab"));

    }


    public static void close(Table table, ResultScanner resultScanner){

    }

}
