package com.imooc.o2o.dao;

import com.imooc.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAwardMapDao {
    /**
     * 根据传入进来的查询条件分页返回用户兑换奖品记录的列表信息
     */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardCondition") UserAwardMap userAwardMap,
                                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);


    /**
     * 配合queryUserAwardMapList返回相同查询条件下的兑换奖品记录数
     */
    int queryUserAwardMapCount(@Param("userAwardCondition") UserAwardMap userAwardCondition);

    /**
     * 根据userAwardId返回某条奖品兑换信息
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);

    /**
     * 添加一条奖品记录
     */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 跟新奖品兑换信息，主要跟新奖品领取状态
     */
    int updateUserAwardMap(UserAwardMap userAwardMap);
}
