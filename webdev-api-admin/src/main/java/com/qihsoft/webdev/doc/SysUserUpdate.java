package com.qihsoft.webdev.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by qihsoft on 2018/08/10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "100修改")
public class SysUserUpdate {
    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "登录名")
    private String loginName;
    @ApiModelProperty(value = "用户密码")
    private String password;
    @ApiModelProperty(value = "密码混淆盐")
    private Integer salt;
    @ApiModelProperty(value = "用户状态")
    private Integer status;
    @ApiModelProperty(value = "用户类型")
    private Integer type;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "手机验证码")
    private String verifyCode;
    @ApiModelProperty(value = "电子邮件")
    private String email;
    @ApiModelProperty(value = "错误次数")
    private Integer errorCount;
    @ApiModelProperty(value = "解锁时间")
    private Integer unlockTime;
    @ApiModelProperty(value = "用于第三方登录的openID")
    private String openID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getSalt() {
        return salt;
    }

    public void setSalt(Integer salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public Integer getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(Integer unlockTime) {
        this.unlockTime = unlockTime;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

}
