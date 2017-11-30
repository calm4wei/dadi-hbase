package com.zqykj.dadi.exchange;

import com.zqykj.dadi.common.Config;
import com.zqykj.dadi.dao.SnaFinal2Dao;
import com.zqykj.dadi.dao.SnaFinal3Dao;
import com.zqykj.dadi.entity.SnaFinal2;
import com.zqykj.dadi.entity.SnaFinal3;
import com.zqykj.dadi.hbase.HBaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/28.
 */
public class TransformFinal3Single {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformFinal3Single.class);

    public static void main(String[] args) {
        SnaFinal3Dao snaDao = new SnaFinal3Dao();
        TransformFinal3Single transformData = new TransformFinal3Single();
        List<SnaFinal3> entityList = new ArrayList<SnaFinal3>();
        try {
            HBaseClient hBaseClient = new HBaseClient(Config.buildHBaseConfiguration(),
                    CONFIG.getProperty("hbase.table.final3", "SNA_FINAL_TEST2"));

            entityList = hBaseClient.getFinal3Data();
            LOGGER.info("final3 读取共 {} 条数据", entityList.size());
        } catch (IOException e) {
            LOGGER.error("读取HBase 有错误: {}", e);
        }
        snaDao.multiInsert(entityList);
    }

}
