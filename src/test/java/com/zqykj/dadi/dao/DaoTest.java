package com.zqykj.dadi.dao;

import com.zqykj.dadi.entity.SnaFinalEntity;
import com.zqykj.dadi.util.DataSourceC3p0;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by weifeng on 2017/11/23.
 */
public class DaoTest {

    SnaDao dao = new SnaDao();

    @Test
    public void testMysqlGetById() {
        dao.getById("1");
    }

    @Test
    public void testMysqlInsert() {
        List<SnaFinalEntity> list = new ArrayList<SnaFinalEntity>();
        SnaFinalEntity entity = new SnaFinalEntity();
        entity.setGROUPID("22223333");
        entity.setCOUNT("123123");
        entity.setREPORTHOUR("12:10:11");
        entity.setMARK(90);
        entity.setREPORTDATE("2017-10-12");
        entity.setSys_type(null);
        entity.setREPORTHOUR("09:10:22");
        entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        list.add(entity);
        dao.insertBatch(list);
    }

    @Test
    public void testClearData() {
        dao.clearData();
    }

    @Test
    public void testDataSourceC3p0() {
        Connection connection = DataSourceC3p0.getConnection();
        System.out.println(connection);
    }

}
