package com.imooc.o2o.dao;

import com.imooc.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {
    /**
     * 通过账号和密码查询对应信息，登陆用
     */
    LocalAuth queryLocalByUserNameAndPwd(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户Id查询对应localauth
     */
    LocalAuth queryLocalByUserId(@Param("userId") long userId);

    /**
     * 添加平台账号
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 通过userId,username，password更改密码
     */
    int updatelLocalAuth(@Param("userId") Long userId, @Param("username") String username,
                         @Param("password") String password, @Param("newPassword") String newPassword,
                         @Param("lastEditTime") Date lastEditTime);
}
