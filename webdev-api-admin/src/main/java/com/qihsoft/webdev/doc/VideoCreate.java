package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/12/03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "视频新增")
public class VideoCreate {
    @ApiModelProperty(value = "节内容id")
    private String sectionId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "视频URL")
    private String url;
    @ApiModelProperty(value = "视频路径")
    private String path;
    @ApiModelProperty(value = "描述")
    private String describe;
    @ApiModelProperty(value = "文件标识码")
    private String code;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
