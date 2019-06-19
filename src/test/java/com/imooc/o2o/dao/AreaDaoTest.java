package com.imooc.o2o.dao;

import com.imooc.o2o.O2oApplication;
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={O2oApplication.class})
public class AreaDaoTest{
	@Autowired
	private AreaDao areaDao;

	@Test
	public void testQueryArea() {
		List<Area> areaList=areaDao.queryArea();
		assertEquals(2, areaList.size());
	}

}
