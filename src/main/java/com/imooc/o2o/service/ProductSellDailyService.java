package com.imooc.o2o.service;

import com.imooc.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

public interface ProductSellDailyService {
    /**
     * 每日定时对所有的店铺的商品销量进行统计
     */
    void dailyCalculate();

    /**
     * 根据查询条件返回商品日销售的统计列表
     */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
                                                Date endTime);
}
