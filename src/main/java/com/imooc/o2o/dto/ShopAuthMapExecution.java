package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;

import java.util.List;

public class ShopAuthMapExecution {
    //结果状态
    private int state;

    //状态标识
    private String stateInfo;

    //授权数
    private Integer count;

    //操作的shopAuthMap
    private ShopAuthMap shopAuthMap;

    //授权列表（查询）
    private List<ShopAuthMap> shopAuthMapList;

    public ShopAuthMapExecution() {

    }

    //失败的构造器
    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum) {
        this.state=stateEnum.getState();
        this.stateInfo=stateEnum.getStateInfo();
    }
    // 成功的构造器
    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, ShopAuthMap shopAuthMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMap = shopAuthMap;
    }

    // 操作列表的成功构造器
    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, ShopAuthMap shopAuthMap, List<ShopAuthMap> shopAuthMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMap = shopAuthMap;
        this.shopAuthMapList = shopAuthMapList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ShopAuthMap getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMap shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMap> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }
}

