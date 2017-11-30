package com.zqykj.dadi.exchange;

import com.zqykj.dadi.common.Config;
import com.zqykj.dadi.dao.SnaDao;
import com.zqykj.dadi.entity.SnaFinalEntity;
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
public class TransformSingle {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformSingle.class);

    public static void main(String[] args) {
        SnaDao snaDao = new SnaDao();
        TransformSingle transformData = new TransformSingle();
        List<SnaFinalEntity> entityList = new ArrayList<SnaFinalEntity>();
        try {
            HBaseClient hBaseClient = new HBaseClient(Config.buildHBaseConfiguration(),
                    CONFIG.getProperty("hbase.table", "SNA_FINAL_TEST2"));

            entityList = hBaseClient.getData();
            LOGGER.info("读取共 {} 条数据", entityList.size());
        } catch (IOException e) {
            LOGGER.error("读取HBase 有错误: {}", e);
        }
        snaDao.multiInsert(entityList);
    }

}
