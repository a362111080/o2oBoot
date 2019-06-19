package com.imooc.o2o.dao;

import com.imooc.o2o.entity.WechatAuth;

public interface WechaAuthDao {
    /**
     * 通过openId查询对应本平台的微信账号
     */
    WechatAuth queryWechatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信
     */
    int insertWechatAuth(WechatAuth wechatAuth);
}
