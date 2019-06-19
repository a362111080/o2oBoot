package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthDaoTset {
    @Autowired
    private WechaAuthDao wechaAuthDao;
    @Test
    public void testAInsertWechatAuth()throws Exception {
        //新增微信账号
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        //给微信账号绑定上用户信息
        wechatAuth.setPersonInfo(personInfo);
        //随意设置上openId
        wechatAuth.setOpenId("sdgewgsfsd");
        wechatAuth.setCreateTime(new Date());
        int effectedNum = wechaAuthDao.insertWechatAuth(wechatAuth);
        assertEquals(1, effectedNum);
    }
    @Test
    public void testBQueryWechatAuthByOpenId() throws Exception {
        WechatAuth wechatAuth = wechaAuthDao.queryWechatInfoByOpenId("sdgewgsfsd");
        String naem = wechatAuth.getPersonInfo().getName();
        System.out.println(naem);
    }
}
