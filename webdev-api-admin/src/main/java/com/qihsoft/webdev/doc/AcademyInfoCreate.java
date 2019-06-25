package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Qihsoft on 2018/09/01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "部门信息新增")
public class AcademyInfoCreate {
    @ApiModelProperty(value = "部门代码")
    private String code;
    @ApiModelProperty(value = "部门名称")
    private String name;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "所在校区")
    private Integer campus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCampus() {
        return campus;
    }

    public void setCampus(Integer campus) {
        this.campus = campus;
    }

}
