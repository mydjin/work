package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Party on 2019/03/09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "考察与谈话记录新增")
public class SurveyRecordChatCreate {
    @ApiModelProperty(value = "用户学号/工号")
    private String number;
    @ApiModelProperty(value = "考察/谈话时间")
    private Integer surveyTime;
    @ApiModelProperty(value = "考察/谈话情况")
    private String surveyCondition;
    @ApiModelProperty(value = "审核状态")
    private Integer approvalStatus;
    @ApiModelProperty(value = "审核意见")
    private String approvalOpinion;
    @ApiModelProperty(value = "审核结果")
    private Integer approvalResult;
    @ApiModelProperty(value = "审核人学号/工号")
    private String approverNumber;
    @ApiModelProperty(value = "类型")
    private Integer type;
    @ApiModelProperty(value = "阶段")
    private Integer stage;
    @ApiModelProperty(value = "次数")
    private Integer count;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSurveyTime() {
        return surveyTime;
    }

    public void setSurveyTime(Integer surveyTime) {
        this.surveyTime = surveyTime;
    }

    public String getSurveyCondition() {
        return surveyCondition;
    }

    public void setSurveyCondition(String surveyCondition) {
        this.surveyCondition = surveyCondition;
    }

    public Integer getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(Integer approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalOpinion() {
        return approvalOpinion;
    }

    public void setApprovalOpinion(String approvalOpinion) {
        this.approvalOpinion = approvalOpinion;
    }

    public Integer getApprovalResult() {
        return approvalResult;
    }

    public void setApprovalResult(Integer approvalResult) {
        this.approvalResult = approvalResult;
    }

    public String getApproverNumber() {
        return approverNumber;
    }

    public void setApproverNumber(String approverNumber) {
        this.approverNumber = approverNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
