package com.imooc.o2o.exceptions;

public class WechatAuthOperationException extends RuntimeException {

    private static final long serialVersionUID = 4352373686846499781L;

    public WechatAuthOperationException(String msg) {
       super(msg);
    }
}
