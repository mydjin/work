package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "黑名单数据")
public class VisitorAdd {
    @ApiModelProperty(value = "cardNo")
    private String cardNo;
    @ApiModelProperty(value = "deviceCode")
    private String deviceCode;
    @ApiModelProperty(value = "jobId")
    private String jobId;
    @ApiModelProperty(value = "token")
    private String token;
    @ApiModelProperty(value = "queryType")
    private Integer queryType;
    @ApiModelProperty(value = "passTime")
    private String passTime;

    @ApiModelProperty(value = "collectionType")
    private String collectionType;
    @ApiModelProperty(value = "extendInfo1")
    private String extendInfo1;
    @ApiModelProperty(value = "extendInfo2")
    private String extendInfo2;
    @ApiModelProperty(value = "extendInfo3")
    private Integer extendInfo3;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getExtendInfo1() {
        return extendInfo1;
    }

    public void setExtendInfo1(String extendInfo1) {
        this.extendInfo1 = extendInfo1;
    }

    public String getExtendInfo2() {
        return extendInfo2;
    }

    public void setExtendInfo2(String extendInfo2) {
        this.extendInfo2 = extendInfo2;
    }

    public Integer getExtendInfo3() {
        return extendInfo3;
    }

    public void setExtendInfo3(Integer extendInfo3) {
        this.extendInfo3 = extendInfo3;
    }
}
