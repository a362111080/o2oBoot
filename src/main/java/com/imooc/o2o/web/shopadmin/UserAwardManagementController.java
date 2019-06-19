package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WeiXinUserUtil;
import com.imooc.o2o.util.wechat.WeixinUtil;
import com.imooc.o2o.util.wechat.message.pojo.UserAccessToken;
import com.imooc.o2o.util.wechat.message.req.WechatInfo;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class UserAwardManagementController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private  PersonInfoService personInfoService;

    @RequestMapping(value = "/listuserawardmapsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //从session里获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && currentShop.getShopId() != null) {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setShop(currentShop);
            //从请求中获取奖品名
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                //如果按奖品名搜索，则添加搜索条件
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            //分页返回结果
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMap, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ue.getUserAwardMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }
    @RequestMapping(value = "/exchangeaward", method = RequestMethod.POST)
    @ResponseBody
    private String exchangeAward(HttpServletRequest request) throws UnsupportedEncodingException {
        //获取负责扫描二维码的店员信息
        WechatAuth auth = getOperatorInfo(request);
        if (auth != null) {
            //通过userId获取店员信息
            PersonInfo operator = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            //解析微信传过来的自定义参数state，由于之前进行了编码，这里要解码
            String qrCodeinfo = new String(
                    URLDecoder.decode((HttpServletRequestUtil.getString(request, "state")), "UTF-8"));
            ObjectMapper mapper = new ObjectMapper();
            WechatInfo wechatInfo = null;
            try {
                //将解码后的内容用aaa去替换之前生成二维码的时候加入的aaa前缀，转换成WechaInfo实体类
                wechatInfo = mapper.readValue(qrCodeinfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {
                return "shop/operationfail";
            }
            //校验二维码是否已过期
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }
            //获取用户奖品映射主键
            Long userAwardId = wechatInfo.getUserAwardId();
            //获取顾客信息id
            Long customerId = wechatInfo.getCustomerId();
            //将顾客信息，操作员以及奖品信息封装成userAwardMap
            UserAwardMap userAwardMap = compactUserAwardMap4Exchange(customerId, userAwardId, operator);
            //获取添加消费记录所需要的参数并组件成userproductmap实例
            if (userAwardMap != null) {
                try {
                    //检查该员工是否具有扫码权限
                    if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
                        return "shop/operationfail";
                    }
                    //修改奖品的领取状态
                    UserAwardMapExecution se = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (se.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                        return "shop/operationfail";
                    }
                } catch (RuntimeException e) {
                    return "shop/operationfail";
                }
            }

        }
        return "shop/operationfail";
    }

    /**
     * 获取扫描二维码的店员信息
     */
    private WechatAuth getOperatorInfo(HttpServletRequest request) {
        String code = request.getParameter("code");
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                token = WeiXinUserUtil.getUserAccessToken(code);
                String openId = token.getOpenId();
                request.getSession().setAttribute("openId", openId);
                auth = wechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return auth;
    }
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getProductId() != null
                && wechatInfo.getCustomerId() != null
                && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            if ((nowTime - wechatInfo.getCreateTime()) <= 5000) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private UserAwardMap compactUserAwardMap4Exchange(Long customerId, Long userAwardId, PersonInfo operator) {
        UserAwardMap userAwardMap = null;
        if (customerId != null && userAwardId != null&&operator!=null) {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setUserId(customerId);
            userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            userAwardMap.setUser(personInfo);
            userAwardMap.setOperator(operator);
            userAwardMap.setUsedStatus(0);
        }
        return userAwardMap;
    }
    private boolean checkShopAuth(long userId,UserAwardMap userAwardMap) {
        //获取该店铺的所有授权信息
        UserAwardMapExecution userAwardMapExecution = userAwardMapService.listUserAwardMap(userAwardMap, 0, 100);
        for (UserAwardMap uam : userAwardMapExecution
                //看看是否给过该人员进行授权
                .getUserAwardMapList()) {
            if (uam.getUser().getUserId() == userId) {
                return true;
            }
        }
        return false;
    }


}
