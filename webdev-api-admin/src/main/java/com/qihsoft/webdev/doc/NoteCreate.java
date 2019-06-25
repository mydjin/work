package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Qihsoft on 2019/05/06
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by qihsoft on 2018/12/09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "短信新增")
public class NoteCreate {
    @ApiModelProperty(value = "短信主题")
    private String theme;
    @ApiModelProperty(value = "短信内容")
    private String detail;
    @ApiModelProperty(value = "发布范围")
    private String range;
    @ApiModelProperty(value = "发布人群")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}
