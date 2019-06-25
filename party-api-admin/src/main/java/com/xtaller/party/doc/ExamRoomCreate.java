package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Party on 2019/05/28
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@ApiModel(value ="考场新增")
public class ExamRoomCreate {
    @ApiModelProperty(value = "试卷Id")
    private String examId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "监考员")
    private String monitor;
    @ApiModelProperty(value = "是否设定启封码")
    private Integer isUnseal;
    @ApiModelProperty(value = "启封码")
    private String unsealCode;

    public String getExamId() { 
        return examId;
    } 

    public void setExamId(String examId) { 
        this.examId = examId;
    } 

    public String getName() { 
        return name;
    } 

    public void setName(String name) { 
        this.name = name;
    } 

    public String getMonitor() { 
        return monitor;
    } 

    public void setMonitor(String monitor) { 
        this.monitor = monitor;
    } 

    public Integer getIsUnseal() { 
        return isUnseal;
    } 

    public void setIsUnseal(Integer isUnseal) { 
        this.isUnseal = isUnseal;
    } 

    public String getUnsealCode() { 
        return unsealCode;
    } 

    public void setUnsealCode(String unsealCode) { 
        this.unsealCode = unsealCode;
    } 

}
