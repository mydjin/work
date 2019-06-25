package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/10/08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "培养联系人新增")
public class LinkUserCreate {
    @ApiModelProperty(value = "用户学号/工号")
    private String number;
    @ApiModelProperty(value = "联系人学号/工号")
    private String linkNumber;
    @ApiModelProperty(value = "联系人党内单位及职务")
    private String inDuty;
    @ApiModelProperty(value = "联系人党外单位及职务")
    private String outDuty;
    @ApiModelProperty(value = "联系人入党时间")
    private Integer linkJoinTime;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLinkNumber() {
        return linkNumber;
    }

    public void setLinkNumber(String linkNumber) {
        this.linkNumber = linkNumber;
    }

    public String getInDuty() {
        return inDuty;
    }

    public void setInDuty(String inDuty) {
        this.inDuty = inDuty;
    }

    public String getOutDuty() {
        return outDuty;
    }

    public void setOutDuty(String outDuty) {
        this.outDuty = outDuty;
    }

    public Integer getLinkJoinTime() {
        return linkJoinTime;
    }

    public void setLinkJoinTime(Integer linkJoinTime) {
        this.linkJoinTime = linkJoinTime;
    }

}
