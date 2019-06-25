package com.xtaller.party.core.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.xtaller.party.utils.kit.IdKit;
import java.io.Serializable;

/**
 * Created by Taller on 2018/11/07
 */
@SuppressWarnings("serial")
@TableName(value = "notice")
public class Notice extends Model<Notice> {
  private String id = IdKit.getId(this.getClass());
  private String title;
  private String content;
  private String author;
  private String source;
  private String pictureId;
  private Integer hits;
  private String summary;
  private Integer likenum;
  private Integer isReview;
  private Integer isEssence;
  private Integer isCheck;
  private Integer result;
  private Integer status;
  private String opinion;
  private String approverNumber;
  private Integer isPublish;
  private String creator;
  private Integer createTime;
  private String reviser;
  private Integer reviseTime;
  private Integer isDel;
  private Integer releaseTime;

  public String getId() { 
      return id;
  } 
  public void setId(String id) { 
      this.id = id;
  } 
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
  public String getAuthor() { 
      return author;
  } 
  public void setAuthor(String author) { 
      this.author = author;
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
  public Integer getHits() { 
      return hits;
  } 
  public void setHits(Integer hits) { 
      this.hits = hits;
  } 
  public String getSummary() { 
      return summary;
  } 
  public void setSummary(String summary) { 
      this.summary = summary;
  } 
  public Integer getLikenum() { 
      return likenum;
  } 
  public void setLikenum(Integer likenum) { 
      this.likenum = likenum;
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
  public Integer getIsCheck() { 
      return isCheck;
  } 
  public void setIsCheck(Integer isCheck) { 
      this.isCheck = isCheck;
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
  public String getApproverNumber() { 
      return approverNumber;
  } 
  public void setApproverNumber(String approverNumber) { 
      this.approverNumber = approverNumber;
  } 
  public Integer getIsPublish() { 
      return isPublish;
  } 
  public void setIsPublish(Integer isPublish) { 
      this.isPublish = isPublish;
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
  public Integer getReleaseTime() { 
      return releaseTime;
  } 
  public void setReleaseTime(Integer releaseTime) { 
      this.releaseTime = releaseTime;
  } 
  @Override
  protected Serializable pkVal() {
      return id;
  }
}
