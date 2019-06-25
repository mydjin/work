package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "基础数据")
public class TpsConfig {
    @ApiModelProperty(value = "代码")
    private String code;
    @ApiModelProperty(value = "属性")
    private String attr;
    @ApiModelProperty(value = "值")
    private String val;

    public String getCode() {
        return code;
    }

    public void setCode(String attr) {
        this.code = code;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
