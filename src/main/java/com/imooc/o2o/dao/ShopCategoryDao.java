package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {

	/**
	 * 列出店铺种类
	 * @param shopcategory
	 * @return
	 */
	List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopcategory);
}
