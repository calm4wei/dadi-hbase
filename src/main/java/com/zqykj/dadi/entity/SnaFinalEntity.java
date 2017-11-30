package com.zqykj.dadi.entity;

import java.io.Serializable;

/**
 * Created by weifeng on 2017/11/23.
 */
public class SnaFinalEntity implements Serializable {

    private String id;

    /**
     * 网络号
     */
    private String GROUPID;

    /**
     * 案件数
     */
    private String COUNT;

    /**
     * 总得分
     */
    private Integer MARK;

    /**
     * 报案号
     */
    private String REGISTNO;

    /**
     * 新老系统
     */
    private Integer sys_type;

    /**
     * 承保机构
     */
    private String COMCODE;

    /**
     * 报案日期
     */
    private String REPORTDATE;

    /**
     * 报案时间
     */
    private String REPORTHOUR;

    //******************

    /**
     * 出险日期
     */
    private String DAMAGESTARTDATE;

    /**
     * 出险时间
     */
    private String DAMAGESTARTHOUR;

    /**
     * 报案电话
     */
    private String REPORTORPHONENO;

    /**
     * 报案联系电话
     */
    private String LINKERMOBILE;

    /**
     * 出险区域
     */
    private String DAMAGEAREANAME;

    /**
     * 出险地点
     */
    private String DAMAGEADDRESS;

    /**
     * 是否含人伤
     */
    private String INVOLVEWOUND;

    /**
     * 保单号
     */
    private String POLICYNO;

    /**
     * 起保日期
     */
    private String STARTDATE;

    /**
     * 终保日期
     */
    private String ENDDATE;

    /**
     * 被保险人姓名
     */
    private String INSUREDNAME;

    /**
     * 被保险人类型
     */
    private String INSUREDTYPE;

    /**
     * 被保险人证件类型
     */
    private String IDENTIFYTYPE;

    /**
     * 被保险人证件号
     */
    private String IDENTIFYNUMBER;

    /**
     * 被保险人手机号
     */
    private String MOBILE;

    /**
     * 标的车牌
     */
    private String ACTUALLICENSENO_1;

    /**
     * 标的车架
     */
    private String VINNO_1;

    /**
     * 标的驾驶员
     */
    private String DRIVERNAME_1;

    /**
     * 标的驾照
     */
    private String DRIVINGLICENSENO_1;

    /**
     * 标的电话
     */
    private String MOBILE_1;

    /**
     * 标的修理厂
     */
    private String REPAIRNAME_1;

    /**
     * 标的是否4S维修
     */
    private String IS4S_1;

    /**
     * 标的定损员代码
     */
    private String LOSSAPPROVALCODE_1;

    /**
     * 标的定损员姓名
     */
    private String LOSSAPPROVALNAME_1;

    /**
     * 标的核损金额
     */
    private String SUMVERIFYFINAL_1;

    /**
     * 三者车标识
     */
    private String ITEMNO;

    /**
     * 三者车牌
     */
    private String ACTUALLICENSENO_2;

    /**
     * 三者车架
     */
    private String VINNO_2;

    /**
     * 三者驾驶员
     */
    private String DRIVERNAME_2;

    /**
     * 三者驾照
     */
    private String DRIVINGLICENSENO_2;

    /**
     * 三者电话
     */
    private String MOBILE_2;

    /**
     * 三者修理厂
     */
    private String REPAIRNAME_2;

    /**
     * 三者是否4S维修
     */
    private String IS4S_2;

    /**
     * 三者定损员代码
     */
    private String LOSSAPPROVALCODE_2;

    /**
     * 三者定损员姓名
     */
    private String LOSSAPPROVALNAME_2;

    /**
     * 三者核损金额
     */
    private Float SUMVERIFYFINAL_2;

    public String getLOSSAPPROVALNAME_1() {
        return LOSSAPPROVALNAME_1;
    }

    public void setLOSSAPPROVALNAME_1(String LOSSAPPROVALNAME_1) {
        this.LOSSAPPROVALNAME_1 = LOSSAPPROVALNAME_1;
    }

    public String getSUMVERIFYFINAL_1() {
        return SUMVERIFYFINAL_1;
    }

    public void setSUMVERIFYFINAL_1(String SUMVERIFYFINAL_1) {
        this.SUMVERIFYFINAL_1 = SUMVERIFYFINAL_1;
    }

    public String getITEMNO() {
        return ITEMNO;
    }

    public void setITEMNO(String ITEMNO) {
        this.ITEMNO = ITEMNO;
    }

    public String getACTUALLICENSENO_2() {
        return ACTUALLICENSENO_2;
    }

    public void setACTUALLICENSENO_2(String ACTUALLICENSENO_2) {
        this.ACTUALLICENSENO_2 = ACTUALLICENSENO_2;
    }

    public String getVINNO_2() {
        return VINNO_2;
    }

    public void setVINNO_2(String VINNO_2) {
        this.VINNO_2 = VINNO_2;
    }

    public String getDRIVERNAME_2() {
        return DRIVERNAME_2;
    }

    public void setDRIVERNAME_2(String DRIVERNAME_2) {
        this.DRIVERNAME_2 = DRIVERNAME_2;
    }

    public String getDRIVINGLICENSENO_2() {
        return DRIVINGLICENSENO_2;
    }

    public void setDRIVINGLICENSENO_2(String DRIVINGLICENSENO_2) {
        this.DRIVINGLICENSENO_2 = DRIVINGLICENSENO_2;
    }

    public String getMOBILE_2() {
        return MOBILE_2;
    }

    public void setMOBILE_2(String MOBILE_2) {
        this.MOBILE_2 = MOBILE_2;
    }

    public String getREPAIRNAME_2() {
        return REPAIRNAME_2;
    }

    public void setREPAIRNAME_2(String REPAIRNAME_2) {
        this.REPAIRNAME_2 = REPAIRNAME_2;
    }

    public String getIS4S_2() {
        return IS4S_2;
    }

    public void setIS4S_2(String IS4S_2) {
        this.IS4S_2 = IS4S_2;
    }

    public String getLOSSAPPROVALCODE_2() {
        return LOSSAPPROVALCODE_2;
    }

    public void setLOSSAPPROVALCODE_2(String LOSSAPPROVALCODE_2) {
        this.LOSSAPPROVALCODE_2 = LOSSAPPROVALCODE_2;
    }

    public String getLOSSAPPROVALNAME_2() {
        return LOSSAPPROVALNAME_2;
    }

    public void setLOSSAPPROVALNAME_2(String LOSSAPPROVALNAME_2) {
        this.LOSSAPPROVALNAME_2 = LOSSAPPROVALNAME_2;
    }

    public Float getSUMVERIFYFINAL_2() {
        return SUMVERIFYFINAL_2;
    }

    public void setSUMVERIFYFINAL_2(Float SUMVERIFYFINAL_2) {
        this.SUMVERIFYFINAL_2 = SUMVERIFYFINAL_2;
    }

    public String getACTUALLICENSENO_1() {
        return ACTUALLICENSENO_1;
    }

    public void setACTUALLICENSENO_1(String ACTUALLICENSENO_1) {
        this.ACTUALLICENSENO_1 = ACTUALLICENSENO_1;
    }

    public String getVINNO_1() {
        return VINNO_1;
    }

    public void setVINNO_1(String VINNO_1) {
        this.VINNO_1 = VINNO_1;
    }

    public String getDRIVERNAME_1() {
        return DRIVERNAME_1;
    }

    public void setDRIVERNAME_1(String DRIVERNAME_1) {
        this.DRIVERNAME_1 = DRIVERNAME_1;
    }

    public String getDRIVINGLICENSENO_1() {
        return DRIVINGLICENSENO_1;
    }

    public void setDRIVINGLICENSENO_1(String DRIVINGLICENSENO_1) {
        this.DRIVINGLICENSENO_1 = DRIVINGLICENSENO_1;
    }

    public String getMOBILE_1() {
        return MOBILE_1;
    }

    public void setMOBILE_1(String MOBILE_1) {
        this.MOBILE_1 = MOBILE_1;
    }

    public String getREPAIRNAME_1() {
        return REPAIRNAME_1;
    }

    public void setREPAIRNAME_1(String REPAIRNAME_1) {
        this.REPAIRNAME_1 = REPAIRNAME_1;
    }

    public String getIS4S_1() {
        return IS4S_1;
    }

    public void setIS4S_1(String IS4S_1) {
        this.IS4S_1 = IS4S_1;
    }

    public String getLOSSAPPROVALCODE_1() {
        return LOSSAPPROVALCODE_1;
    }

    public void setLOSSAPPROVALCODE_1(String LOSSAPPROVALCODE_1) {
        this.LOSSAPPROVALCODE_1 = LOSSAPPROVALCODE_1;
    }

    public String getDAMAGESTARTDATE() {
        return DAMAGESTARTDATE;
    }

    public void setDAMAGESTARTDATE(String DAMAGESTARTDATE) {
        this.DAMAGESTARTDATE = DAMAGESTARTDATE;
    }

    public String getDAMAGESTARTHOUR() {
        return DAMAGESTARTHOUR;
    }

    public void setDAMAGESTARTHOUR(String DAMAGESTARTHOUR) {
        this.DAMAGESTARTHOUR = DAMAGESTARTHOUR;
    }

    public String getREPORTORPHONENO() {
        return REPORTORPHONENO;
    }

    public void setREPORTORPHONENO(String REPORTORPHONENO) {
        this.REPORTORPHONENO = REPORTORPHONENO;
    }

    public String getLINKERMOBILE() {
        return LINKERMOBILE;
    }

    public void setLINKERMOBILE(String LINKERMOBILE) {
        this.LINKERMOBILE = LINKERMOBILE;
    }

    public String getDAMAGEAREANAME() {
        return DAMAGEAREANAME;
    }

    public void setDAMAGEAREANAME(String DAMAGEAREANAME) {
        this.DAMAGEAREANAME = DAMAGEAREANAME;
    }

    public String getDAMAGEADDRESS() {
        return DAMAGEADDRESS;
    }

    public void setDAMAGEADDRESS(String DAMAGEADDRESS) {
        this.DAMAGEADDRESS = DAMAGEADDRESS;
    }

    public String getINVOLVEWOUND() {
        return INVOLVEWOUND;
    }

    public void setINVOLVEWOUND(String INVOLVEWOUND) {
        this.INVOLVEWOUND = INVOLVEWOUND;
    }

    public String getPOLICYNO() {
        return POLICYNO;
    }

    public void setPOLICYNO(String POLICYNO) {
        this.POLICYNO = POLICYNO;
    }

    public String getSTARTDATE() {
        return STARTDATE;
    }

    public void setSTARTDATE(String STARTDATE) {
        this.STARTDATE = STARTDATE;
    }

    public String getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(String ENDDATE) {
        this.ENDDATE = ENDDATE;
    }

    public String getINSUREDNAME() {
        return INSUREDNAME;
    }

    public void setINSUREDNAME(String INSUREDNAME) {
        this.INSUREDNAME = INSUREDNAME;
    }

    public String getINSUREDTYPE() {
        return INSUREDTYPE;
    }

    public void setINSUREDTYPE(String INSUREDTYPE) {
        this.INSUREDTYPE = INSUREDTYPE;
    }

    public String getIDENTIFYTYPE() {
        return IDENTIFYTYPE;
    }

    public void setIDENTIFYTYPE(String IDENTIFYTYPE) {
        this.IDENTIFYTYPE = IDENTIFYTYPE;
    }

    public String getIDENTIFYNUMBER() {
        return IDENTIFYNUMBER;
    }

    public void setIDENTIFYNUMBER(String IDENTIFYNUMBER) {
        this.IDENTIFYNUMBER = IDENTIFYNUMBER;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGROUPID() {
        return GROUPID;
    }

    public void setGROUPID(String GROUPID) {
        this.GROUPID = GROUPID;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public Integer getMARK() {
        return MARK;
    }

    public void setMARK(Integer MARK) {
        this.MARK = MARK;
    }

    public String getREGISTNO() {
        return REGISTNO;
    }

    public void setREGISTNO(String REGISTNO) {
        this.REGISTNO = REGISTNO;
    }

    public Integer getSys_type() {
        return sys_type;
    }

    public void setSys_type(Integer sys_type) {
        this.sys_type = sys_type;
    }

    public String getCOMCODE() {
        return COMCODE;
    }

    public void setCOMCODE(String COMCODE) {
        this.COMCODE = COMCODE;
    }

    public String getREPORTDATE() {
        return REPORTDATE;
    }

    public void setREPORTDATE(String REPORTDATE) {
        this.REPORTDATE = REPORTDATE;
    }

    public String getREPORTHOUR() {
        return REPORTHOUR;
    }

    public void setREPORTHOUR(String REPORTHOUR) {
        this.REPORTHOUR = REPORTHOUR;
    }
}
