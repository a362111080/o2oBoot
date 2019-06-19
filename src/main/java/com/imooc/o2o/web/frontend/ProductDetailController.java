package com.imooc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.service.ProductService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {
    @Autowired
    private ProductService productService;

    private static String URLPREFIX = "https://open.weixin.qq.com/connect/oauth2/authorize?"
            + "appid=wxd7f6c5b8899fba83&"
            + "redirect_uri=115.28.159.6/myo2o/shop/adduserproductmap&"
            + "response_type=code&scope=snsapi_userinfo&state=";
    private static String URLSUFFIX = "#wechat_redirect";

    @RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductDetailPageInfo(
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取前台传过来的productId
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        Product product = null;
        //空值判断
        if (productId != -1) {
            //根据productId获取商品详情信息，包含商品详情图列表
            product = productService.getProductById(productId);
            //新增2.0
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            if (user == null) {
                modelMap.put("needQRCode", false);
            } else {
                modelMap.put("needQRCode", true);
            }
            modelMap.put("product", product);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty productId");
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
    private static String productmapUrl;

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductDetailController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductDetailController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductDetailController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.product.url}")
    public void setProductmapUrl(String productmapUrl) {
        ProductDetailController.productmapUrl = productmapUrl;
    }

    @RequestMapping(value = "/generateqrcode4product", method = RequestMethod.GET)
    @ResponseBody
    private void generateQRCode4Product(HttpServletRequest request,
                                        HttpServletResponse response) {
        //获取前端传递过来的商品Id
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        //从session里获取当前顾客的信息
        PersonInfo user = (PersonInfo) request.getSession()
                .getAttribute("user");
        if (productId != -1 && user != null && user.getUserId() != null) {
            //获取当前时间戳，以保证二维码的有效性，精确到毫秒
            long timpStamp = System.currentTimeMillis();
            //将商品Id，顾客Id和timestamp传入content赋值到state中，这样微信获取到这些信息后会回传到用户
            //加上aaa是为了一个会的在添加信息的方法里替换这些信息使用
            String content = "{aaaproductIdaaa:" + productId + ",aaacustomerIdaaa:"
                    + user.getUserId() + ",aaacreateTimeaaa:" + timpStamp + "}";
            //将conten的信息先进行base64编码以及避免特殊字符造成的干扰，之后拼接目标URL
            try {
                String longUrl = urlPrefix + productmapUrl + urlMiddle + URLEncoder.encode(content, "UTF-8")
                        + urlSuffix;
                //将目标URL转换成短的URL
                String shortUrl = ShortNetAddress.getShortURL(longUrl);
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
}

