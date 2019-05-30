package com.reanod.workflow.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xugj<br>
 * @version 1.0<br>
 * @createDate 2019/05/29 17:51 <br>
 * @Description <p> 返回响应数据 </p>
 */
@ApiModel(description = "返回响应数据")
public class RestMessgae {

    @ApiModelProperty(value = "错误信息")
    private String message;
    @ApiModelProperty(value = "状态码")
    private String code;
    @ApiModelProperty(value = "返回的数据")
    private Object data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static  RestMessgae success(String message, Object data){
        RestMessgae restMessgae = new RestMessgae();
        restMessgae.setCode("200");
        restMessgae.setMessage(message);
        restMessgae.setData(data);
        return restMessgae;
    }

    public static  RestMessgae fail(String message, Object data){
        RestMessgae restMessgae = new RestMessgae();
        restMessgae.setCode("500");
        restMessgae.setMessage(message);
        restMessgae.setData(data);
        return restMessgae;
    }
}