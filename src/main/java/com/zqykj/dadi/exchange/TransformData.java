package com.zqykj.dadi.exchange;

import com.zqykj.dadi.common.Config;
import com.zqykj.dadi.dao.SnaDao;
import com.zqykj.dadi.entity.SnaFinalEntity;
import com.zqykj.dadi.hbase.HBaseClient;
import com.zqykj.dadi.util.HBaseUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/23.
 */
public class TransformData {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformData.class);
    SnaDao snaDao = new SnaDao();

    public static void main(String[] args) throws IOException {
        TransformData transformData = new TransformData();
        String tableName = CONFIG.getProperty("hbase.table", "SNA_FINAL_TEST2");
        LOGGER.info("table name: {}, column family: {}", tableName, CONFIG.getProperty("hbase.family", "ccic"));
        transformData.snaDao.clearData();
        transformData.getPageAndInsert();
        //        HBaseClient hBaseClient = new HBaseClient(Config.buildHBaseConfiguration(), tableName);
        //        SnaDao snaDao = new SnaDao();
        //        List<SnaFinalEntity> entityList = new ArrayList<SnaFinalEntity>();
        //        try {
        //            entityList = hBaseClient.getData();
        //        } catch (IOException e) {
        //            LOGGER.error("读取HBase 有错误: {}", e);
        //        }
        //        snaDao.insertBatch(entityList);
    }

    public void getPageAndInsert() throws IOException {
        HBaseClient client = new HBaseClient(Config.buildHBaseConfiguration(),
                CONFIG.getProperty("hbase.table", "SNA_FINAL_TEST2"));

        int currentPage = 1;
        long pageSize = Long.valueOf(CONFIG.getProperty("hbase.scan.pagesize", "10000"));
        LOGGER.info("pageSize: {}, currentPage:{}", pageSize, currentPage);
        List<SnaFinalEntity> entities = client.getDataByPage(null, pageSize, currentPage);
        long total = entities.size() - 1;
        while (true) {
            if (entities.size() > 1) {
                snaDao.multiInsert(entities.subList(0, entities.size() - 1));
            } else {
                snaDao.multiInsert(entities);
            }
            if (entities.size() == 1) {
                LOGGER.info("读取完毕==============> pageSize: {}, currentPage:{}", pageSize, currentPage);
                break;
            } else {
                long ts = Long.valueOf(CONFIG.getProperty("hbase.scan.sleep", "1000"));
                try {
                    Thread.sleep(ts);
                } catch (InterruptedException e) {
                    LOGGER.info("等待 {} 毫秒", ts);
                }
                currentPage++;
                String startKey = entities.get(entities.size() - 1).getId();
                LOGGER.info("pageSize: {}, currentPage:{}, startKey: {}", pageSize, currentPage, startKey);
                entities = client.getDataByPage(startKey, pageSize, currentPage);
                total += entities.size() - 1;
            }
            LOGGER.info("hbase total: {}, currentPage: {}", total, currentPage);
        }
        snaDao.clearThread();
        LOGGER.info("取完全部数据 hbase total: {}", total);
    }

}
