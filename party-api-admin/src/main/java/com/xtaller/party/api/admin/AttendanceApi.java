package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Attendance;
import com.xtaller.party.core.service.impl.AttendanceService;
import com.xtaller.party.core.service.impl.SysUserService;
import com.xtaller.party.doc.AttendanceCreate;
import com.xtaller.party.doc.AttendanceUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by party on 2018/08/13
 */
@Api(value = "15_考勤管理")
@RestController
@RequestMapping("/v1/base")
public class AttendanceApi extends BaseApi {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private SysUserService userService;

    @PostMapping("/attendance")
    @ApiOperation(value = "考勤新增")
    public Object createAttendance(@RequestBody AttendanceCreate object,
                                   @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Attendance model = o2c(object, token, Attendance.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        if (model.getStatus() == 2) {//2-已签到的检验是否输入考勤时间
            if (V.isEmpty(model.getAttendanceTime())) {
                return R.error("请选择考勤时间");
            }
        } else {
            model.setAttendanceTime(null);
        }

        if (!userService.existNumber(model.getNumber()))
            return R.error("不存在的学生学号");

        //校验用户学号/工号是否重复
        Boolean exist = attendanceService.exist(W.f(
                W.and("scheduleId", "eq", model.getScheduleId()),
                W.and("number", "eq", model.getNumber()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该用户已经有考勤记录");

        if (V.isEmpty(model.getWay())) {
            model.setWay(null);
        }

        model = attendanceService.createAttendance(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/attendance")
    @ApiOperation(value = "修改考勤")
    public Object updateAttendance(@RequestBody AttendanceUpdate object,
                                   @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Attendance model = o2c(object, token, Attendance.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        if (!userService.existNumber(model.getNumber()))
            return R.error("不存在的学生学号");
        if (V.isEmpty(model.getWay())) {
            model.setWay(null);
        }


        if (model.getStatus() == 2) {//2-已签到的检验是否输入考勤时间
            if (V.isEmpty(model.getAttendanceTime())) {
                return R.error("请选择考勤时间");
            }
        } else {
            model.setAttendanceTime(null);
        }

        model.setReviser(userId);
        model = attendanceService.updateAttendance(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/attendance/{id}")
    @ApiOperation(value = "考勤删除")
    public Object deleteAttendance(@PathVariable("id") String id,
                                   @RequestHeader("token") String token) {

        if (!attendanceService.existId(id))
            return R.error("Id数据异常");

        if (attendanceService.delete(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/attendance/{index}-{size}-{key}")
    @ApiOperation(value = "读取考勤分页列表")
    public Object getAttendance(@PathVariable("index") int index,
                                @PathVariable("size") int size,
                                @PathVariable("key") String key,
                                @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))

            wKey = S.apppend(" and number like '%", key, "%' ");
        return R.ok(attendanceService.page(index, size, wKey));

    }


    @GetMapping("/attendance/type/{index}-{size}-{type}-{number}")
    @ApiOperation(value = "通过类型读取考勤分页列表")
    public Object getAttendanceByType(@PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("type") int type,
                                      @PathVariable("number") String number,

                                      @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(type))
            wKey = S.apppend(" and type =", type, " ");
        if (!V.isEmpty(number))
            wKey = S.apppend(" and number like '%", number, "%' ");
        return R.ok(attendanceService.page(index, size, wKey));

    }

    @GetMapping("/attendance")
    @ApiOperation(value = "读取考勤所有列表")
    public Object getAllAttendance(@RequestHeader("token") String token) {

        return R.ok(attendanceService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
//        verify.put("attendanceTime", "请选择考勤时间");
        verify.put("scheduleId", "请选择主题");
        verify.put("status", "请选择考勤状态");
        verify.put("number", "请输入学生学号");
//        verify.put("way", "签到方式");
        verify.put("type", "请选择考勤类型");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
//        verify.put("attendanceTime", "请选择考勤时间");
        verify.put("scheduleId", "请选择主题");
        verify.put("status", "请选择考勤状态");
        verify.put("number", "请输入学生学号");
//        verify.put("way", "签到方式");
        verify.put("type", "请选择考勤类型");
        return verify;
    }

}



