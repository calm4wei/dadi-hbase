package com.zqykj.dadi.dao;

import com.zqykj.dadi.entity.SnaFinal2;
import com.zqykj.dadi.entity.SnaFinal3;
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
public class SnaFinal3Dao {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnaFinal3Dao.class);
    private static final String sna3TableName = CONFIG.getProperty("mysql.sna.final3", "ccic-sna-fifth");
    private static final Integer NUM_THREAD = Integer.valueOf(CONFIG.getProperty("write.batch.thread", "5"));
    private static final Integer THREAD_BATCH = Integer.valueOf(CONFIG.getProperty("write.thread.batch", "2000"));
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREAD);

    public void insertBatch(List<SnaFinal3> list) {
        LOGGER.info("写入共 {} 条数据", list.size());
        Connection connection = null;
        PreparedStatement preStat = null;
        try {
            connection = DataSourceC3p0.getConnection();
            // INSERT INTO `ccic-sna-fifth`(`网络号`,`案件数`,`总得分`,`报案号`,`新老系统`,`id-6b40f4bb2f4343bf963eb9830`) VALUES('111','100',99,'subno',00,'1231312')
            String sql = "INSERT INTO `" + sna3TableName + "`"
                    // 7
                    + "(`id-6b40f4bb2f4343bf963eb9830`,`网络号`,`案件数`,`所属机构`,`总得分`,`得分01`,`得分02`"
                    // 10
                    + ",`得分03`,`得分03_2`,`得分04`,`得分05`,`得分06`,`得分07`,`得分08`,`得分09`,`得分10`,`得分12`"
                    // 3
                    + ",`得分13`,`得分14`,`得分17`)"

                    // 7
                    + " VALUES(?,?,?,?,?,?,?"
                    // 10
                    + ",?,?,?,?,?,?,?,?,?,?"
                    // 3
                    + ",?,?,?)";

            LOGGER.info("插入表 {} 的sql语句: {}", sna3TableName, sql);

            connection.setAutoCommit(false);
            preStat = connection.prepareStatement(sql);
            for (SnaFinal3 entity : list) {
                preStat.setString(1, entity.getId());
                preStat.setString(2, entity.getGROUPID());
                preStat.setInt(3, entity.getCOUNT() == null ? -1 : entity.getCOUNT());
                preStat.setString(4, entity.getCOMCODE());
                preStat.setInt(5, entity.getMARK() == null ? -1 : entity.getMARK());
                preStat.setInt(6, entity.getMARK01() == null ? -1 : entity.getMARK01());
                preStat.setInt(7, entity.getMARK02() == null ? -1 : entity.getMARK02());
                preStat.setInt(8, entity.getMARK03() == null ? -1 : entity.getMARK03());
                preStat.setInt(9, entity.getMARK03_2() == null ? -1 : entity.getMARK03_2());
                preStat.setInt(10, entity.getMARK04() == null ? -1 : entity.getMARK04());
                preStat.setInt(11, entity.getMARK05() == null ? -1 : entity.getMARK05());
                preStat.setInt(12, entity.getMARK06() == null ? -1 : entity.getMARK06());
                preStat.setInt(13, entity.getMARK07() == null ? -1 : entity.getMARK07());
                preStat.setInt(14, entity.getMARK08() == null ? -1 : entity.getMARK08());
                preStat.setInt(15, entity.getMARK09() == null ? -1 : entity.getMARK09());
                preStat.setInt(16, entity.getMARK10() == null ? -1 : entity.getMARK10());
                preStat.setInt(17, entity.getMARK12() == null ? -1 : entity.getMARK12());
                preStat.setInt(18, entity.getMARK13() == null ? -1 : entity.getMARK13());
                preStat.setInt(19, entity.getMARK14() == null ? -1 : entity.getMARK14());
                preStat.setInt(20, entity.getMARK17() == null ? -1 : entity.getMARK17());
                preStat.addBatch();
            }
            LOGGER.info("插入 {} 表, 共 {} 条数据: ", sna3TableName, list.size());
            preStat.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("批量写入异常 {}", e);
        } finally {
            DataSourceUtils.close(connection, preStat);
        }
    }

    public void multiInsert(final List<SnaFinal3> list) {
        final int total = list.size();
        final int batchCount = total / THREAD_BATCH;
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
        LOGGER.info("清空 {} 表数据", sna3TableName);
        String sql = "TRUNCATE `" + sna3TableName + "`";
        Connection conn = null;
        Statement stat = null;
        try {
            conn = DataSourceC3p0.getConnection();
            stat = conn.createStatement();
            stat.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("清空 {} 表异常", sna3TableName);
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
