package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by party on 2018/08/23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "创建题库")
public class UpdateQuestionAndOptions {
    @ApiModelProperty(value = "题目id")
    private String questionId;
    @ApiModelProperty(value = "题目名称")
    private String questionName;
    @ApiModelProperty(value = "类型")
    private Integer type;
    @ApiModelProperty(value = "答案解析")
    private String answerKeys;
    @ApiModelProperty(value = "选项内容")
    private List<String> optionInfo;
    @ApiModelProperty(value = "选项号")//1-A,2-B,3-C...,传来那个说明那个就是正确答案
    private List<Integer> optionNumber;
    @ApiModelProperty(value = "答案号")
    private List<Integer> answerNumber;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAnswerKeys() {
        return answerKeys;
    }

    public void setAnswerKeys(String answerKeys) {
        this.answerKeys = answerKeys;
    }

    public List<String> getOptionInfo() {
        return optionInfo;
    }

    public void setOptionInfo(List<String> optionInfo) {
        this.optionInfo = optionInfo;
    }

    public List<Integer> getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(List<Integer> optionNumber) {
        this.optionNumber = optionNumber;
    }

    public List<Integer> getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(List<Integer> answerNumber) {
        this.answerNumber = answerNumber;
    }
}
