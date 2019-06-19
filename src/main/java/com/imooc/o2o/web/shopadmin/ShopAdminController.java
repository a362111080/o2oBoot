package com.imooc.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopmanagement")
    //转发至店铺管理页面
    public String shopManagement() {
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/productcategorymanagement",method= {RequestMethod.GET})
    //转发至商品类别管理页面
    public String productcategorymanagement() {
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/productoperation")
    public String productOperation() {
        //转发商品添加/编辑页面
        return "shop/productoperation";
    }

    @RequestMapping(value = "/productmanagement")
    public String productManagement() {
        //转发商品管理页面
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/shopauthmanagement")
    public String shopAuthManagement() {
        //转发至店铺授权页面
        return "shop/shopauthmanagement";
    }

    @RequestMapping(value = "/shopauthedit")
    public String shopAuthEdit() {
        //转发至授权信息修改页面
        return "shop/shopauthedit";
    }

    @RequestMapping(value = "/productbuycheck", method = RequestMethod.GET)
    private String productBuyCheck() {
        //转发至店铺的消费记录页面
        return "shop/productbuycheck";
    }

    @RequestMapping(value = "/usershopcheck", method = RequestMethod.GET)
    private String userShopCheck() {
        //店铺用户积分统计路由
        return "shop/usershopcheck";
    }

    @RequestMapping(value = "/awarddelivercheck", method = RequestMethod.GET)
    private String awardDeliverCheck() {
        return "shop/awarddelivercheck";
    }

    @RequestMapping(value = "/awardmanagement", method = RequestMethod.GET)
    private String awardManagenet() {
        //奖品管理
        return "shop/awardmanagement";
    }

    @RequestMapping(value = "/awardedit")
    private String awardEdit() {
        //奖品编辑
        return "shop/awardedit";
    }
}
