package com.xtaller.party.doc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Party on 2019/03/14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@ApiModel(value = "考勤新增")
public class AttendanceCreate {
    @ApiModelProperty(value = "考勤时间")
    private Integer attendanceTime;
    @ApiModelProperty(value = "集中学习/会议id")
    private String scheduleId;
    @ApiModelProperty(value = "考勤状态")
    private Integer status;
    @ApiModelProperty(value = "学生学号")
    private String number;
    @ApiModelProperty(value = "签到方式")
    private Integer way;
    @ApiModelProperty(value = "考勤类型")
    private Integer type;
    @ApiModelProperty(value = "签到地点")
    private String position;
    @ApiModelProperty(value = "备注，备用字段")
    private String remark;

    public Integer getAttendanceTime() {
        return attendanceTime;
    }

    public void setAttendanceTime(Integer attendanceTime) {
        this.attendanceTime = attendanceTime;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
