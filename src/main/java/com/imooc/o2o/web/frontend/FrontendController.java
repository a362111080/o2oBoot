package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {
    @RequestMapping(value = "/index",method= RequestMethod.GET)
    private String index() {
        return "frontend/index";
    }

    /**
     * 商品列表页路由
     */
    @RequestMapping(value = "/shoplist", method = RequestMethod.GET)
    private String shopShopList() {
        return "frontend/shoplist";
    }

    /**
     * 店铺详情页路由
     */
    @RequestMapping(value = "/shopdetail", method = RequestMethod.GET)
    private String showProductDetail() {
        return "frontend/shopdetail";
    }

    @RequestMapping(value = "/productdetail",method = RequestMethod.GET )
    private String productdetail(){
        return "frontend/productdetail";
    }
    /**
     * 奖品编辑
     */
    @RequestMapping(value = "/awardlist", method = RequestMethod.GET)
    private String awardEdit() {
        return "frontend/awardlist";
    }
    /**
     * 我的奖品列表
     */
    @RequestMapping(value = "/myawarddetail", method = RequestMethod.GET)
    private String myAwardDetail() {
        return "frontend/myawarddetail";
    }
    /**
     * 我的积分列表
     */
    @RequestMapping(value = "/mypoint", method = RequestMethod.GET)
    private String myPoint() {
        return "frontend/mypoint";
    }
    /**
     * 消费记录
     */
    @RequestMapping(value = "/myrecord", method = RequestMethod.GET)
    private String myRecord() {
        return "frontend/myrecord";
    }
    /**
     * 积分记录
     */
    @RequestMapping(value = "/pointrecord", method = RequestMethod.GET)
    private String pointRecord() {
        return "frontend/pointrecord";
    }
}
