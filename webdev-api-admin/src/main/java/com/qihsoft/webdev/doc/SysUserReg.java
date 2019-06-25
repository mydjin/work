package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Qihsoft on 2017/10/5
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "后端登记用户")
public class SysUserReg {
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "登录密码")
    private String password;
    @ApiModelProperty(value = "学号/工号")
    private Integer type;
    @ApiModelProperty(value = "用户类型")
    private String number;
    @ApiModelProperty(value = "用户属性")
    private Attr attr;
    @ApiModelProperty(value = "权限id")
    private String roleId;

    public class Attr {
        @ApiModelProperty(value = "真实姓名")
        private String trueName;
        @ApiModelProperty(value = "手机")
        private String mobile;
        @ApiModelProperty(value = "电子邮件")
        private String mail;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Attr getAttr() {
        return attr;
    }

    public void setAttr(Attr attr) {
        this.attr = attr;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
