package com.imooc.o2o.enums;

public enum AwardStateEnum {
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    NULL_USERPRODUCT_ID(-1002, "AwardId为空"),
    NULL_USERPRODUCT_INFO(-1003, "传入了空的信息");

    private int state;

    private String stateInfo;

    private AwardStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static AwardStateEnum stateOf(int index) {
        for (AwardStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

}
