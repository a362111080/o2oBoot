package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonInfoDaoTeat {
    @Autowired
    private PersonInfoDao personInfoDao;
    @Test
    public void testAPersonInfo() throws Exception{
        //设置新增用户信息
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("地对地导弹达到");
        personInfo.setGender("nv");
        personInfo.setUserType(1);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        personInfo.setEnableStatus(1);
        int effectedNum = personInfoDao.insertPersonInfo(personInfo);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryPersonInfoById() {
        long userId = 1;
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(userId);
        System.out.println(personInfo.getName());

    }
}
