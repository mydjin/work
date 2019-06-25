package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Qihsoft on 2019/02/03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "用户基础信息新增")
public class SysUserInfoCreate {
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "学号/工号")
    private String number;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "性别")
    private Integer sex;
    @ApiModelProperty(value = "民族代码")
    private String nationCode;
    @ApiModelProperty(value = "出生日期")
    private Integer birthday;
    @ApiModelProperty(value = "籍贯")
    private String originPlace;
    @ApiModelProperty(value = "身份证")
    private String idCard;
    @ApiModelProperty(value = "用户类型")
    private Integer type;
    @ApiModelProperty(value = "政治面貌")
    private Integer politicalOutlook;
    @ApiModelProperty(value = "部门代码")
    private String academyCode;
    @ApiModelProperty(value = "专业代码")
    private String majorCode;
    @ApiModelProperty(value = "班级代码")
    private String classCode;
    @ApiModelProperty(value = "在读状态 ")
    private Integer studyStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNationCode() {
        return nationCode;
    }

    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public String getOriginPlace() {
        return originPlace;
    }

    public void setOriginPlace(String originPlace) {
        this.originPlace = originPlace;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPoliticalOutlook() {
        return politicalOutlook;
    }

    public void setPoliticalOutlook(Integer politicalOutlook) {
        this.politicalOutlook = politicalOutlook;
    }

    public String getAcademyCode() {
        return academyCode;
    }

    public void setAcademyCode(String academyCode) {
        this.academyCode = academyCode;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Integer getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(Integer studyStatus) {
        this.studyStatus = studyStatus;
    }

}
