package com.zqykj.dadi.dao;

import com.zqykj.dadi.entity.SnaFinal2;
import com.zqykj.dadi.entity.SnaFinal3;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weifeng on 2017/11/28.
 */
public class Final3DaoTest {

    @Test
    public void testInsert() {
        SnaFinal3Dao dao = new SnaFinal3Dao();
        List<SnaFinal3> list = new ArrayList<>();
        SnaFinal3 entity = new SnaFinal3();
        entity.setId("asdfa");
        entity.setMARK(10);
        list.add(entity);
        dao.insertBatch(list);
    }
}
