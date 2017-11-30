package com.zqykj.dadi.dao;

import com.zqykj.dadi.entity.SnaFinal2;
import com.zqykj.dadi.util.DataSourceC3p0;
import com.zqykj.dadi.util.DataSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.zqykj.dadi.common.Config.CONFIG;

/**
 * Created by weifeng on 2017/11/28.
 */
public class SnaFinal2Dao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnaFinal2Dao.class);
    private static final String sna2TableName = CONFIG.getProperty("mysql.sna.final2", "ccic-sna-fifth");
    private static final Integer NUM_THREAD = Integer.valueOf(CONFIG.getProperty("write.batch.thread", "5"));
    private static final Integer THREAD_BATCH = Integer.valueOf(CONFIG.getProperty("write.thread.batch", "2000"));
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREAD);

    public void insertBatch(List<SnaFinal2> list) {
        LOGGER.info("写入共 {} 条数据", list.size());
        Connection connection = null;
        PreparedStatement preStat = null;
        try {
            connection = DataSourceC3p0.getConnection();
            // INSERT INTO `ccic-sna-fifth`(`网络号`,`案件数`,`总得分`,`报案号`,`新老系统`,`id-6b40f4bb2f4343bf963eb9830`) VALUES('111','100',99,'subno',00,'1231312')
            String sql = "INSERT INTO `" + sna2TableName + "`"
                    // 9
                    + "(`id-6b40f4bb2f4343bf963eb9830`,`网络号`,`节点名称`,`触碰规则`)"

                    // 9
                    + " VALUES(?,?,?,?)";

            LOGGER.info("插入表 {} 的sql语句: {}", sna2TableName, sql);

            connection.setAutoCommit(false);
            preStat = connection.prepareStatement(sql);
            for (SnaFinal2 entity : list) {
                preStat.setString(1, entity.getKEY());
                preStat.setString(2, entity.getGROUPID());
                preStat.setString(3, entity.getITEMNAME());
                preStat.setString(4, entity.getTOUCHROLE());
                preStat.addBatch();
            }
            LOGGER.info("插入 {} 表, 共 {} 条数据: ", sna2TableName, list.size());
            preStat.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("批量写入异常 {}", e);
        } finally {
            DataSourceUtils.close(connection, preStat);
        }
    }

    public void multiInsert(List<SnaFinal2> list) {
        int total = list.size();
        int batchCount = total / THREAD_BATCH;
        if (batchCount == 0) {
            insertBatch(list);
            return;
        }
        for (int i = 0; i < batchCount; i++) {
            final int index = i;
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    int endIndex = (index + 1) * THREAD_BATCH;
                    //                    int end = endIndex > total ? total : endIndex;
                    insertBatch(list.subList(index * THREAD_BATCH, endIndex));
                    return null;
                }
            });
        }

        if (total % THREAD_BATCH != 0) {
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    insertBatch(list.subList(batchCount * THREAD_BATCH, total));
                    return null;
                }
            });
        }
    }

    public void clearData() {
        LOGGER.info("清空 {} 表数据", sna2TableName);
        String sql = "TRUNCATE `" + sna2TableName + "`";
        Connection conn = null;
        Statement stat = null;
        try {
            conn = DataSourceC3p0.getConnection();
            stat = conn.createStatement();
            stat.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("清空 {} 表异常", sna2TableName);
        } finally {
            DataSourceUtils.close(conn, stat);
        }

    }

    public void clearThread() {
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            executorService.shutdown();
        } catch (InterruptedException e) {
            LOGGER.error("清理线程池异常: {}", e);
        }
    }
}
