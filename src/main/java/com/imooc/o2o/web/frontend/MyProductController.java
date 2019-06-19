package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class MyProductController {
    @Autowired
    private UserProductMapService userProductMapService;

    /**
     * 列出某个顾客的商品消费信息
     */
    @RequestMapping(value = "/listuserproductmapsbycustomer", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserProductMapsByCustomer(
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session里面获取顾客信息
        PersonInfo user = (PersonInfo) request.getSession()
                .getAttribute("user");
//        /空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (user != null)
                && (user.getUserId() != -1)) {
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId > -1) {
                //如果传入店铺信息，则列出某个店铺下该店铺的消费历史
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userProductMapCondition.setShop(shop);
            }
            String productName = HttpServletRequestUtil.getString(request,
                    "productName");
            if (productName != null) {
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }
            //根据查询条件分页返回用户消费信息
            UserProductMapExecution ue = userProductMapService
                    .listUserProductMap(userProductMapCondition, pageIndex,
                            pageSize);
            modelMap.put("userProductMapList", ue.getUserProductMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }
}
