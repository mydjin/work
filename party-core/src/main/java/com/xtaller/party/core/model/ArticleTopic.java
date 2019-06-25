package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/12/08
 */
@SuppressWarnings("serial")
@TableName(value = "article_topic")
public class ArticleTopic extends Model<ArticleTopic> {
  private String id = IdKit.getId(this.getClass());
  private String topicTypeCode;
  private String topicTypeName;
  private Integer parentID;
  private Integer isDel;
  private String creator;
  private Integer createTime;
  private String reviser;
  private Integer reviseTime;

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
  public Integer getIsDel() { 
      return isDel;
  } 
  public void setIsDel(Integer isDel) { 
      this.isDel = isDel;
  } 
  public String getCreator() { 
      return creator;
  } 
  public void setCreator(String creator) { 
      this.creator = creator;
  } 
  public Integer getCreateTime() { 
      return createTime;
  } 
  public void setCreateTime(Integer createTime) { 
      this.createTime = createTime;
  } 
  public String getReviser() { 
      return reviser;
  } 
  public void setReviser(String reviser) { 
      this.reviser = reviser;
  } 
  public Integer getReviseTime() { 
      return reviseTime;
  } 
  public void setReviseTime(Integer reviseTime) { 
      this.reviseTime = reviseTime;
  } 
  @Override
  protected Serializable pkVal() {
      return id;
  }
}
