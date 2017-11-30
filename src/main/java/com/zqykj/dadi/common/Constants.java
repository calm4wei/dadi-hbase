package com.zqykj.dadi.common;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by weifeng on 2017/11/23.
 */
public interface Constants {

    byte[] f = Bytes.toBytes(Config.CONFIG.getProperty("hbase.family", "ccic"));
    byte[] GROUPID = Bytes.toBytes("GROUPID");
    byte[] COUNT = Bytes.toBytes("COUNT");
    byte[] MARK = Bytes.toBytes("MARK");
    byte[] REGISTNO = Bytes.toBytes("REGISTNO");
    byte[] sys_type = Bytes.toBytes("sys_type");
    byte[] COMCODE = Bytes.toBytes("COMCODE");
    byte[] REPORTDATE = Bytes.toBytes("REPORTDATE");
    byte[] REPORTHOUR = Bytes.toBytes("REPORTHOUR");

    String GROUPID_QUA = "GROUPID";
    String COUNT_QUA = "COUNT";
    String MARK_QUA = "MARK";
    String REGISTNO_QUA = "REGISTNO";
    String SYS_TYPE_QUA = "SYS_TYPE";
    String COMCODE_QUA = "COMCODE";
    String REPORTDATE_QUA = "REPORTDATE";
    String REPORTHOUR_QUA = "REPORTHOUR";

    String DAMAGESTARTDATE = "DAMAGESTARTDATE";
    String DAMAGESTARTHOUR = "DAMAGESTARTHOUR";
    String REPORTORPHONENO = "REPORTORPHONENO";
    String LINKERMOBILE = "LINKERMOBILE";
    String DAMAGEAREANAME = "DAMAGEAREANAME";
    String DAMAGEADDRESS = "DAMAGEADDRESS";
    String INVOLVEWOUND = "INVOLVEWOUND";
    String POLICYNO = "POLICYNO";
    String STARTDATE = "STARTDATE";
    String ENDDATE = "ENDDATE";
    String INSUREDNAME = "INSUREDNAME";

    String INSUREDTYPE = "INSUREDTYPE";
    String IDENTIFYTYPE = "IDENTIFYTYPE";
    String IDENTIFYNUMBER = "IDENTIFYNUMBER";
    String MOBILE = "MOBILE";
    String ACTUALLICENSENO_1 = "ACTUALLICENSENO_1";
    String VINNO_1 = "VINNO_1";
    String DRIVERNAME_1 = "DRIVERNAME_1";
    String DRIVINGLICENSENO_1 = "DRIVINGLICENSENO_1";
    String MOBILE_1 = "MOBILE_1";
    String REPAIRNAME_1 = "REPAIRNAME_1";
    String IS4S_1 = "IS4S_1";
    String LOSSAPPROVALCODE_1 = "LOSSAPPROVALCODE_1";

    String LOSSAPPROVALNAME_1 = "LOSSAPPROVALNAME_1";
    String SUMVERIFYFINAL_1 = "SUMVERIFYFINAL_1";
    String ITEMNO = "ITEMNO";
    String ACTUALLICENSENO_2 = "ACTUALLICENSENO_2";
    String VINNO_2 = "VINNO_2";
    String DRIVERNAME_2 = "DRIVERNAME_2";
    String DRIVINGLICENSENO_2 = "DRIVINGLICENSENO_2";
    String MOBILE_2 = "MOBILE_2";
    String REPAIRNAME_2 = "REPAIRNAME_2";
    String IS4S_2 = "IS4S_2";
    String LOSSAPPROVALCODE_2 = "LOSSAPPROVALCODE_2";
    String LOSSAPPROVALNAME_2 = "LOSSAPPROVALNAME_2";
    String SUMVERIFYFINAL_2 = "SUMVERIFYFINAL_2";

    /**
     * final2
     */
    String FINAL2_GROUPID = "GROUPID";
    String FINAL2_ITEMNAME = "ITEMNAME";
    String FINAL2_TOUCHROLE = "TOUCHROLE";

    /**
     * final3
     */
    String FINAL3_GROUPID = "GROUPID";
    String FINAL3_COUNT = "COUNT";
    String FINAL3_COMCODE = "COMCODE";
    String FINAL3_MARK = "MARK";
    String FINAL3_MARK01 = "MARK01";
    String FINAL3_MARK02 = "MARK02";
    String FINAL3_MARK03 = "MARK03";
    String FINAL3_MARK03_2 = "MARK03_2";
    String FINAL3_MARK04 = "MARK04";
    String FINAL3_MARK05 = "MARK05";
    String FINAL3_MARK06 = "MARK06";
    String FINAL3_MARK07 = "MARK07";
    String FINAL3_MARK08 = "MARK08";
    String FINAL3_MARK09 = "MARK09";
    String FINAL3_MARK10 = "MARK10";
    String FINAL3_MARK11 = "MARK11";
    String FINAL3_MARK12 = "MARK12";
    String FINAL3_MARK13 = "MARK13";
    String FINAL3_MARK14 = "MARK14";
    String FINAL3_MARK17 = "MARK17";
}
