package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserShopMapDaoTest {
    @Autowired
    private UserShopMapDao userShopMapDao;

    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertUserShopMap() {
        //创建用户店铺积分统计信息1
        UserShopMap userShopMap = new UserShopMap();
        PersonInfo customer = new PersonInfo();
        customer.setUserId(1L);
        userShopMap.setUser(customer);
        Shop shop = new Shop();
        shop.setShopId(17L);
        userShopMap.setShop(shop);
        userShopMap.setCreateTime(new Date());
        userShopMap.setPoint(1);
        int effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
        //创建用户店铺积分统计信息2
        UserShopMap userShopMap2 = new UserShopMap();
        PersonInfo customer2 = new PersonInfo();
        customer2.setUserId(2L);
        userShopMap2.setUser(customer2);
        Shop shop2 = new Shop();
        shop2.setShopId(20L);
        userShopMap2.setShop(shop2);
        userShopMap2.setCreateTime(new Date());
        userShopMap2.setPoint(1);
        effectedNum = userShopMapDao.insertUserShopMap(userShopMap2);
        assertEquals(1, effectedNum);
    }

    /**
     * 测试查询功能
     */
    @Test
    public void tsetQueryUserShopMap() throws Exception {
        UserShopMap userShopMap = new UserShopMap();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("2");
        userShopMap.setUser(personInfo);
        //查全部
        List<UserShopMap> userUserShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 2);
        System.out.println(userUserShopMapList);

//        assertEquals(2, userUserShopMapList.size());
//        int count = userShopMapDao.queryUserShopMapCount(userShopMap);
//        assertEquals(2, count);
//        //按店铺去查询
//        Shop shop = new Shop();
//        shop.setShopId(17L);
//        userShopMap.setShop(shop);
//        userUserShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 3);
//        assertEquals(1, userUserShopMapList.size());
//        count = userShopMapDao.queryUserShopMapCount(userShopMap);
//        assertEquals(1, count);
//        //按用户Id和店铺查询
//        userShopMap = userShopMapDao.queryUserShopMap(1, 17);
//        assertEquals("测试",userShopMap.getUser().getName());
    }
    /**
     * 测试更新功能
     */
    @Test
    public void testCUpdateUserShopMap() throws Exception {
        UserShopMap userShopMap = new UserShopMap();
        userShopMap = userShopMapDao.queryUserShopMap(1, 17);
        assertTrue("Error,积分不一致！", 1 == userShopMap.getPoint());
        userShopMap.setPoint(2);
        int effecteNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
        assertEquals(1, effecteNum);

    }

}
