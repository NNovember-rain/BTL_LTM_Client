package com.game.solve.model;

import java.io.Serializable;

public class DataSending<T> implements Serializable {
    private String requestType;
    private T data;


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
