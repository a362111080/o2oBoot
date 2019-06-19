package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.AwardDao;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.exceptions.AwardOperationException;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AwardServiceImpl implements AwardService {
    @Autowired
    private AwardDao awardDao;

    @Override
    public AwardExecution getAwardList(Award awardCondition, int pageIndex, int pageSize) {
        //非空判断
        if (awardCondition != null && pageIndex > -1 && pageSize > -1) {
            //页转行
            int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            //查询出奖品列表信息
            List<Award> awardList = awardDao.queryAwardList(awardCondition, rowIndex, pageSize);
            //返回总数
            int count = awardDao.queryAwardCount(awardCondition);
            AwardExecution ae = new AwardExecution();
            ae.setAwardList(awardList);
            ae.setCount(count);
            return ae;
        } else {
            return null;
        }
    }

    //1若缩略图有值，则处理缩略图
    //若原先存在缩略图先删除再添加新图，之后获取缩略图相对路径赋值给award
    //2更新tb_award的信息
    @Override
    @Transactional
    public AwardExecution modifyAward(Award award, ImageHolder thumbnail) {
        //空值判断
        if (award != null && award.getShopId() != null) {
            award.setLastEditTime(new Date());
            if (thumbnail != null) {
                //通过awardId取出对应的实体类信息
                Award tempAward = awardDao.queryAwardByAwardId(award.getAwardId());
                //如果传输过程存在图片流，则删除原有图片
                if (tempAward.getAwardImg() != null) {
                    ImageUtil.deleteFlieOrPath(tempAward.getAwardImg());
                }
                //储存图片流，获取相对路径
                addThumbnail(award, thumbnail);
            }
            try {
                //根据传入的实体类修改相应的信息
                int effectedNum = awardDao.updateAward(award);
                if (effectedNum <= 0) {
                    throw new AwardOperationException("更新商品信息失败");
                }
                return new AwardExecution(AwardStateEnum.SUCCESS, award);
            } catch (Exception e) {
                throw new AwardOperationException("更新商品信息失败：" + e.toString());
            }
        } else {
            return new AwardExecution(AwardStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    @Override
    public Award getAwardById(long awardId) {
        return awardDao.queryAwardByAwardId(awardId);
    }

    @Override
    @Transactional
    //1.处理缩略图，获取缩略图相对路径并赋值给award
    //2.往tb_awardId希写入奖品信息
    public AwardExecution addAward(Award award, ImageHolder thumbnail) {
        //空值判断
        if (award != null && award.getShopId() != null) {
            //给award附上初始值
            award.setCreateTime(new Date());
            award.setLastEditTime(new Date());
            //award默认可用，即出现在前端展示系统中
            award.setEnableStatus(1);
            if (thumbnail != null) {
                addThumbnail(award, thumbnail);
            }
            try {
                //添加奖品信息
                int effectedNum = awardDao.insertAward(award);
                if (effectedNum <= 0) {
                    throw new AwardOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new AwardOperationException("创建商品失败：" + e.toString());
            }
            return new AwardExecution(AwardStateEnum.SUCCESS, award);
        } else {
            return new AwardExecution(AwardStateEnum.NULL_USERPRODUCT_INFO);
        }
    }

    private void addThumbnail(Award award, ImageHolder thumbnail) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(award.getShopId());
        String shopImgAdd = ImageUtil.generateThumbnail(thumbnail, dest);
        award.setAwardImg(shopImgAdd);
    }
}
