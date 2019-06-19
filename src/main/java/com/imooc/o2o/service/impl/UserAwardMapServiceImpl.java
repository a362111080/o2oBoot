package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.UserAwardMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.exceptions.UserAwardMapOpeartionException;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.PageCalculator;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
    @Autowired
    private UserAwardMapDao userAwardMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize) {
        //空值判断
        if (userAwardMapCondition != null && pageIndex != null && pageSize != null) {
            //页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            //分页查询领取奖品的信息列表
            List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMapCondition, beginIndex, pageSize);
            //返回总数
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardMapCondition);
            UserAwardMapExecution ue = new UserAwardMapExecution();
            ue.setUserAwardMapList(userAwardMapList);
            ue.setCount(count);
            return ue;
        } else {
            return null;
        }
    }

    @Override
    public UserAwardMap getUserAwardMapById(long userAwardMapId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
    }

    @Override
    @Transactional
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) {
        //空值判断，确定userId和shopId不为空
        if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null
                && userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {
            //设置默认值
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            try {
                int effecteNum = 0;
                //若该奖品需要消耗积分，则将tb_shop_map对应的用户积分抵扣
                if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
                    //根据用户Id和店铺Id获取该用户在店铺的积分
                    UserShopMap userShopMap = userShopMapDao.queryUserShopMap(userAwardMap.getUser().getUserId(),
                            userAwardMap.getShop().getShopId());
                    //判断该用户在店铺里是否有积分
                    if (userShopMap != null) {
                        //若有积分必须确保店铺积分大于本次要兑换奖品的积分
                        if (userShopMap.getPoint() >= userAwardMap.getPoint()) {
                            //积分抵扣
                            userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
                            //更新积分信息
                            effecteNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                            if (effecteNum < 0) {
                                throw new UserAwardMapOpeartionException("更新积分信息失败");
                            }
                        } else {
                            throw new UserAwardMapOpeartionException("积分不足无法领取");
                        }
                    } else {
                        throw new UserAwardMapOpeartionException("在本店铺没有积分，无法领取");
                    }
                }
                //插入礼品兑换信息
                effecteNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (effecteNum <= 0) {
                    throw new UserAwardMapOpeartionException("领取奖品失败");
                }
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
            } catch (Exception e) {
                throw new UnsupportedOperationException("领取奖励失败" + e.toString());
            }

        } else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    @Override
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UnsupportedOperationException {
        //空值判断，主要是检验传入的userAwardId以及领取状态是否为空
        if (userAwardMap == null || userAwardMap.getUserAwardId() == null || userAwardMap.getUsedStatus() == null) {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERPRODUCT_ID);
        } else {
            try {
                //更新可用状态
                int effectedNum = userAwardMapDao.updateUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USERPRODUCT_INFO);
                } else {
                    return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMap);
                }
            } catch (Exception e) {
                throw new UserAwardMapOpeartionException("modifyUserAwardId error" + e.getMessage());
            }
        }
    }
}
