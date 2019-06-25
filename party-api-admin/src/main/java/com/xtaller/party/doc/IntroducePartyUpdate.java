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
@ApiModel(value = "入党介绍人修改")
public class IntroducePartyUpdate {
    @ApiModelProperty(value = "")
    private String id;
    @ApiModelProperty(value = "用户学号/工号")
    private String number;
    @ApiModelProperty(value = "介绍人学号/工号")
    private String introduceNumber;
    @ApiModelProperty(value = "介绍人党内单位及职务")
    private String inDuty;
    @ApiModelProperty(value = "介绍人党外单位及职务")
    private String outDuty;
    @ApiModelProperty(value = "介绍人入党时间")
    private Integer introduceJoinTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIntroduceNumber() {
        return introduceNumber;
    }

    public void setIntroduceNumber(String introduceNumber) {
        this.introduceNumber = introduceNumber;
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

    public Integer getIntroduceJoinTime() {
        return introduceJoinTime;
    }

    public void setIntroduceJoinTime(Integer introduceJoinTime) {
        this.introduceJoinTime = introduceJoinTime;
    }

}
