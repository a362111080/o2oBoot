package com.imooc.o2o.web.wechat;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.wechat.WeiXinUser;
import com.imooc.o2o.util.wechat.WeiXinUserUtil;
import com.imooc.o2o.util.wechat.WeixinUtil;
import com.imooc.o2o.util.wechat.message.pojo.UserAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 * @author lixiang
 *
 */
@Controller
@RequestMapping("wechatlogin")
public class WechatLoginController {

    private static Logger log = LoggerFactory
            .getLogger(WechatLoginController.class);

    @Resource
    private PersonInfoService personInfoService;
    @Resource
    private WechatAuthService WechatAuthService;

    @Resource
    private ShopService shopService;

    @Resource
    private ShopAuthMapService shopAuthMapService;

    private static final String FRONTEND = "1";
    private static final String SHOPEND = "2";

    @RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        //获取微信公众号传输过来的code，通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        //这个state可以用来传我们自定义的信息，方便程序使用，这里也可以不用
       // String roleType = request.getParameter("state");
        log.debug("weixin login code:" + code);
      //  WechatAuth auth = null;
        WeiXinUser user = null;
        String openId = null;
        if (null != code) {
            UserAccessToken token;
            try {
                //通过code获取access_token
                token = WeiXinUserUtil.getUserAccessToken(code);
                log.debug("weixin login token:" + token.toString());
                //通过token获取openId
                String accessToken = token.getAccessToken();
                //通过token获取openId
                openId = token.getOpenId();
                //通过access_token和openId获取用户昵称等信息
                user = WeiXinUserUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);
               // auth = WechatAuthService.getWechatAuthByOpenId(openId);
            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
                        + e.toString());
                e.printStackTrace();
            }
        }
        //====todo begin======
        //前面获取到openId后，可以通过它去数据库判断该微信账号是否在网站有对应账号了，
        //没有的化这里可以自动创建上，直接实现微信与我们网站的无缝对接
        //====todo end===========
        if (user != null) {
            //获取到微信验证的信息后返回da z指定路由（需要自己设定）
            return "frontend/index";
        } else {
            return null;
        }
        }
        }
//        log.debug("weixin login success.");
//        log.debug("login role_type:" + roleType);
//        if (FRONTEND.equals(roleType)) {
//            PersonInfo personInfo = WeiXinUserUtil
//                    .getPersonInfoFromRequest(user);
//            if (auth == null) {
//                personInfo.setCustomerFlag(1);
//                auth = new WechatAuth();
//                auth.setOpenId(openId);
//                auth.setPersonInfo(personInfo);
//                WechatAuthExecution we = WechatAuthService.register(auth, null);
//                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
//                    return null;
//                }
//            }
//            personInfo = personInfoService.getPersonInfoById(auth.getUserId());
//            request.getSession().setAttribute("user", personInfo);
//            return "frontend/index";
//        }
//        if (SHOPEND.equals(roleType)) {
//            PersonInfo personInfo = null;
//            WechatAuthExecution we = null;
//            if (auth == null) {
//                auth = new WechatAuth();
//                auth.setOpenId(openId);
//                personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
//                personInfo.setShopOwnerFlag(1);
//                auth.setPersonInfo(personInfo);
//                we = WechatAuthService.register(auth, null);
//                if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
//                    return null;
//                }
//            }
//            personInfo = personInfoService.getPersonInfoById(auth.getUserId());
//            request.getSession().setAttribute("user", personInfo);
//            ShopExecution se = shopService.getByEmployeeId(personInfo
//                    .getUserId());
//            request.getSession().setAttribute("user", personInfo);
//            if (se.getShopList() == null || se.getShopList().size() <= 0) {
//                return "shop/registershop";
//            } else {
//                request.getSession().setAttribute("shopList", se.getShopList());
//                return "shop/shoplist";
//            }
//        }
//        return null;
//    }
//}
