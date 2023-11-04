package com.phucvukimcore.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phucvukimcore.enums.ResultEnum;
import com.phucvukimcore.util.JsonUtil;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 11L;

    @JsonProperty
    private Integer code = ResultEnum.FAIL.getCode();

    @JsonProperty
    private T data = null;

    @JsonProperty
    private String msg = null;

    public Result(){}

    public Result(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public Result(Integer code, String msg, T data) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public static <T> Result<T> success() {
        return new Result<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc());
    }

    public static <T> Result<T> fail() {
        return new Result<T>(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getDesc());
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<T>(ResultEnum.FAIL.getCode(), msg, null);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<T>(ResultEnum.SUCCESS.getCode(), msg, data);
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}
