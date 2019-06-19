package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
    /**
     * 通过账号和密码获取平台账号信息
     */
    LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password);

    /**
     * 通过userId获取平台账号信息
     */
    LocalAuth getLocalAutByUserId(long userId);

    /**
     * 由于我没有买服务器，无法绑定微信
     * 这里只能单纯的添加username和password到本地
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 修改平台账号的登陆密码
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
            throws LocalAuthOperationException;
}
