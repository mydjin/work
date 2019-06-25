package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "新增访客申请")

public class AppVisitorAdd {
    @ApiModelProperty(value = "访客Id")
    private String id;
    @ApiModelProperty(value = "初访问人用户id")
    private String userId;
    @ApiModelProperty(value = "访客姓名")
    private String name;
    @ApiModelProperty(value = "到访人数")
    private Integer applyNum;
    @ApiModelProperty(value = "证件类型 ——0身份证  1市民卡")
    private Integer idCardType;
    @ApiModelProperty(value = "证件号码")
    private String idCard;
    @ApiModelProperty(value = "预约到访时间")
    private String applyBeginDate;
    @ApiModelProperty(value = "预约访问时长")
    private Integer applyDuration;
    @ApiModelProperty(value = "来访事由")
    private String applyRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(Integer applyNum) {
        this.applyNum = applyNum;
    }

    public Integer getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(Integer idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getApplyBeginDate() {
        return applyBeginDate;
    }

    public void setApplyBeginDate(String applyBeginDate) {
        this.applyBeginDate = applyBeginDate;
    }

    public Integer getApplyDuration() {
        return applyDuration;
    }

    public void setApplyDuration(Integer applyDuration) {
        this.applyDuration = applyDuration;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }
}
