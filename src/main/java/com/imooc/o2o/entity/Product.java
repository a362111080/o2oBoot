package com.imooc.o2o.entity;

import java.util.Date;
import java.util.List;

public class Product {
	//主键Id
	private Long productId;
	//商品名
	private String productName;
	//商品简介
	private String productDesc;
	//简略图
	private String imgAddr;
	//原价
	private String normalPrice;
	//现价
	private String promotionPrice;
	//权重，越大越排前
	private Integer priority;
	//积分
	private Integer	point;
	//创建时间
	private Date createTime;
	//最近修改时间
	private  Date lastEditTime;
	//-1不可用，0下架，1.在前端展示系统展示
	private Integer enableStatus;

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	private List<ProductImg> productImgList;
	private ProductCategory productCategory;
	private Shop shop;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getImgAddr() {
		return imgAddr;
	}
	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}
	public String getNormalPrice() {
		return normalPrice;
	}
	public void setNormalPrice(String normalPrice) {
		this.normalPrice = normalPrice;
	}
	public String getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(String promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public List<ProductImg> getProductImgList() {
		return productImgList;
	}
	public void setProductImgList(List<ProductImg> productImgList) {
		this.productImgList = productImgList;
	}
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
}
