package com.imooc.o2o.dao;

import com.imooc.o2o.entity.HeadLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadLineDao {
    /**
     * 根据转入的查询条件（头条名查询头条）
     * @param
     * @return
     */
    List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);
}
