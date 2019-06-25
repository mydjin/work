package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.ReleasedScheduleCancel;
import com.xtaller.party.doc.ReleasedScheduleModify;
import com.xtaller.party.doc.ScheduleCreate;
import com.xtaller.party.doc.ScheduleUpdate;
import com.xtaller.party.doc.ScheduleSelectStudent;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Created by party on 2018/08/13
 */
@Api(value = "13_学习安排管理")
@RestController
@RequestMapping("/v1/base")
public class ScheduleApi extends BaseApi {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private AcademyInfoService academyInfoService;
    @Autowired
    private UserBaseInfoService userBaseInfoService;
    @Autowired
    private SysUserService userService;

    @PostMapping("/schedule")
    @ApiOperation(value = "学习安排新增")
    public Object createSchedule(@RequestBody ScheduleCreate object,
                                 @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Schedule model = o2c(object, token, Schedule.class);
        if (!userService.existNumber(model.getLinkmanNumber()))
            return R.error("联系人不存在");
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = scheduleService.exist(W.f(
                W.and("topic", "eq", model.getTopic()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该标题已存在请更换！");


        if (model.getStartTime() > model.getEndTime()) {
            return R.error("开始时间应小于结束时间");
        }


        model = scheduleService.createSchedule(model);

        if (model == null) {

            return R.error("新增失败");

        } else {

            return R.ok("新增成功", fm2(model));
        }

    }

    @PutMapping("/schedule")
    @ApiOperation(value = "修改学习安排")
    public Object updateSchedule(@RequestBody ScheduleUpdate object,
                                 @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Schedule model = o2c(object, token, Schedule.class);

        if (!userService.existNumber(model.getLinkmanNumber()))
            return R.error("联系人不存在");
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        if (model.getStartTime() > model.getEndTime()) {
            return R.error("开始时间应小于结束时间");
        }

        Schedule data = scheduleService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getTopic().equals(model.getTopic())) {
            Boolean exist = scheduleService.exist(W.f(
                    W.and("topic", "eq", model.getTopic()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("该标题已存在请更换！");

        }


        model.setReviser(userId);
        model = scheduleService.updateSchedule(model);


        if (model == null) {

            return R.error("修改失败");

        } else {

            return R.ok("修改成功", fm2(model));
        }
    }

    @DeleteMapping("/schedule/{id}")
    @ApiOperation(value = "学习安排删除")
    public Object deleteSchedule(@PathVariable("id") String id,
                                 @RequestHeader("token") String token) {

        if (!scheduleService.existId(id))
            return R.error("Id数据异常");

        if (scheduleService.deleteSchedule(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/schedule/{index}-{size}-{type}-{topic}")
    @ApiOperation(value = "读取安排分页列表")
    public Object getSchedule(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("type") String type,
                              @PathVariable("topic") String topic,
                              @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(type))
            wKey = S.apppend(" and type = ", type, " ");

        if (!V.isEmpty(topic))
            wKey += S.apppend(" and topic like '%", topic, "%' ");
        return R.ok(scheduleService.page(index, size, wKey));

    }

    @GetMapping("/schedule/byType/{type}")
    @ApiOperation(value = "读取学习安排所有列表byType")
    public Object getAllScheduleByType(
            @PathVariable("type") String type,
            @RequestHeader("token") String token) {
        //status!=1：未发布的不能考勤
        return R.ok(scheduleService.queryAll(" and status!=1 and type=" + type + " "));

    }


    @GetMapping("/schedule")
    @ApiOperation(value = "读取学习安排所有列表")
    public Object getAllSchedule(@RequestHeader("token") String token) {

        return R.ok(scheduleService.queryAll(""));

    }


    @PutMapping("/schedule/release/{id}")
    @ApiOperation(value = "发布学习安排")
    public Object releaseSchedule(@PathVariable("id") String id,
                                  @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        List<Schedule> scheduleList = scheduleService.findByIds(id);
        if (scheduleList == null || scheduleList.size() == 0)
            return R.error("学习安排Id异常");

        Schedule schedule = scheduleList.get(0);
        Long nowTime = TimeKit.getTimestamp();
        Long startTime = schedule.getStartTime().longValue();
        Long endTime = schedule.getEndTime().longValue();
        int status = schedule.getStatus();

        if (status == 2 || status == 3) {
            return R.error("该安排已发布，请勿重复发布");
        } else if (status == 4) {//4-已取消
            return R.error("该安排已取消，不可发布");
        } else if (status == 5) {//5-已结束
            return R.error("该安排已结束");
        }

        if (startTime > endTime) {
            return R.error("结束时间在开始时间之前，请重新设置");
        }
        if (nowTime > startTime) {
            return R.error("当前时间已超过开始时间，请重新设置开始时间");
        }
        if (nowTime > endTime) {
            return R.error("当前时间已超过结束时间，请重新设置结束时间");
        }

        schedule.setReviser(userId);
        schedule.setStatus(2);//发布成功，2-未开始

        schedule = scheduleService.updateSchedule(schedule);


        if (schedule == null) {

            return R.error("发布失败");

        } else {

            return R.ok("发布成功", fm2(schedule));

        }


    }


    @PutMapping("/schedule/releaseSend/{id}")
    @ApiOperation(value = "发布学习安排推送")
    public Object releaseScheduleSend(@PathVariable("id") String id,
                                      @RequestHeader("token") String token) {


        List<Schedule> scheduleList = scheduleService.findByIds(id);
        if (scheduleList == null || scheduleList.size() == 0)
            return R.error("学习安排推送异常");

        Schedule schedule = scheduleList.get(0);
        Long startTime = schedule.getStartTime().longValue();
        int status = schedule.getStatus();

        String BeginTimeStr = TimeKit.stampToDate(startTime * 1000L, "yyyy年MM月dd日HH点mm分");
        if (status == 2 || status == 3) {
            JSONArray rangeArray = JSON.parseArray(schedule.getRange());
            List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray);

            //根据发布范围添加学生关系，默认推送信息提醒
            JSONObject linkuser = userBaseInfoService.queryUserByNumber(schedule.getLinkmanNumber());

            StringBuffer content = new StringBuffer();
            String type = "";
            if (schedule.getType() == 1) {
                type = "课程安排";
            } else {
                type = "会议安排";
            }

            content.append("#{name}同学你好，“" + schedule.getTopic() + "”将于" + BeginTimeStr + "在" + schedule.getPlace() +
                    "举行，主讲人：" + schedule.getSpeaker() + ",请按时参加，如不能到场，请联系" + linkuser.getString("name") +
                    ",电话：" + schedule.getLinkmanPhone() + "。");
            List<Message> messages = new ArrayList<>();


            List<Attendance> attendances = new ArrayList<>();

            for (JSONObject number : checkNumbers) {
                String userId = getUserIdByCache(token);

                String wKey = "";

                wKey = S.apppend(" and number = '", number.getString("number"), "' and scheduleId = '", schedule.getId(), "' ");



                Attendance attendance = o2c(new Attendance(), token, Attendance.class);
                attendance.setScheduleId(schedule.getId());
                attendance.setNumber(number.getString("number"));
                attendance.setType(schedule.getType());
                attendance.setCreator(userId);

                List<JSONObject> checkAttendance = attendanceService.queryAll(wKey);
                if (checkAttendance == null || checkAttendance.size() <= 0) {
                    //  attendanceService.createAttendance(attendance);
                    attendances.add(attendance);
                } else {
                    attendance.setId(checkAttendance.get(0).getString("id"));
                    attendance.setReviser(userId);
                    attendanceService.updateAttendance(attendance);
                }
                Message message = new Message();
                message.setNumber(number.getString("number"));
                message.setTopic("“" + schedule.getTopic() + "”" + type + "发布通知");
                message.setContent(content.toString());
                message.setType(schedule.getType());
                message.setServer(pcServer);
                message.setLinkId(schedule.getId());
                messages.add(message);

            }
            int result = customService.sendMessages(messages, token);


            int attenceCount = attendanceService.inserRows(attendances);

            // lw 根据发布范围进行推送 18.12.10
            int noteSum = 0, pushSum = 0;
            if (schedule.getIsNote() == 2 || schedule.getIsPush() == 2) {

                for (JSONObject number : checkNumbers) {
                    // lw 默认推送消息 18.12.4
                    JSONObject user = userBaseInfoService.queryUserByNumber(number.getString("number"));
                    // lw 对关系表中的已绑定手机号的学生循环推送消息 18.12.4
                    if (schedule.getIsNote() == 2) {
                        if (customService.sendNote(number.getString("number"), type, content.toString(), 0, token)) {
                            noteSum++;
                        }

                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //lw 对关系表中的已绑定微信号的学生循环推送消息 19.2.23
                    if (schedule.getIsPush() == 2) {
                        //fixme:发送微信推送 网址为默认网址
                        if (customService.sendWeixinPush(number.getString("number"), "“" + schedule.getTopic() + "”" + type + "发布通知", content.toString(), "http://wechat.party.gxun.club/", "info"))
                            pushSum++;
                    }

                }
            }
            String back = "";
            if (schedule.getIsNote() == 2) {
                back += "，短信推送" + noteSum + "人";
            }
            if (schedule.getIsPush() == 2) {
                back += "，微信推送" + pushSum + "人";

            }


            return R.ok("“" + schedule.getTopic() + "”发布推送成功,已消息推送" + result + "人" + back + "。", fm2(schedule));

        } else {
            return R.error("“" + schedule.getTopic() + "”发布推送失败");


        }


    }

    @PostMapping("/schedule/modify")
    @ApiOperation(value = "修改已发布的学习安排")
    public Object modifyReleasedSchedule(@RequestBody ReleasedScheduleModify object,
                                         @RequestHeader("token") String token) {


        String userId = getUserIdByCache(token);

        JSONObject check = V.checkEmpty(modifyVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        List<Schedule> scheduleList = scheduleService.findByIds(object.getId());
        if (scheduleList == null || scheduleList.size() == 0)
            return R.error("该学习安排数据异常");

        Schedule schedule = scheduleList.get(0);


        Long nowTime = TimeKit.getTimestamp();
        Long newStartTime = object.getStartTime().longValue();
        Long newEndTime = object.getEndTime().longValue();


        if (newStartTime > newEndTime) {
            return R.error("开始时间应在结束时间之前，请重新设置");
        }

        if (newStartTime < nowTime) {
            return R.error("开始时间应大于当前时间，请重新设置开始时间");
        }
        if (newEndTime < nowTime) {
            return R.error("结束时间应大于当前时间，请重新设置结束时间");
        }


        cacheKit.setVal("modify-oldSschedule-" + schedule.getId(), fm2(schedule).toJSONString(), 0);
        cacheKit.setVal("modify-newSschedule-" + schedule.getId(), fm2(object).toJSONString(), 0);


        schedule = o2c(object, token, Schedule.class);


        schedule.setReviser(userId);
        schedule = scheduleService.updateSchedule(schedule);
        if (schedule == null) {
            cacheKit.deleteVal("modify-newSschedule-" + schedule.getId());
            cacheKit.deleteVal("modify-oldSschedule-" + schedule.getId());

            return R.error("修改失败");
        } else {
            return R.ok("修改成功", fm2(schedule));

        }

    }


    @PostMapping("/schedule/modifySend/{id}")
    @ApiOperation(value = "修改已发布的学习安排推送")
    public Object modifyReleasedScheduleSend(@PathVariable("id") String id, @RequestHeader("token") String token) {


        JSONObject newSscheduleObj = JSON.parseObject(cacheKit.getVal("modify-newSschedule-" + id));
        ReleasedScheduleModify object = JSON.toJavaObject(newSscheduleObj, ReleasedScheduleModify.class);

        JSONObject oldSscheduleObj = JSON.parseObject(cacheKit.getVal("modify-oldSschedule-" + id));
        Schedule schedule = JSON.toJavaObject(oldSscheduleObj, Schedule.class);

        if (object == null || schedule == null) {
            return R.error("修改推送失败");

        } else {

            cacheKit.deleteVal("modify-newSschedule-" + id);
            cacheKit.deleteVal("modify-oldSschedule-" + id);


            Long oldStartTime = schedule.getStartTime().longValue();
            Long oldEndTime = schedule.getEndTime().longValue();
            Long newStartTime = object.getStartTime().longValue();
            Long newEndTime = object.getEndTime().longValue();

            //对关系表中的已绑定手机号的学生循环发送短信
            //由于xx原因，原定于xx时间xx地点的xx安排改为xx时间xx地点
            if(V.isEmpty(schedule.getRange())){
                return  R.error("未设置发布范围！");
            }
            JSONArray rangeArray = JSON.parseArray(schedule.getRange());
            List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray);


            //修改原因
            String modifyReason = object.getModifyReason();
            String oldStartTimeStr = TimeKit.stampToDate(oldStartTime * 1000L, "yyyy年MM月dd日HH点mm分");
            String oldEndTimeStr = TimeKit.stampToDate(oldEndTime * 1000L, "yyyy年MM月dd日HH点mm分");
            String oldPlace = schedule.getPlace();
            String oldTopic = schedule.getTopic();

            String newStartTimeStr = TimeKit.stampToDate(newStartTime * 1000L, "yyyy年MM月dd日HH点mm分");
            String newEndTimeStr = TimeKit.stampToDate(newEndTime * 1000L, "yyyy年MM月dd日HH点mm分");
            String newPlace = object.getPlace();

            String typeName = "安排";
            switch (schedule.getType()) {
                case 1:
                    typeName = "课程安排";
                    break;
                case 2:
                    typeName = "会议安排";
                    break;
                default:
                    break;
            }

            //1.如果是时间变动或者地点变动则短信提醒
            //2.如果选择了"需要发短信通知"则发送

            if (!oldStartTimeStr.equals(newStartTimeStr) || !oldEndTimeStr.equals(newEndTimeStr)
                    || !oldPlace.trim().equals(newPlace.trim())) {

                List<Message> messages = new ArrayList<>();
                StringBuffer content = new StringBuffer();
                content.append("#{name}同学你好，由于“" + modifyReason + "”原因，");
                content.append("原定于" + oldStartTimeStr + "到" + oldEndTimeStr + "在" + oldPlace);
                content.append("的“" + oldTopic + "”" + typeName + "改为");
                content.append(newStartTimeStr + "到" + newEndTimeStr + "在" + newPlace + "开始。");

                // lw 默认推送消息 18.12.10
                for (JSONObject number : checkNumbers ) {

                    Message message = new Message();
                    message.setNumber(number.getString("number"));
                    message.setTopic("“" + oldTopic + "”" + typeName + "更改通知");
                    message.setContent(content.toString());
                    message.setType(schedule.getType());
                    message.setServer(pcServer);
                    message.setLinkId(schedule.getId());
                    messages.add(message);
                }
                int result = customService.sendMessages(messages, token);

                int noteSum = 0, pushSum = 0;
                //lw 对关系表中的已绑定手机号的学生循环推送消息 18.12.4
                if (schedule.getIsNote() == 2 || schedule.getIsPush() == 2) {
                    for (JSONObject number : checkNumbers) {
                        if (schedule.getIsNote() == 2) {
                            if (customService.sendNote(number.getString("number"), typeName, content.toString(), 0, token)) {
                                noteSum++;
                            }

                            try {
                                sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


                        //lw 对关系表中的已绑定微信号的学生循环推送消息 19.2.23
                        if (schedule.getIsPush() == 2) {
                            //fixme:发送微信推送 网址为默认网址
                            if (customService.sendWeixinPush(number.getString("number"), "“" + oldTopic + "”" + typeName + "更改通知", content.toString(), "http://wechat.party.gxun.club/", "info"))
                                pushSum++;
                        }
                    }
                }

                String back = "";
                if (schedule.getIsNote() == 2) {
                    back += "，短信推送" + noteSum + "人";
                }
                if (schedule.getIsPush() == 2) {
                    back += "，微信推送" + pushSum + "人";

                }

                return R.ok(object.getTopic() + "修改推送成功,已消息推送" + result + "人" + back + "。", fm2(schedule));

            } else {
                return R.ok(object.getTopic() + "修改推送成功", fm2(schedule));

            }


        }

    }

    @PutMapping("/schedule/cancel")
    @ApiOperation(value = "取消已发布的学习安排")
    public Object cancelReleasedSchedule(@RequestBody ReleasedScheduleCancel object,
                                         @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //取消原因
        String cancelReason = object.getCancelReason();
        if (cancelReason == null || cancelReason.length() == 0) {
            return R.error("请填写取消原因");
        }


        List<Schedule> scheduleList = scheduleService.findByIds(object.getId());
        if (scheduleList == null || scheduleList.size() == 0)
            return R.error("该学习安排数据异常");

        Schedule schedule = scheduleList.get(0);

        if (schedule.getStatus() == 1) {
            return R.error("该安排未发布，不可取消");
        }

        if (schedule.getStatus() == 4) {
            return R.error("该安排已取消，请勿重复取消");
        }

        Long nowTime = TimeKit.getTimestamp();
        Long startTime = schedule.getStartTime().longValue();
        Long endTime = schedule.getEndTime().longValue();

        if (nowTime > startTime && nowTime < endTime) {
            return R.error("该安排已开始");
        }

        if (nowTime > endTime) {
            return R.error("该安排已结束");
        }


        schedule.setStatus(4);//4-取消
        schedule.setReviser(userId);
        schedule = scheduleService.updateSchedule(schedule);
        if (schedule == null) {
            return R.error("取消失败");
        } else {
            cacheKit.setVal("cancel-reason-" + schedule.getId(), cancelReason, 0);
            cacheKit.setVal("cancel-schedule-" + schedule.getId(), fm2(schedule).toJSONString(), 0);

            return R.ok("取消成功", fm2(schedule));
        }


    }


    @PutMapping("/schedule/cancelSend/{id}")
    @ApiOperation(value = "取消已发布的学习安排推送")
    public Object cancelReleasedScheduleSend(@PathVariable("id") String id,
                                             @RequestHeader("token") String token) {


        //取消原因
        String cancelReason = cacheKit.getVal("cancel-reason-" + id);

        JSONObject cancleSscheduleObj = JSON.parseObject(cacheKit.getVal("cancel-schedule-" + id));

        cacheKit.deleteVal("cancel-reason-" + id);
        cacheKit.deleteVal("cancel-schedule-" + id);

        if (cancelReason == null || cancleSscheduleObj == null) {
            return R.error("取消推送失败");

        } else {

            Schedule schedule = JSON.toJavaObject(cancleSscheduleObj, Schedule.class);

            Long startTime = schedule.getStartTime().longValue();
            Long endTime = schedule.getEndTime().longValue();


            //对关系表中的已绑定手机号的学生循环发送短信
            //由于xx原因，原定于xx时间xx地点的xx安排取消
            if(V.isEmpty(schedule.getRange())){
                return  R.error("未设置发布范围！");
            }
            JSONArray rangeArray = JSON.parseArray(schedule.getRange());
            List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray);

            //修改原因
            String startTimeStr = TimeKit.stampToDate(startTime * 1000L, "yyyy年MM月dd日HH点mm分");
            String endTimeStr = TimeKit.stampToDate(endTime * 1000L, "yyyy年MM月dd日HH点mm分");
            String place = schedule.getPlace();
            String topic = schedule.getTopic();

            String type = "";
            if (schedule.getType() == 1) {
                type = "课程安排";
            } else {
                type = "会议安排";
            }

            StringBuffer content = new StringBuffer();
            content.append("#{name}同学你好，由于“" + cancelReason + "”原因，");
            content.append("原定于" + startTimeStr + "到" + endTimeStr + "在" + place);
            content.append("的“" + topic + "”" + type + "取消。");
            List<Message> messages = new ArrayList<>();
            // lw 默认推送消息 18.12.10

            for (JSONObject number : checkNumbers) {
                Message message = new Message();
                message.setNumber(number.getString("number"));
                message.setTopic("“" + topic + "”" + type + "取消通知");
                message.setContent(content.toString());
                message.setType(schedule.getType());
                message.setServer(pcServer);
                messages.add(message);
            }

            int result = customService.sendMessages(messages, token);

            int noteSum = 0, pushSum = 0;
            if (schedule.getIsNote() == 2 || schedule.getIsPush() == 2) {

                for (JSONObject number :checkNumbers) {
                    //lw 对关系表中的已绑定手机号的学生循环推送消息 18.12.4
                    if (schedule.getIsNote() == 2) {
                        if (customService.sendNote(number.getString("number"), type, content.toString(), 0, token)) {
                            noteSum++;
                        }

                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    //lw 对关系表中的已绑定微信号的学生循环推送消息 19.2.23
                    if (schedule.getIsPush() == 2) {
                        //fixme:发送微信推送 网址为默认网址
                        if (customService.sendWeixinPush(number.getString("number"), "“" + topic + "”" + type + "取消通知", content.toString(), "http://wechat.party.gxun.club/", "info"))
                            pushSum++;

                    }


                }

            }

            String back = "";
            if (schedule.getIsNote() == 2) {
                back += "，短信推送" + noteSum + "人";
            }
            if (schedule.getIsPush() == 2) {
                back += "，微信推送" + pushSum + "人";

            }

            return R.ok("“" + schedule.getTopic() + "”" + type + "取消推送成功,已消息推送" + result + "人" + back + "。", fm2(schedule));
        }

    }


    @PutMapping("/schedule/selectStudent")
    @ApiOperation(value = "线下学习学生安排")
    public Object scheduleSelectStudent(@RequestBody ScheduleSelectStudent object,
                                        @RequestHeader("token") String token) {

        String usedId = getUserIdByCache(token);
        if (object.getNumber().length <= 0)
            return R.error("未选择学生");
        for (int i = 0; i < object.getNumber().length; i++) {
            Attendance  attendance = new Attendance();
            attendance.setNumber(object.getNumber()[i]);
            attendance.setScheduleId(object.getScheduleId());
            attendance.setCreator(usedId);
            attendance = attendanceService.createAttendance(attendance);
            if (attendance == null)
                return R.error("新增学生选课记录失败");
        }
        return R.ok("安排成功");

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("summary", "请输入摘要");
        verify.put("topic", "请输入主题");
        verify.put("place", "请输入地点");
//        verify.put("status", "课程状态");
        verify.put("score", "请输入总分值");
        verify.put("type", "请选择类型");
        verify.put("speaker", "请输入主讲人姓名");
        verify.put("startTime", "请选择开始时间");
        verify.put("endTime", "请选择结束时间");
//        verify.put("isNote", "是否发短信通知");
//        verify.put("isPush", "是否推送");
        verify.put("checkType", "请选择考核形式");
        verify.put("linkmanNumber", "请输入联系人学号/工号");
        verify.put("linkmanPhone", "请输入联系电话");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("summary", "请输入摘要");
        verify.put("topic", "请输入主题");
        verify.put("place", "请输入地点");
//        verify.put("status", "课程状态");
        verify.put("score", "请输入总分值");
        verify.put("type", "请选择类型");
        verify.put("speaker", "请输入主讲人姓名");
        verify.put("startTime", "请选择开始时间");
        verify.put("endTime", "请选择结束时间");
//        verify.put("isNote", "是否发短信通知");
//        verify.put("isPush", "是否推送");
        verify.put("checkType", "请选择考核形式");
        verify.put("linkmanNumber", "请输入联系人学号/工号");
        verify.put("linkmanPhone", "请输入联系电话");
        return verify;
    }


    private Map<String, String> modifyVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("place", "请输入地点");
        verify.put("startTime", "请选择开始时间");
        verify.put("endTime", "请选择结束时间");
        verify.put("modifyReason", "请输入修改原因");
        return verify;
    }

}
