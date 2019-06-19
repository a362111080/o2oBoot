package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductSellDailyDao;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductSellDailServiceImpl implements ProductSellDailyService {
    private static final Logger log = LoggerFactory.getLogger(ProductSellDailServiceImpl.class);
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        log.info("Quartz跑起来了");
        productSellDailyDao.insertProductSellDaily();
//        System.out.println("Quartz跑起来了");

    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }
}
