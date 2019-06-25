package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by qihsoft on 2018/12/09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "消息提醒新增")
public class MessageCreate {
    @ApiModelProperty(value = "目的服务器")
    private Integer server;
    @ApiModelProperty(value = "主题")
    private String topic;
    @ApiModelProperty(value = "内容")
    private String content;
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


    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}