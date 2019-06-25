package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "品总种类新增")
public class VarietiesTypeUpdata {

    @ApiModelProperty(value = "Id")
    private String id;
    @ApiModelProperty(value = "种类名称")
    private String name;
    @ApiModelProperty(value = "是否默认")
    private String isDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "VarietiesTypeUpdata{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isDefault='" + isDefault + '\'' +
                '}';
    }
}
