package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/12/08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "文章主题类型修改")
public class ArticleTopicUpdate {
    @ApiModelProperty(value = "文章主题ID")
    private String id;
    @ApiModelProperty(value = "文章主题代码")
    private String topicTypeCode;
    @ApiModelProperty(value = "文章主题名称")
    private String topicTypeName;
    @ApiModelProperty(value = "上级ID")
    private Integer parentID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicTypeCode() {
        return topicTypeCode;
    }

    public void setTopicTypeCode(String topicTypeCode) {
        this.topicTypeCode = topicTypeCode;
    }

    public String getTopicTypeName() {
        return topicTypeName;
    }

    public void setTopicTypeName(String topicTypeName) {
        this.topicTypeName = topicTypeName;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

}
