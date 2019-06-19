package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
    @Autowired
    PersonInfoDao personInfoDao;
    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }

}
