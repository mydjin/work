package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/12/04
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "试卷新增")
public class ExamCreate {
    @ApiModelProperty(value = "试卷名称")
    private String name;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "考试类型")
    private Integer type;
    @ApiModelProperty(value = "考试方式")
    private Integer way;
    @ApiModelProperty(value = "试卷总分值")
    private String score;
    @ApiModelProperty(value = "判断题总分值")
    private String trueOrFalse;
    @ApiModelProperty(value = "单选题总分值")
    private String single;
    @ApiModelProperty(value = "多选题总分值")
    private String multiple;
    @ApiModelProperty(value = "填空题总分值")
    private Integer startTime;
    @ApiModelProperty(value = "开始时间")
    private Integer endTime;
    @ApiModelProperty(value = "结束时间")
    private String gap;
    @ApiModelProperty(value = "考试时长")
    private Integer duration;
    @ApiModelProperty(value = "发布范围")
    private String range;
    @ApiModelProperty(value = "是否发短信")
    private Integer isNote;
    @ApiModelProperty(value = "是否推送")
    private Integer isPush;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTrueOrFalse() {
        return trueOrFalse;
    }

    public void setTrueOrFalse(String trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getGap() {
        return gap;
    }

    public void setGap(String gap) {
        this.gap = gap;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public Integer getIsNote() {
        return isNote;
    }

    public void setIsNote(Integer isNote) {
        this.isNote = isNote;
    }

    public Integer getIsPush() {
        return isPush;
    }

    public void setIsPush(Integer isPush) {
        this.isPush = isPush;
    }

}
