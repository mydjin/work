package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.VacateCreate;
import com.xtaller.party.doc.VacatePermit;
import com.xtaller.party.doc.VacateUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.*;
import com.xtaller.party.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by party on 2018/08/15
 */
@Api(value = "17_请假管理")
@RestController
@RequestMapping("/v1/base")
public class VacateApi extends BaseApi {
    @Autowired
    private VacateService vacateService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ScheduleService scheduleService;


    @PostMapping("/vacate")
    @ApiOperation(value = "请假新增")
    public Object createVacate(@RequestBody VacateCreate object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Vacate model = o2c(object, token, Vacate.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = vacateService.exist(W.f(
                W.and("number", "eq", model.getNumber()),
                W.and("scheduleId", "eq", model.getScheduleId()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该用户已提交请假，请不要重复提交！");

        model = vacateService.createVacate(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/vacate")
    @ApiOperation(value = "修改请假")
    public Object updateVacate(@RequestBody VacateUpdate object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Vacate model = o2c(object, token, Vacate.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Vacate data = vacateService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getNumber().equals(model.getNumber())) {
            return R.error("不能修改学号！");
        }

        if (!data.getScheduleId().equals(model.getScheduleId())) {
            return R.error("不能修改课程编号！");
        }


        model.setReviser(userId);
        model = vacateService.updateVacate(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/vacate/{id}")
    @ApiOperation(value = "请假删除")
    public Object deleteVacate(@PathVariable("id") String id,
                               @RequestHeader("token") String token) {

        if (!vacateService.existId(id))
            return R.error("Id数据异常");

        if (vacateService.deleteVacate(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }


    //2018/12/08 htc
    @GetMapping("/vacate/{index}-{size}-{type}-{number}")
    @ApiOperation(value = "读取请假分页列表")
    public Object getVacate(@PathVariable("index") int index,
                            @PathVariable("size") int size,
                            @PathVariable("type") String type,
                            @PathVariable("number") String number,
                            @RequestHeader("token") String token) {


        String userId = getUserIdByCache(token);

        if (V.isEmpty(userId)) {
            return R.error("账号异常，请重新登录");
        }

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }

        String wKey = "";

        Page back = new Page(new ArrayList<JSONObject>(), size, 0, 0, 1);


        if (visible == 1) {//1-全部（组织部）

            if (!V.isEmpty(type))
                wKey += S.apppend(" and a.type = ", type + " ");


            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");

            back = vacateService.page(index, size, wKey);
        } else if (visible == 2) {//2-院级

            String academyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(academyCode)) {
                return R.error("请先完善您的学院信息");
            }
            String numbers = customService.queryUserNumbersByAcademyCode(academyCode);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(back);
            }
            //获取该学院代码下所有的用户信息
            wKey += S.apppend(" and number in (", numbers, ") ");

            if (!V.isEmpty(type))
                wKey += S.apppend(" and a.type = ", type + " ");

            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");
            back = vacateService.page(index, size, wKey);
        } else if (visible == 3) {//3-个人(班主任、培养联系人)

            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }
            String numbers = customService.queryUserNumbersByLinkNumber(loginNumber);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(back);
            }

            wKey += S.apppend(" and number in (", numbers, ") ");

            if (!V.isEmpty(type))
                wKey += S.apppend(" and a.type = ", type + " ");

            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");
//            if (!V.isEmpty(stage))
//                wKey += S.apppend(" and stage = ", stage, " ");
            back = vacateService.page(index, size, wKey);
        } else {
            back = new Page();
        }


        return R.ok(back);

    }


    @GetMapping("/vacate/info/{id}")
    @ApiOperation(value = "请假信息")
    public Object getVacateById(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {


        JSONObject back = vacateService.queryById(id);

        return R.ok("操作成功", back);

    }


    @PutMapping("/vacate/permit")
    @ApiOperation(value = "批假")
    public Object permitVacate(@RequestBody VacatePermit object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        List<SysUser> sysUserList = sysUserService.findByIds(userId);
        if (sysUserList == null || sysUserList.size() == 0)
            return R.error("账号异常，请重新登录");

        SysUser sysUser = sysUserList.get(0);
        //获取登录账号者的学号/工号，即审批人的学号/工号
        String approverNumber = sysUser.getNumber();

        //映射对象
        List<Vacate> vacateList = vacateService.findByIds(object.getId());
        if (vacateList == null || vacateList.size() == 0)
            return R.error("找不到该请假记录");
        Vacate vacate = vacateList.get(0);

        if (vacate.getStatus() == 2)
            return R.error("该请假已审批，请勿重复批假");

        vacate.setStatus(2);//2-已审
        vacate.setResult(object.getResult());
        vacate.setOpinion(object.getOpinion());
        vacate.setApproverNumber(approverNumber);
        vacate.setReviser(userId);

//        List<Attendance> attendanceList = attendanceService.findByScheduleId(vacate.getScheduleId());
        List<Attendance> attendanceList = attendanceService.query(W.f(
                W.and("scheduleId", "eq", vacate.getScheduleId()),
                W.and("number", "eq", vacate.getNumber())
        ));
        if (attendanceList == null || attendanceList.size() <= 0)
            return R.error("找不到该考勤记录");
        Attendance attendance = attendanceList.get(0);
        if (object.getResult() == 1) {
            //1-同意，2-不同意
            //同意后才能设置为已请假
            attendance.setScheduleId(vacate.getScheduleId());
            attendance.setNumber(vacate.getNumber());
            attendance.setStatus(3);
            attendance.setAttendanceTime((int) TimeKit.getTimestamp());
            attendance.setReviser(userId);
            attendance = attendanceService.updateAttendance(attendance);
            if (attendance == null)
                return R.error("批假失败：更新考勤记录失败");
        }


        vacate = vacateService.updateVacate(vacate);
        if (vacate == null)
            return R.error("批假失败");


        List<Schedule> scheduleList = scheduleService.findByIds(vacate.getScheduleId());
        if (scheduleList != null && scheduleList.size() != 0) {
            //获取请假人的number
            String number = vacate.getNumber();

            StringBuffer content = new StringBuffer();
            int result = object.getResult();
            //lw 默认推送消息 18.12.10
            String type = "";
            if (scheduleList.get(0).getType() == 1) {
                type = "课程";
            } else {
                type = "会议";
            }
            content.append("#{name}同学您好，" + "您的“" + scheduleList.get(0).getTopic() + "”" + type + "请假审核结果为：");

            if (result == 1) {
                content.append("“通过”，");
            } else if (result == 2) {
                content.append("“不通过”，");
            }
            content.append("批假人意见为：“" + object.getOpinion() + "”。");
            String theme = "";
            theme = scheduleList.get(0).getTopic();
            Message message = new Message();
            message.setNumber(number);
            message.setTopic("“" + scheduleList.get(0).getTopic() + "”" + type + "请假审核结果");
            message.setContent(content.toString());
            message.setType(scheduleList.get(0).getType());
            message.setServer(pcServer);
            message.setLinkId(scheduleList.get(0).getId());
            customService.sendMessage(message, token);
            // lw 对关系表中的已绑定手机号的学生推送消息 18.12.10
            customService.sendNote(number, theme, content.toString(), 0, token);
            //lw 对关系表中的已绑定微信号的学生循环推送消息
            //fixme:发送微信推送 网址为默认网址
            customService.sendWeixinPush(number, "“" + scheduleList.get(0).getTopic() + "”" + type + "请假审核结果", content.toString(), "http://wechat.party.gxun.club/", "info");


        }

        return R.ok("批假成功", fm2(vacate));


    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("scheduleId", "学习安排Id");
//        verify.put("type", "请假类型");
        verify.put("number", "请假人学号/工号");
        verify.put("reason", "请假理由");
        verify.put("result", "审批结果");
        verify.put("status", "审批状态");
        verify.put("opinion", "审批意见");
        verify.put("approverNumber", "审批人学号/工号");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("scheduleId", "学习安排Id");
//        verify.put("type", "请假类型");
        verify.put("number", "请假人学号/工号");
        verify.put("reason", "请假理由");
        verify.put("result", "审批结果");
        verify.put("status", "审批状态");
        verify.put("opinion", "审批意见");
        verify.put("approverNumber", "审批人学号/工号");
        return verify;
    }


}
