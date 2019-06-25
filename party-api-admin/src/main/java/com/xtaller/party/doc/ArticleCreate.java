package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/10/12
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "文章新增")
public class ArticleCreate {
    @ApiModelProperty(value = "文章标题")
    private String title;
    @ApiModelProperty(value = "文章内容")
    private String content;
    @ApiModelProperty(value = "文章摘要")
    private String summary;
    @ApiModelProperty(value = "作者")
    private String author;
    @ApiModelProperty(value = "是否需要审核")
    private Integer isNeedAuditing;
    @ApiModelProperty(value = "审核状态")
    private Integer auditingStatus;
    @ApiModelProperty(value = "审核人")
    private String auditor;
    @ApiModelProperty(value = "发布时间")
    private Integer releaseTime;
    @ApiModelProperty(value = "1-已发布，2-未发布")
    private Integer releaseStatus;
    @ApiModelProperty(value = "所属文章类型代码")
    private String type;
    @ApiModelProperty(value = "所属主题代码")
    private String topic;
    @ApiModelProperty(value = "缩略图地址")
    private String thumbnailURL;
    @ApiModelProperty(value = "置顶状态")
    private Integer setTopStatus;
    @ApiModelProperty(value = "排序号")
    private Integer sortNumber;
    @ApiModelProperty(value = "点击数")
    private Integer clicks;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "封面图片id")
    private String pictureId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getIsNeedAuditing() {
        return isNeedAuditing;
    }

    public void setIsNeedAuditing(Integer isNeedAuditing) {
        this.isNeedAuditing = isNeedAuditing;
    }

    public Integer getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(Integer auditingStatus) {
        this.auditingStatus = auditingStatus;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Integer getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Integer releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(Integer releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Integer getSetTopStatus() {
        return setTopStatus;
    }

    public void setSetTopStatus(Integer setTopStatus) {
        this.setTopStatus = setTopStatus;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }
}
