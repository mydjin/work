package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Qihsoft on 2019/05/07
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "系统操作记录新增")
public class SysOperationRecordSelect {
    @ApiModelProperty(value = "学工号")
    private String number;
    @ApiModelProperty(value = "服务器")
    private Integer server;
    @ApiModelProperty(value = "模块")
    private String control;
    @ApiModelProperty(value = "功能")
    private String function;
    @ApiModelProperty(value = "IP地址")
    private String ipAddr;
    @ApiModelProperty(value = "操作状态")
    private Integer status;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
