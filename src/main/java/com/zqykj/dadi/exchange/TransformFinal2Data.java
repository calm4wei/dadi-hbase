package com.zqykj.dadi.exchange;

import com.zqykj.dadi.common.Config;
import com.zqykj.dadi.dao.SnaFinal2Dao;
import com.zqykj.dadi.entity.SnaFinal2;
import com.zqykj.dadi.hbase.HBaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/23.
 */
public class TransformFinal2Data {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformFinal2Data.class);
    SnaFinal2Dao snaDao = new SnaFinal2Dao();

    public static void main(String[] args) throws IOException {
        TransformFinal2Data transformData = new TransformFinal2Data();
        String tableName = CONFIG.getProperty("hbase.table.final2", "SNA_FINAL2");
        LOGGER.info("table name: {}, column family: {}", tableName, CONFIG.getProperty("hbase.family", "ccic"));
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
        snaDao.clearData();
        HBaseClient client = new HBaseClient(Config.buildHBaseConfiguration(),
                CONFIG.getProperty("hbase.table.final2", "SNA_FINAL_TEST2"));

        int currentPage = 1;
        long pageSize = Long.valueOf(CONFIG.getProperty("hbase.scan.pagesize", "10000"));
        LOGGER.info("pageSize: {}, currentPage:{}", pageSize, currentPage);
        List<SnaFinal2> entities = client.getFinal2ByPage(null, pageSize, currentPage);
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
                String startKey = entities.get(entities.size() - 1).getKEY();
                LOGGER.info("pageSize: {}, currentPage:{}, startKey: {}", pageSize, currentPage, startKey);
                entities = client.getFinal2ByPage(startKey, pageSize, currentPage);
            }
        }
    }

}
