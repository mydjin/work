package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/11/11
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "思想汇报新增")
public class ReportCreate {
    @ApiModelProperty(value = "文件路径")
    private String fileURL;
    @ApiModelProperty(value = "上版本文件路径")
    private String lastFileURL;
    //    @ApiModelProperty(value = "审核状态")
//    private Integer status;
//    @ApiModelProperty(value = "审核意见")
//    private String opinion;
//    @ApiModelProperty(value = "审核结果")
//    private Integer result;
    @ApiModelProperty(value = "用户学号/工号")
    private String proposerNumber;
    //    @ApiModelProperty(value = "审核人学号/工号")
//    private String verifierNumber;
    @ApiModelProperty(value = "类型")
    private Integer type;
    @ApiModelProperty(value = "提交时间")
    private Integer referTime;
    @ApiModelProperty(value = "阶段")
    private Integer stage;
    @ApiModelProperty(value = "次数")
    private Integer count;

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getLastFileURL() {
        return lastFileURL;
    }

    public void setLastFileURL(String lastFileURL) {
        this.lastFileURL = lastFileURL;
    }

//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }

//    public String getOpinion() {
//        return opinion;
//    }
//
//    public void setOpinion(String opinion) {
//        this.opinion = opinion;
//    }
//
//    public Integer getResult() {
//        return result;
//    }
//
//    public void setResult(Integer result) {
//        this.result = result;
//    }

    public String getProposerNumber() {
        return proposerNumber;
    }

    public void setProposerNumber(String proposerNumber) {
        this.proposerNumber = proposerNumber;
    }

//    public String getVerifierNumber() {
//        return verifierNumber;
//    }
//
//    public void setVerifierNumber(String verifierNumber) {
//        this.verifierNumber = verifierNumber;
//    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getReferTime() {
        return referTime;
    }

    public void setReferTime(Integer referTime) {
        this.referTime = referTime;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
