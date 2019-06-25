package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/08/23
 */
@SuppressWarnings("serial")
@TableName(value = "notice_review")
public class NoticeReview extends Model<NoticeReview> {
  private String id = IdKit.getId(this.getClass());
  private String noticeId;
  private String number;
  private Integer reviewTime;
  private String content;
  private Integer star;
  private Integer status;
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
