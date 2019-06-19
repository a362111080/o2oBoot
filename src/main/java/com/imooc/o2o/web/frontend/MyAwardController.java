package com.imooc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.PersonInfoService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.baidu.ShortNetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping("/frontend")
public class MyAwardController {
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private PersonInfoService personInfoService;

    @RequestMapping(value = "/getawardbyuserawardid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取前端传递过来的userAwardId
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        //空值判断
        if (userAwardId > -1) {
            //根据Id获取顾客奖品的映射，进而获取奖品Id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            //根据奖品Id获取奖品信息
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            //将奖品信息和领取状态返回给前端
            modelMap.put("award", award);
            modelMap.put("userAwardMap", userAwardMap);
            modelMap.put("usedStatus", userAwardMap.getUsedStatus());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty awardId");
        }
        return modelMap;
    }
    //微信获取用户信息的api
    private static String urlPrefix;
    //微信获取用户信息的api中间部分
    private static String urlMiddle;
    //微信获取用户信息的api的后缀
    private static String urlSuffix;
    //微信回传给的响应添加顾客商品映射信息的url
    private static String exchangeUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        MyAwardController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        MyAwardController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        MyAwardController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.exchange.url}")
    public void setexchangeUrl(String exchangeUrl) {
        MyAwardController.exchangeUrl = exchangeUrl;
    }

    @RequestMapping(value = "/generateqrcode4award", method = RequestMethod.GET)
    @ResponseBody

    private void generateQRCode4Product(HttpServletRequest request,
                                        HttpServletResponse response) {
        //获取前端传递过来的用户奖品映射
        long userAwardId = HttpServletRequestUtil.getLong(request,
                "userAwardId");
        //根据Id获取顾客奖品映射实体类
        UserAwardMap userAwardMap = userAwardMapService
                .getUserAwardMapById(userAwardId);
        //从session获取顾客信息
        PersonInfo user = (PersonInfo) request.getSession()
                .getAttribute("user");
        //空值判断
        if (userAwardMap != null && user != null && user.getUserId() != null
                && userAwardMap.getUser().getUserId() == user.getUserId()) {
            //获取当前时间戳，以保证二维码的时间有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();
            //将商品Id，顾客Id和timestamp传入content赋值到state中，这样微信获取到这些信息后会回传到用户
            //加上aaa是为了一个会的在添加信息的方法里替换这些信息使用
            String content = "{aaaproductIdaaa:" + userAwardId + ",aaacustomerIdaaa:"
                    + user.getUserId() + ",aaacreateTimeaaa:" + timpStamp + "}";
            //将conten的信息先进行base64编码以及避免特殊字符造成的干扰，之后拼接目标URL
            try {
                String longUrl = urlPrefix + exchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8")
                        + urlSuffix;
                //将目标URL转换成短的URL
                String shortUrl = ShortNetAddress.getShortURL(longUrl);
                //调用二维码生成的工具类方法，传入短的URL，生成二维码
                BitMatrix qRcodeImg = CodeUtil.generateQRCodeStream(shortUrl,
                        response);
                //将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRcodeImg, "png",
                        response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {
        UserAwardMap userAwardMap = null;
        if (user != null && user.getUserId() != null && awardId != -1) {
            userAwardMap = new UserAwardMap();
            PersonInfo personInfo = personInfoService.getPersonInfoById(user
                    .getUserId());
            Award award = awardService.getAwardById(awardId);
            user.setName(personInfo.getName());
            userAwardMap.setUser(user);
            userAwardMap.setAward(award);
            userAwardMap.setPoint(award.getPoint());
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(1);
        }
        return userAwardMap;
    }

}
