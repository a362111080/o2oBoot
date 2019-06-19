package com.imooc.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dao.AwardDao;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/shopadmin")
@Controller
public class AwardManagementController {
    @Autowired
    private AwardService awardService;


    @RequestMapping(value = "/listawardsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listAwardsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取分页
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //从session里面获取shopId
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        //空值判断
        if ((pageIndex > -1) && (pageSize > -1) && (currentShop.getShopId() != null)) {
            //判断查询条件里面是否传入奖品名，有则模糊查询
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            //拼接查询条件
            Award awardCondition = compactAwardCondition4Search(currentShop.getShopId(), awardName);
            //根据查询条件分页获取奖品列表即总数
            AwardExecution ae = awardService.getAwardList(awardCondition, pageIndex, pageSize);
            modelMap.put("awardList", ae.getAwardList());
            modelMap.put("count", ae.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyaward", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        //根据传入的状态值决定是否跳过验证码校验
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入错误的验证码");
            return modelMap;
        }
        //接受前端参数的变量初始化包括商品，缩略图
        ObjectMapper mapper = new ObjectMapper();
        Award award = null;
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        //请求中都带有multi字样，因此没法过滤，只是用来拦截外部非图片流的处理，
        //里边有slt的空值判断，放心使用
        try {
            if (multipartResolver.isMultipart(request)) {
                //这里之前在if外面，，如果是上下架，没有文件属性，强转一直报空指针，
                // 现在先判断再强转就可以了
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
                }
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
            award = mapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        //空值判断
        if (award != null) {
            try {
                //从session中获取当前店铺的Id并赋值给award，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                AwardExecution pe = awardService.modifyAward(award, thumbnail);
                if (pe.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getawardbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getAwardById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //从request里面获取前端传过来的awardId
        long awardId = HttpServletRequestUtil.getLong(request, "awardById");
        //空值判断
        if (awardId > -1) {
            //根据传入的Id获取奖品信息并返回
            Award award = awardService.getAwardById(awardId);
            modelMap.put("award", award);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty awardId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/addaward", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addAward(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入错误的验证码");
            return modelMap;
        }
        //接受前端参数的变量初始化，包括奖品，缩略图
        ObjectMapper mapper = new ObjectMapper();
        Award award = null;
        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        ImageHolder thumbnail = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        try {
            if (multipartResolver.isMultipart(request)) {
                //这里之前在if外面，，如果编辑文件流为null，强转一直报空指针，
                // 现在先判断再强转就可以了
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
                //判断传图片没，如果是空，获取时会报错
                if (thumbnailFile != null) {
                    thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
                }
            }
        } catch (Exception e) {
            modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            //将前端传入的awardStr转换成奖品对象
            award = mapper.readValue(awardStr, Award.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        //空值判断
        if (award != null && thumbnail != null) {
            //从session中获取当前店铺的Id并赋值给award，减少对前端数据的依赖
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                award.setShopId(currentShop.getShopId());
                //添加award
                AwardExecution ae = awardService.addAward(award, thumbnail);
                if (ae.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ae.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入奖品信息");
            return modelMap;
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

//    private ImageHolder handleImage(MultipartHttpServletRequest request, ImageHolder thumbnails) throws IOException {
//        ImageHolder thumbnail;
//        MultipartHttpServletRequest multipartRequest = request;
//        //取出缩略图并构建ImageHolder对象
//        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
//        thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
//        //取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
//        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
//            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
//                    .getFile("thumbnails" + i);
//            if (productImgFile != null) {
//                //若取出的第i个详情图片文件流不为空，则将其加入详情图列表
//                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
//                        productImgFile.getInputStream());
//                productImgList.add(productImg);
//            } else {
//                //若取出的第i个详情图片文件流为空，则终止循环
//                break;
//            }
//        }
//        return thumbnail;


}

