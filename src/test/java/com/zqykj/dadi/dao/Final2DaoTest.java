package com.zqykj.dadi.dao;

import com.zqykj.dadi.entity.SnaFinal2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weifeng on 2017/11/28.
 */
public class Final2DaoTest {

    @Test
    public void testInsert(){
        SnaFinal2Dao dao = new SnaFinal2Dao();
        List<SnaFinal2> list = new ArrayList<>();
        SnaFinal2 entity = new SnaFinal2();
        entity.setKEY("qwerq");
        entity.setGROUPID("3qwerq");
        entity.setITEMNAME("test");
        entity.setTOUCHROLE("test");
        list.add(entity);
        dao.insertBatch(list);
    }
}
