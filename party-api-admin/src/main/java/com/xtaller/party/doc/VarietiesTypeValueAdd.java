package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "品总种类新增")
public class VarietiesTypeValueAdd {

    @ApiModelProperty(value = "类型属性名称")
    private String name;

    @ApiModelProperty(value = "类型id")
    private String typeId;

    @ApiModelProperty(value = "是否默认")
    private String isDefault;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "VarietiesTypeValueAdd{" +
                "name='" + name + '\'' +
                ", typeId='" + typeId + '\'' +
                ", isDefault='" + isDefault + '\'' +
                '}';
    }
}
