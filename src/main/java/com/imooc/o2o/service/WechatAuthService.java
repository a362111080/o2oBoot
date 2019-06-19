package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {
    /**
     * 通过openId查找平台对应的微信账号
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     */
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
