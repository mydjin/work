package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/08/23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "通知公告评论点赞修改")
public class NoticeReviewUpdate {
    @ApiModelProperty(value = "")
    private String id;
    @ApiModelProperty(value = "通知公告id")
    private String noticeId;
    @ApiModelProperty(value = "评论者学号/工号")
    private String number;
    @ApiModelProperty(value = "评论时间")
    private Integer reviewTime;
    @ApiModelProperty(value = "内容")
    private String content;
    @ApiModelProperty(value = "评论星级")
    private Integer star;
    @ApiModelProperty(value = "点赞状态")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Integer reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
