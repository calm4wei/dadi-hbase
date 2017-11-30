package com.zqykj.dadi.hbase;

import com.zqykj.dadi.common.Config;
import com.zqykj.dadi.dao.SnaDao;
import com.zqykj.dadi.entity.SnaFinalEntity;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/27.
 */
public class HBaseTest {

    @Test
    public void testGetPage() throws IOException {
        HBaseClient client = new HBaseClient(Config.buildHBaseConfiguration(),
                CONFIG.getProperty("hbase.table", "SNA_FINAL_TEST2"));

        SnaDao snaDao = new SnaDao();

        int currentPage = 1;
        long pageSize = Long.valueOf(CONFIG.getProperty("hbase.scan.pagesize", "10000"));
        List<SnaFinalEntity> entities = client.getDataByPage(null, pageSize, currentPage);
        while (true) {
            snaDao.insertBatch(entities.subList(0, entities.size() - 2));
            if (entities.size() == 1 && currentPage != 1) {
                break;
            } else {
                currentPage++;
                String startKey = entities.get(entities.size() - 1).getId();
                entities = client.getDataByPage(startKey, pageSize, currentPage);
            }

        }

    }
}
