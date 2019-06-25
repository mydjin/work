package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
* Created by Taller on 2017/12/31
*/
@SuppressWarnings("serial")
@TableName(value = "data_article_accepter")
public class DataArticleAccepter extends Model<DataArticleAccepter> {
  private String id = IdKit.getId(this.getClass());
  private String articleId;
  private String objectId;
  private Integer objectType;
  private Integer isReader;
  private Integer readTime;
  private String creator;
  private Integer createTime;
  private String reviser;
  private Integer reviseTime;
  private Integer isDel;

  public String getId() { 
      return id;
  } 
  public void setId(String id) { 
      this.id = id;
  } 
  public String getArticleId() { 
      return articleId;
  } 
  public void setArticleId(String articleId) { 
      this.articleId = articleId;
  } 
  public String getObjectId() { 
      return objectId;
  }


  public void setObjectId(String objectId) { 
      this.objectId = objectId;
  } 
  public Integer getObjectType() { 
      return objectType;
  }

    /**
     *
     * @param objectType 对象类型 1年级 2班级 3教师 4家长 5学生
     */
  public void setObjectType(Integer objectType) { 
      this.objectType = objectType;
  } 
  public Integer getIsReader() { 
      return isReader;
  } 
  public void setIsReader(Integer isReader) { 
      this.isReader = isReader;
  } 
  public Integer getReadTime() { 
      return readTime;
  } 
  public void setReadTime(Integer readTime) { 
      this.readTime = readTime;
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
  public Integer getIsDel() { 
      return isDel;
  } 
  public void setIsDel(Integer isDel) { 
      this.isDel = isDel;
  } 
  @Override
  protected Serializable pkVal() {
      return id;
  }
}
