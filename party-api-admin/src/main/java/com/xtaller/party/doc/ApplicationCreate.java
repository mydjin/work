package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by party on 2018/10/23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "入党申请书新增")
public class ApplicationCreate {
    @ApiModelProperty(value = "文件URL")
    private String fileURL;
    @ApiModelProperty(value = "用户学号/工号")
    private String proposerNumber;
    @ApiModelProperty(value = "历史版本文件URL")
    private String lastFileURL;
    @ApiModelProperty(value = "提交时间")
    private Integer referTime;

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getProposerNumber() {
        return proposerNumber;
    }

    public void setProposerNumber(String proposerNumber) {
        this.proposerNumber = proposerNumber;
    }

    public String getLastFileURL() {
        return lastFileURL;
    }

    public void setLastFileURL(String lastFileURL) {
        this.lastFileURL = lastFileURL;
    }

    public Integer getReferTime() {
        return referTime;
    }

    public void setReferTime(Integer referTime) {
        this.referTime = referTime;
    }

}
