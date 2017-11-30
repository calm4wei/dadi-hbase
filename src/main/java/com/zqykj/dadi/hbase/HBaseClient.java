package com.zqykj.dadi.hbase;

import com.zqykj.dadi.common.Constants;
import com.zqykj.dadi.entity.SnaFinal2;
import com.zqykj.dadi.entity.SnaFinal3;
import com.zqykj.dadi.entity.SnaFinalEntity;
import com.zqykj.dadi.util.FormatUtils;
import javafx.scene.control.Tab;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HRegionLocation;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.*;

import static com.zqykj.dadi.common.Config.CONFIG;
import static com.zqykj.dadi.common.Constants.*;

/**
 * Created by weifeng on 2017/11/23.
 */
public class HBaseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseClient.class);
    private Configuration configuration;
    private String tableName;
    private static Connection connection = null;

    public HBaseClient(Configuration configuration, String tableName) throws IOException {
        this.configuration = configuration;
        this.tableName = tableName;
        try {
            connection = ConnectionFactory.createConnection(configuration);
            //new Reminder(30);
        } catch (IOException e) {
            LOGGER.error("获取hbase connection异常:{}", e);
            throw new IOException(e);
        }
    }

    public static class Reminder {
        Timer timer;

        public Reminder(int sec) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        synchronized (connection) {
                            connection = ConnectionFactory.createConnection();
                        }
                    } catch (IOException e) {
                        LOGGER.error("hbase conn reconnect error");
                    }

                }
            }, sec * 1000);
        }
    }

    public ResultScanner getResultScanner(Table table, Integer startRow, Long pageSize) throws IOException {
        Scan scan = new Scan();
        // 每次多取一条，用下一页的startRow
        PageFilter pageFilter = new PageFilter(pageSize + 1);
        scan.setFilter(pageFilter);
        if (null != startRow) {
            scan.setStartRow(Bytes.toBytes(startRow));
        }
        //        scan.setCaching(10000);
        //        scan.setCacheBlocks(false);
        ResultScanner resultScanner = table.getScanner(scan);
        return resultScanner;
    }

    public ResultScanner getResultScanner(Table table, String startRow, Long pageSize) throws IOException {
        Scan scan = new Scan();
        // 每次多取一条，用下一页的startRow
        PageFilter pageFilter = new PageFilter(pageSize + 1);
        scan.setFilter(pageFilter);
        if (null != startRow) {
            scan.setStartRow(Bytes.toBytes(startRow));
        }
        //        scan.setCaching(10000);
        //        scan.setCacheBlocks(false);
        ResultScanner resultScanner = table.getScanner(scan);
        return resultScanner;
    }

    public List<SnaFinalEntity> getDataByPage(String startRow, Long pageSize, Integer currentPage) {
        List<SnaFinalEntity> list = new ArrayList<>();
        Table table = null;
        ResultScanner resultScanner = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            resultScanner = getResultScanner(table, startRow, pageSize);
            Result result;
            long count = 0;
            while ((result = resultScanner.next()) != null) {
                SnaFinalEntity entity = parseResult2SnaDataOri(result, null);
                LOGGER.debug(entity.toString());
                list.add(entity);
                count++;
            }
            LOGGER.info("count: {}", count);
            return list;
        } catch (IOException e) {
            LOGGER.error("get data by page has exception: {}", e);
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resultScanner != null) {
                resultScanner.close();
            }
        }
        return list;
    }

    public List<SnaFinal2> getFinal2ByPage(String startRow, Long pageSize, Integer currentPage) {
        List<SnaFinal2> list = new ArrayList<>();
        Table table = null;
        ResultScanner resultScanner = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName));
            resultScanner = getResultScanner(table, startRow, pageSize);
            Result result;
            long count = 0;
            while ((result = resultScanner.next()) != null) {
                SnaFinal2 entity = parseSnaFinal2Data(result);
                LOGGER.debug(entity.toString());
                list.add(entity);
                count++;
            }
            LOGGER.info("count: {}", count);
            return list;
        } catch (IOException e) {
            LOGGER.error("get data by page has exception: {}", e);
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (resultScanner != null) {
                resultScanner.close();
            }
        }
        return list;
    }

    public void getTotalCount(ResultScanner resultScanner) throws IOException {
        Result result = null;
        int count = 0;
        while ((result = resultScanner.next()) != null) {
            System.out.println(Bytes.toString(result.getRow()));
            count++;
        }
        LOGGER.info("count: {} ", count);
    }

    public List<SnaFinalEntity> getData() throws IOException {
        List<SnaFinalEntity> list = new ArrayList<SnaFinalEntity>();
        HTable hTable = new HTable(configuration, tableName);
        Scan scan = new Scan();
        //        scan.setBatch(1000);
        //        scan.setCaching(Integer.parseInt(CONFIG.getProperty("hbase.scan.cache", "10000")));
        //        scan.setCacheBlocks(Boolean.parseBoolean(CONFIG.getProperty("hbase.scan.chache.blocks", "false")));
        ResultScanner resultScanner = hTable.getScanner(scan);
        Result result;
        while ((result = resultScanner.next()) != null) {
            SnaFinalEntity entity = parseResult2SnaData(result);
            LOGGER.debug(entity.toString());
            list.add(entity);
        }

        resultScanner.close();
        hTable.close();
        return list;
    }

    public List<byte[]> getRowData() throws IOException {
        List<byte[]> list = new ArrayList<byte[]>();
        HTable hTable = new HTable(configuration, tableName);
        Scan scan = new Scan();
        scan.setFilter(new FirstKeyOnlyFilter());
        ResultScanner resultScanner = hTable.getScanner(scan);
        Result result;
        while ((result = resultScanner.next()) != null) {
            list.add(result.getRow());
        }
        resultScanner.close();
        hTable.close();
        return list;
    }

    public List<SnaFinalEntity> getEntities(List<byte[]> rows) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        List<SnaFinalEntity> list = new ArrayList<>();
        Result result = null;
        for (byte[] by : rows) {
            result = table.get(new Get(by));
            SnaFinalEntity entity = parseResult2SnaData(result);
            list.add(entity);
        }
        LOGGER.info("转换: {} 条数据", list.size());
        table.close();
        return list;
    }

    public List<SnaFinal2> getFinal2Data() throws IOException {
        List<SnaFinal2> list = new ArrayList<SnaFinal2>();
        HTable hTable = new HTable(configuration, tableName);
        Scan scan = new Scan();
        //        scan.setBatch(1000);
        scan.setCaching(Integer.parseInt(CONFIG.getProperty("hbase.scan.cache", "10000")));
        scan.setCacheBlocks(Boolean.parseBoolean(CONFIG.getProperty("hbase.scan.chache.blocks", "false")));
        ResultScanner resultScanner = hTable.getScanner(scan);
        Result result;
        while ((result = resultScanner.next()) != null) {
            SnaFinal2 entity = parseSnaFinal2Data(result);
            LOGGER.debug(entity.toString());
            list.add(entity);
        }

        resultScanner.close();
        hTable.close();
        return list;
    }

    public List<SnaFinal3> getFinal3Data() throws IOException {
        List<SnaFinal3> list = new ArrayList<SnaFinal3>();
        HTable hTable = new HTable(configuration, tableName);
        Scan scan = new Scan();
        //        scan.setBatch(1000);
        //        scan.setCaching(Integer.parseInt(CONFIG.getProperty("hbase.scan.cache", "10000")));
        //        scan.setCacheBlocks(Boolean.parseBoolean(CONFIG.getProperty("hbase.scan.chache.blocks", "false")));
        ResultScanner resultScanner = hTable.getScanner(scan);
        Result result;
        while ((result = resultScanner.next()) != null) {
            SnaFinal3 entity = parseSnaFinal3Data(result);
            LOGGER.debug(entity.toString());
            list.add(entity);
        }

        resultScanner.close();
        hTable.close();
        return list;
    }

    public void test(Result result) {
        for (Cell cell : result.listCells()) {
            String qualifer = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            LOGGER.info("qualifer: {}, value: {}",
                    Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()),
                    qualifer);

        }
    }

    private void buildEntityIf(SnaFinalEntity entity, Cell cell, String qualifer) {
        if (qualifer.equals(GROUPID_QUA)) {
            entity.setGROUPID(getValueInCell(cell));
        } else if (qualifer.equals(COUNT_QUA)) {
            entity.setCOUNT(getValueInCell(cell));
        } else if (qualifer.equals(MARK_QUA)) {
            entity.setMARK(getValueIntInCell(cell));
        } else if (qualifer.equals(REGISTNO_QUA)) {
            entity.setREGISTNO(getValueInCell(cell));
        } else if (qualifer.equals(SYS_TYPE_QUA)) {
            entity.setSys_type(getValueIntInCell(cell));
        } else if (qualifer.equals(COMCODE_QUA)) {
            entity.setCOMCODE(getValueInCell(cell));
        } else if (qualifer.equals(REPORTDATE_QUA)) {
            entity.setREPORTDATE(getValueInCell(cell));
        } else if (qualifer.equals(REPORTHOUR_QUA)) {
            entity.setREPORTHOUR(getValueInCell(cell));
        }
    }

    private void buildEntitySwitch(SnaFinalEntity entity, Cell cell, String qualifer) {
        switch (qualifer) {
        case GROUPID_QUA:
            entity.setGROUPID(getValueInCell(cell));
            break;
        case COUNT_QUA:
            entity.setCOUNT(getValueInCell(cell));
            break;
        case MARK_QUA:
            entity.setMARK(getValueIntInCell(cell));
            break;
        case REGISTNO_QUA:
            entity.setREGISTNO(getValueInCell(cell));
            break;
        case SYS_TYPE_QUA:
            entity.setSys_type(getValueIntInCell(cell));
            break;
        case COMCODE_QUA:
            entity.setCOMCODE(getValueInCell(cell));
            break;
        case REPORTDATE_QUA:
            // TODO 日期格式处理
            entity.setREPORTDATE(getValueDateInCell(cell));
            break;
        case REPORTHOUR_QUA:
            entity.setREPORTHOUR(getValueInCell(cell));
            break;
        case DAMAGESTARTDATE:
            // TODO 日期格式处理
            entity.setDAMAGESTARTDATE(getValueDateInCell(cell));
            break;
        case DAMAGESTARTHOUR:
            entity.setDAMAGESTARTHOUR(getValueInCell(cell));
            break;
        case REPORTORPHONENO:
            entity.setREPORTORPHONENO(getValueInCell(cell));
            break;
        case LINKERMOBILE:
            entity.setLINKERMOBILE(getValueInCell(cell));
            break;
        case DAMAGEAREANAME:
            entity.setDAMAGEAREANAME(getValueInCell(cell));
            break;
        case DAMAGEADDRESS:
            entity.setDAMAGEADDRESS(getValueInCell(cell));
            break;
        case INVOLVEWOUND:
            entity.setINVOLVEWOUND(getValueInCell(cell));
            break;
        case POLICYNO:
            entity.setPOLICYNO(getValueInCell(cell));
            break;
        case STARTDATE:
            entity.setSTARTDATE(getValueInCell(cell));
            break;
        case ENDDATE:
            entity.setENDDATE(getValueInCell(cell));
            break;
        case INSUREDNAME:
            entity.setINSUREDNAME(getValueInCell(cell));
            break;
        case INSUREDTYPE:
            entity.setINSUREDTYPE(getValueInCell(cell));
            break;
        case IDENTIFYTYPE:
            entity.setIDENTIFYTYPE(getValueInCell(cell));
            break;
        case IDENTIFYNUMBER:
            entity.setIDENTIFYNUMBER(getValueInCell(cell));
            break;
        case MOBILE:
            entity.setMOBILE(getValueInCell(cell));
            break;
        case ACTUALLICENSENO_1:
            entity.setACTUALLICENSENO_1(getValueInCell(cell));
            break;
        case VINNO_1:
            entity.setVINNO_1(getValueInCell(cell));
            break;
        case DRIVERNAME_1:
            entity.setDRIVERNAME_1(getValueInCell(cell));
            break;
        case DRIVINGLICENSENO_1:
            entity.setDRIVINGLICENSENO_1(getValueInCell(cell));
            break;
        case MOBILE_1:
            entity.setMOBILE_1(getValueInCell(cell));
            break;
        case REPAIRNAME_1:
            entity.setREPAIRNAME_1(getValueInCell(cell));
            break;
        case IS4S_1:
            entity.setIS4S_1(getValueInCell(cell));
            break;
        case LOSSAPPROVALCODE_1:
            entity.setLOSSAPPROVALCODE_1(getValueInCell(cell));
            break;
        case LOSSAPPROVALNAME_1:
            entity.setLOSSAPPROVALNAME_1(getValueInCell(cell));
            break;
        case SUMVERIFYFINAL_1:
            entity.setSUMVERIFYFINAL_1(getValueInCell(cell));
            break;
        case ITEMNO:
            entity.setITEMNO(getValueInCell(cell));
            break;
        case ACTUALLICENSENO_2:
            entity.setACTUALLICENSENO_2(getValueInCell(cell));
            break;
        case VINNO_2:
            entity.setVINNO_2(getValueInCell(cell));
            break;
        case DRIVERNAME_2:
            entity.setDRIVERNAME_2(getValueInCell(cell));
            break;
        case DRIVINGLICENSENO_2:
            entity.setDRIVINGLICENSENO_2(getValueInCell(cell));
            break;
        case MOBILE_2:
            entity.setMOBILE_2(getValueInCell(cell));
            break;
        case REPAIRNAME_2:
            entity.setREPAIRNAME_2(getValueInCell(cell));
            break;
        case IS4S_2:
            entity.setIS4S_2(getValueInCell(cell));
            break;
        case LOSSAPPROVALCODE_2:
            entity.setLOSSAPPROVALCODE_2(getValueInCell(cell));
            break;
        case LOSSAPPROVALNAME_2:
            entity.setLOSSAPPROVALNAME_2(getValueInCell(cell));
            break;
        case SUMVERIFYFINAL_2:
            // TODO 处理浮点数，保留小数点2位
            entity.setSUMVERIFYFINAL_2(getValueFloatInCell(cell));
            break;
        default:
            LOGGER.info("没有匹配到目标列 {}", qualifer);
        }

    }

    private void buildSnaFinal2Switch(SnaFinal2 entity, Cell cell, String qualifer) {
        switch (qualifer) {
        case FINAL2_GROUPID:
            entity.setGROUPID(getValueInCell(cell));
            break;
        case FINAL2_ITEMNAME:
            entity.setITEMNAME(getValueInCell(cell));
            break;
        case FINAL2_TOUCHROLE:
            entity.setTOUCHROLE(getValueInCell(cell));
            break;
        default:
            LOGGER.info("没有匹配到目标列 {}", qualifer);
        }
    }

    private void buildSnaFinal3Switch(SnaFinal3 entity, Cell cell, String qualifer) {
        switch (qualifer) {
        case FINAL3_GROUPID:
            entity.setGROUPID(getValueInCell(cell));
            break;
        case FINAL3_COUNT:
            entity.setCOUNT(getValueIntInCell(cell));
            break;
        case FINAL3_COMCODE:
            entity.setCOMCODE(getValueInCell(cell));
            break;
        case FINAL3_MARK:
            entity.setCOMCODE(getValueInCell(cell));
            break;
        case FINAL3_MARK01:
            entity.setMARK01(getValueIntInCell(cell));
            break;
        case FINAL3_MARK02:
            entity.setMARK02(getValueIntInCell(cell));
            break;
        case FINAL3_MARK03:
            entity.setMARK03(getValueIntInCell(cell));
            break;
        case FINAL3_MARK03_2:
            entity.setMARK03_2(getValueIntInCell(cell));
            break;
        case FINAL3_MARK04:
            entity.setMARK04(getValueIntInCell(cell));
            break;
        case FINAL3_MARK05:
            entity.setMARK05(getValueIntInCell(cell));
            break;
        case FINAL3_MARK06:
            entity.setMARK06(getValueIntInCell(cell));
            break;
        case FINAL3_MARK07:
            entity.setMARK07(getValueIntInCell(cell));
            break;
        case FINAL3_MARK08:
            entity.setMARK08(getValueIntInCell(cell));
            break;
        case FINAL3_MARK09:
            entity.setMARK09(getValueIntInCell(cell));
            break;
        case FINAL3_MARK10:
            entity.setMARK10(getValueIntInCell(cell));
            break;
        case FINAL3_MARK12:
            entity.setMARK12(getValueIntInCell(cell));
            break;
        case FINAL3_MARK13:
            entity.setMARK13(getValueIntInCell(cell));
            break;
        case FINAL3_MARK14:
            entity.setMARK14(getValueIntInCell(cell));
            break;
        case FINAL3_MARK17:
            entity.setMARK17(getValueIntInCell(cell));
            break;
        default:
            LOGGER.info("没有匹配到目标列 {}", qualifer);
        }
    }

    public SnaFinal2 parseSnaFinal2Data(Result result) {

        SnaFinal2 entity = new SnaFinal2();
        LOGGER.debug("rowkey: {}", Bytes.toString(result.getRow()));
        for (Cell cell : result.listCells()) {
            String qualifer = Bytes
                    .toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            LOGGER.debug("qualifer: {}, value: {}", qualifer,
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
            buildSnaFinal2Switch(entity, cell, qualifer);
        }

        entity.setKEY(UUID.randomUUID().toString().replaceAll("-", ""));

        return entity;
    }

    public SnaFinal3 parseSnaFinal3Data(Result result) {

        SnaFinal3 entity = new SnaFinal3();
        LOGGER.debug("rowkey: {}", Bytes.toString(result.getRow()));
        for (Cell cell : result.listCells()) {
            String qualifer = Bytes
                    .toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            LOGGER.debug("qualifer: {}, value: {}", qualifer,
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
            buildSnaFinal3Switch(entity, cell, qualifer);
        }

        entity.setId(UUID.randomUUID().toString());

        return entity;
    }

    public SnaFinalEntity parseResult2SnaDataOri(Result result, String str) {
        // test(result);

        SnaFinalEntity entity = new SnaFinalEntity();
        LOGGER.debug("rowkey: {}", Bytes.toString(result.getRow()));
        for (Cell cell : result.listCells()) {
            String qualifer = Bytes
                    .toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            LOGGER.debug("qualifer: {}, value: {}", qualifer,
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
            buildEntitySwitch(entity, cell, qualifer);
        }

        entity.setId(Bytes.toString(result.getRow()));

        return entity;
    }

    public SnaFinalEntity parseResult2SnaData(Result result) {
        // test(result);

        SnaFinalEntity entity = new SnaFinalEntity();
        LOGGER.debug("rowkey: {}", Bytes.toString(result.getRow()));
        for (Cell cell : result.listCells()) {
            String qualifer = Bytes
                    .toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            LOGGER.debug("qualifer: {}, value: {}", qualifer,
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
            buildEntitySwitch(entity, cell, qualifer);
        }

        entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));

        return entity;
    }

    public Float getValueFloatInCell(Cell cell) {
        LOGGER.debug("cell Float value: {}", cell.getValueArray());
        Float f = cell == null ?
                -1 :
                Float.valueOf(Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
        return FormatUtils.getFloatByScale(f, 2);
    }

    public Integer getValueIntInCell(Cell cell) {
        LOGGER.debug("cell Int value: {}", cell.getValueArray());
        return cell == null ?
                -1 :
                Integer.valueOf(Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
    }

    public String getValueInCell(Cell cell) {
        return cell == null ? null : Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
    }

    public String getValueDateInCell(Cell cell) {
        String dateStr = cell == null ?
                null :
                Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
        if (dateStr != null && dateStr.contains(".")) {
            dateStr = dateStr.substring(0, dateStr.indexOf("."));
        }
        LOGGER.debug("getValueDateInCell dataStr: {}", dateStr);
        return dateStr;
    }

    public Integer getValueInt(Result result, byte[] qualifier) {
        Cell cell = result.getColumnLatestCell(Constants.f, qualifier);
        return cell == null ? -1 : Bytes.toInt(cell.getValueArray());
    }

    public String getValueDateStr(Result result, byte[] qualifier) {
        Cell cell = result.getColumnLatestCell(Constants.f, qualifier);
        String str = cell == null ? null : Bytes.toString(cell.getValue());
        LOGGER.debug("date str: {}", str);
        return str;
    }

    public String getValueStr(Result result, byte[] qualifier) {
        Cell cell = result.getColumnLatestCell(Constants.f, qualifier);
        String str = cell == null ? null : Bytes.toString(cell.getValue());
        LOGGER.debug("cell : {}, ValueStr : {}", cell, str);
        return cell == null ? null : Bytes.toString(cell.getValue());
    }

    public byte[] getValueByNotNull(Result result, byte[] qualifier) {
        return result.getColumnLatestCell(Constants.f, qualifier).getValue();
    }

}
