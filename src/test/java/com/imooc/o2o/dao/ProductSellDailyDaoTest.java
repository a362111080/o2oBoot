package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.ws.Action;

import java.util.List;

import static net.sf.ezmorph.test.ArrayAssertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;
    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertProductSellDaily() throws Exception {
        //创建商品日销量统计
        int effectedNum = productSellDailyDao.insertProductSellDaily();
        assertEquals(3, effectedNum);
    }
    /**
     * 测试添加功能
     */
    @Test
    public void testBInsertDefaultProductSellDaily() throws Exception {
        //创建商品日销量统计
        int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
        assertEquals(2, effectedNum);
    }
    /**
     * 测试查询功能
     */
    @Test
    public void testCQueryProductSellDaily() throws Exception {
        ProductSellDaily productSellDaily = new ProductSellDaily();
        //叠加店铺去查询
        Shop shop = new Shop();
        shop.setShopId(17L);
        productSellDaily.setShop(shop);
        List<ProductSellDaily> productSellDailyList = productSellDailyDao.queryProductSellDailyList(productSellDaily,
                null, null);
        assertEquals(5, productSellDailyList.size());
    }
}
