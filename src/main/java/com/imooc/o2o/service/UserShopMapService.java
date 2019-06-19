package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;

public interface UserShopMapService {
    /**
     * 根据传入的查询信息分页查询用户积分列表
     */
    UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, int pageIndex,
                                         int pageSize);

    /**
     * 根据用户Id和店铺Id返回该用户在某个店铺的积分情况
     */
    UserShopMap getShopUserMap(long userId, long shopId);
}
