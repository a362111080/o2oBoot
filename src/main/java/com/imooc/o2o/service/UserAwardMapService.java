package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;

public interface UserAwardMapService {
    /**
     * 根据传入的查询条件分页获取映射列表及总数
     */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据传入的Id获取映射信息
     */
    UserAwardMap getUserAwardMapById(long userAwardMapId);

    /**
     * 领取奖品，添加映射信息
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 修改映射信息，这里主要修改奖品领取状态
     */
    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UnsupportedOperationException;

}
