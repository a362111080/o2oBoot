package com.imooc.o2o.exceptions;

public class ProductOperationException extends RuntimeException {
    private static final long serialVersionUID = 1182563719599527969L;

    public ProductOperationException(String msg) {
        super(msg);
    }
}
