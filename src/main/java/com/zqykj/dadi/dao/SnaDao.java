package com.zqykj.dadi.dao;

import com.zqykj.dadi.entity.SnaFinalEntity;
import com.zqykj.dadi.hbase.HBaseClient;
import com.zqykj.dadi.util.DataSourceC3p0;
import com.zqykj.dadi.util.DataSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static com.zqykj.dadi.common.Config.*;

/**
 * Created by weifeng on 2017/11/23.
 */
public class SnaDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnaDao.class);
    private static final Integer batchNum = Integer.parseInt(CONFIG.getProperty("mysql.batch.num", "1000"));
    private static final String snaTableName = CONFIG.getProperty("mysql.tablename", "ccic-sna-fifth");
    private static final Integer NUM_THREAD = Integer.valueOf(CONFIG.getProperty("write.batch.thread", "5"));
    private static final Integer THREAD_BATCH = Integer.valueOf(CONFIG.getProperty("write.thread.batch", "2000"));
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREAD);

    public void getById(String id) {
        Connection connection = DataSourceUtils.getConn();
        Statement statement = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM `ccic-sna-fifth` WHERE `ID-6b40f4bb2f4343bf963eb9830` = " + id;
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                LOGGER.debug("groupid:{} , caseCount: {}", rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.close(connection, statement);
        }

    }

    public void multiGetAndInsert(final HBaseClient hBaseClient, final List<byte[]> list) throws IOException {
        final int total = list.size();
        final int batchCount = total / THREAD_BATCH;
        if (batchCount == 0) {
            insertBatch(hBaseClient.getEntities(list));
            return;
        }
        for (int i = 0; i < batchCount; i++) {
            final int index = i;
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    int endIndex = (index + 1) * THREAD_BATCH;
                    //                    int end = endIndex > total ? total : endIndex;
                    insertBatch(hBaseClient.getEntities(list.subList(index * THREAD_BATCH, endIndex)));
                    return null;
                }
            });
        }

        if (total % THREAD_BATCH != 0) {
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    insertBatch(hBaseClient.getEntities(list.subList(batchCount * THREAD_BATCH, total)));
                    return null;
                }
            });
        }
    }

    public void multiInsert(final List<SnaFinalEntity> list) {
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

    public void insertBatch(List<SnaFinalEntity> list) {
        LOGGER.info("写入共 {} 条数据", list.size());
        Connection connection = null;
        PreparedStatement preStat = null;
        try {
            connection = DataSourceC3p0.getConnection();
            // INSERT INTO `ccic-sna-fifth`(`网络号`,`案件数`,`总得分`,`报案号`,`新老系统`,`id-6b40f4bb2f4343bf963eb9830`) VALUES('111','100',99,'subno',00,'1231312')
            String sql = "INSERT INTO `" + snaTableName + "`"
                    // 9
                    + "(`ID-6b40f4bb2f4343bf963eb9830`,`网络号`,`案件数`,`总得分`,`报案号`,`新老系统`,`承保机构`,`报案日期`,`报案时间`"
                    // 6
                    + ",`出险日期`,`出险时间`,`报案电话`,`报案联系电话`,`出险区域`,`出险地点`"
                    // 8
                    + ",`是否含人伤`,`保单号`,`起保日期`,`终保日期`,`被保险人姓名`,`被保险人类型`,`被保险人证件类型`,`被保险人证件号`"
                    // 8
                    + ",`被保险人手机号`,`标的车牌`,`标的车架`,`标的驾驶员`,`标的驾照`,`标的电话`,`标的修理厂`,`标的是否4S维修`"
                    // 7
                    + ",`标的定损员代码`,`标的定损员姓名`,`标的核损金额`,`三者车标识`,`三者车牌`,`三者车架`,`三者驾驶员`"
                    // 7
                    + ",`三者驾照`,`三者电话`,`三者修理厂`,`三者是否4S维修`,`三者定损员代码`,`三者定损员姓名`,`三者核损金额`)"

                    // 9
                    + " VALUES(?,?,?,?,?,?,?,?,?"
                    // 6
                    + ",?,?,?,?,?,?"
                    // 8
                    + ",?,?,?,?,?,?,?,?"
                    // 8
                    + ",?,?,?,?,?,?,?,?"
                    // 7
                    + ",?,?,?,?,?,?,?"
                    // 7
                    + ",?,?,?,?,?,?,?)";
            LOGGER.info("插入表 {} 的sql语句: {}", snaTableName, sql);

            connection.setAutoCommit(false);
            preStat = connection.prepareStatement(sql);
            for (SnaFinalEntity entity : list) {
                preStat.setString(1, entity.getId() + UUID.randomUUID().toString());
                preStat.setString(2, entity.getGROUPID());
                preStat.setString(3, entity.getCOUNT());
                preStat.setInt(4, entity.getMARK() == null ? -1 : entity.getMARK());
                preStat.setString(5, entity.getREGISTNO());
                preStat.setInt(6, entity.getSys_type() == null ? -1 : entity.getSys_type());
                preStat.setString(7, entity.getCOMCODE());
                preStat.setString(8, entity.getREPORTDATE());
                preStat.setString(9, entity.getREPORTHOUR());
                preStat.setString(10, entity.getDAMAGESTARTDATE());
                preStat.setString(11, entity.getDAMAGESTARTHOUR());
                preStat.setString(12, entity.getREPORTORPHONENO());
                preStat.setString(13, entity.getLINKERMOBILE());
                preStat.setString(14, entity.getDAMAGEAREANAME());
                preStat.setString(15, entity.getDAMAGEADDRESS());
                preStat.setString(16, entity.getINVOLVEWOUND());
                preStat.setString(17, entity.getPOLICYNO());
                preStat.setString(18, entity.getSTARTDATE());
                preStat.setString(19, entity.getENDDATE());
                preStat.setString(20, entity.getINSUREDNAME());
                preStat.setString(21, entity.getINSUREDTYPE());
                preStat.setString(22, entity.getIDENTIFYTYPE());
                preStat.setString(23, entity.getIDENTIFYNUMBER());
                preStat.setString(24, entity.getMOBILE());
                preStat.setString(25, entity.getACTUALLICENSENO_1());
                preStat.setString(26, entity.getVINNO_1());
                preStat.setString(27, entity.getDRIVERNAME_1());
                preStat.setString(28, entity.getDRIVINGLICENSENO_1());
                preStat.setString(29, entity.getMOBILE_1());
                preStat.setString(30, entity.getREPAIRNAME_1());
                preStat.setString(31, entity.getIS4S_1());
                preStat.setString(32, entity.getLOSSAPPROVALCODE_1());
                preStat.setString(33, entity.getLOSSAPPROVALNAME_1());
                preStat.setString(34, entity.getSUMVERIFYFINAL_1());
                preStat.setString(35, entity.getITEMNO());
                preStat.setString(36, entity.getACTUALLICENSENO_2());
                preStat.setString(37, entity.getVINNO_2());
                preStat.setString(38, entity.getDRIVERNAME_2());
                preStat.setString(39, entity.getDRIVINGLICENSENO_2());
                preStat.setString(40, entity.getMOBILE_2());
                preStat.setString(41, entity.getREPAIRNAME_2());
                preStat.setString(42, entity.getIS4S_2());
                preStat.setString(43, entity.getLOSSAPPROVALCODE_2());
                preStat.setString(44, entity.getLOSSAPPROVALNAME_2());
                preStat.setFloat(45, entity.getSUMVERIFYFINAL_2() == null ? -1 : entity.getSUMVERIFYFINAL_2());

                preStat.addBatch();
            }
            LOGGER.info("插入 {} 表, 共 {} 条数据: ", snaTableName, list.size());
            preStat.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("批量写入异常 {}", e);
        } finally {
            DataSourceUtils.close(connection, preStat);
        }
    }

    public void clearData() {
        LOGGER.info("清空 {} 表数据", snaTableName);
        String sql = "TRUNCATE `" + snaTableName + "`";
        Connection conn = null;
        Statement stat = null;
        try {
            conn = DataSourceC3p0.getConnection();
            stat = conn.createStatement();
            stat.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("清空 {} 表异常", snaTableName);
        } finally {
            DataSourceUtils.close(conn, stat);
        }
    }

    public void clearThread() {
        try {
            LOGGER.info("清理线程池");
            executorService.awaitTermination(60, TimeUnit.SECONDS);
            executorService.shutdown();
        } catch (InterruptedException e) {
            LOGGER.error("清理线程池异常: {}", e);
        }
    }
}


