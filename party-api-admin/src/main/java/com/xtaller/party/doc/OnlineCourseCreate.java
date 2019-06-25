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
@ApiModel(value = "网络课程新增")
public class OnlineCourseCreate {
    @ApiModelProperty(value = "主题")
    private String topic;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "摘要")
    private String summary;
    @ApiModelProperty(value = "是否发布")
    private Integer isRelease;
    @ApiModelProperty(value = "审核结果")
    private Integer result;
    @ApiModelProperty(value = "审核状态")
    private Integer status;
    @ApiModelProperty(value = "意见")
    private String opinion;
    @ApiModelProperty(value = "是否允许评论")
    private Integer isReview;
    @ApiModelProperty(value = "是否精华")
    private Integer isEssence;
    @ApiModelProperty(value = "开始时间")
    private Integer startTime;
    @ApiModelProperty(value = "结束时间")
    private Integer endTime;
    @ApiModelProperty(value = "性质")
    private Integer property;
    @ApiModelProperty(value = "封面图片id")
    private String pictureId;
    @ApiModelProperty(value = "发布范围")
    private String range;
    @ApiModelProperty(value = "是否发短信")
    private Integer isNote;
    @ApiModelProperty(value = "是否推送")
    private Integer isPush;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(Integer isRelease) {
        this.isRelease = isRelease;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Integer getIsReview() {
        return isReview;
    }

    public void setIsReview(Integer isReview) {
        this.isReview = isReview;
    }

    public Integer getIsEssence() {
        return isEssence;
    }

    public void setIsEssence(Integer isEssence) {
        this.isEssence = isEssence;
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

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
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
