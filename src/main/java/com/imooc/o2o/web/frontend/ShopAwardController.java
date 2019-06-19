package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.UserShopMapService;
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
public class ShopAwardController {
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserShopMapService userShopMapService;
    @Autowired
    private UserAwardMapService userAwardMapService;

    /**
     * 展示某店铺奖品列表及该用户积分
     */
    @RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取店铺Id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (shopId > -1)) {
            //获取前端可能输入的奖品模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            Award awardCondition = compactAwardCondition4Search(shopId, awardName);
            //传入条件查询奖品信息
            AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            modelMap.put("awardList", ae.getAwardList());
            modelMap.put("count", ae.getCount());
            modelMap.put("success", true);
            //从Session中获取用户信息，主要是为了显示该用户在本店铺的积分
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            //空值判断
            if (user != null && user.getUserId() != null) {
                //获取积分信息
                UserShopMap userShopMap = userShopMapService.getShopUserMap(user.getUserId(), shopId);
                if (userShopMap == null) {
                    modelMap.put("totalPoint", 0);
                } else
                    modelMap.put("totalPoint", userShopMap.getPoint());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/adduserawardmap", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addUserAwardMap(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String,Object>();
        //从session获取用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        //从前端请求中获取奖品Id
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        //封装成用户奖品映射对象
        UserAwardMap userAwardMapCondition = new UserAwardMap();
        Award award = null;
        Shop shop = new Shop();
        if (user.getUserId() != null && awardId != null) {
            award = awardService.getAwardById(awardId);
            shop.setShopId(award.getShopId());
            userAwardMapCondition.setUser(user);
            userAwardMapCondition.setAward(award);
            userAwardMapCondition.setShop(shop);
            if (userAwardMapCondition != null) {
                try {
                    //添加
                    UserAwardMapExecution se = userAwardMapService.addUserAwardMap(userAwardMapCondition);
                    if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                        modelMap.put("success", true);
                    } else {
                        modelMap.put("success", false);
                        modelMap.put("errMsg", se.getStateInfo());
                    }
                } catch (RuntimeException e) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", e.toString());
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "请选择领取的奖品");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择领取的奖品");

        }
        return modelMap;
    }
    private Award compactAwardCondition4Search(long shopId, String awardName) {
        Award awardCondition = new Award();
        awardCondition.setShopId(shopId);
        if (awardName != null) {
            awardCondition.setAwardName(awardName);
        }
        return awardCondition;
    }

}

