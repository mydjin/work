package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Approval;
import com.xtaller.party.core.model.Report;
import com.xtaller.party.core.model.SurveyRecordChat;
import com.xtaller.party.core.service.impl.ApprovalService;
import com.xtaller.party.core.service.impl.SurveyRecordChatService;
import com.xtaller.party.doc.ReportApproval;
import com.xtaller.party.doc.SurveyRecordChatApproval;
import com.xtaller.party.doc.SurveyRecordChatCreate;
import com.xtaller.party.doc.SurveyRecordChatUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
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
@Api(value = "30_考察与谈话记录管理")
@RestController
@RequestMapping("/v1/base")
public class SurveyRecordChatApi extends BaseApi {
    @Autowired
    private SurveyRecordChatService surveyRecordChatService;
    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/survey_record_chat")
    @ApiOperation(value = "考察与谈话记录新增")
    public Object createSurveyRecordChat(@RequestBody SurveyRecordChatCreate object,
                                         @RequestHeader("token") String token) {

        //映射对象
        SurveyRecordChat model = o2c(object, token, SurveyRecordChat.class);


        if (!checkUserDataAuth(token, model.getNumber())) {
            return R.error("您没有权限新增" + model.getNumber() + "的记录");
        }

        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        Integer maxCount = surveyRecordChatService.queryMaxCount(" and number = '" + model.getNumber() + "' and type = " + model.getType() + " and stage = '" + model.getStage() + "'");
        if (V.isEmpty(maxCount)) {
            maxCount = 0;
        }
        model.setCount(maxCount + 1);

        model = surveyRecordChatService.createSurveyRecordChat(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/survey_record_chat")
    @ApiOperation(value = "修改考察与谈话记录")
    public Object updateSurveyRecordChat(@RequestBody SurveyRecordChatUpdate object,
                                         @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        SurveyRecordChat model = o2c(object, token, SurveyRecordChat.class);


        if (!checkUserDataAuth(token, model.getNumber())) {
            return R.error("您没有权限修改" + model.getNumber() + "的记录");
        }

        SurveyRecordChat data = surveyRecordChatService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getNumber().equals(model.getNumber())) {
            return R.error("不能修改学号！");
        }

        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        model.setReviser(userId);
        model = surveyRecordChatService.updateSurveyRecordChat(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }


    @GetMapping("/survey_record_chat/{index}-{size}-{type}-{number}")
    @ApiOperation(value = "读取考察与谈话记录分页列表")
    public Object getSurveyRecordChat(@PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("type") String type,
                                      @PathVariable("number") String number,
                                      @RequestHeader("token") String token) {

        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);


        String wKey = "";
        if (!V.isEmpty(type))
            wKey = S.apppend(" and type = '", type, "' ");
        if (!V.isEmpty(number))
            wKey += S.apppend(" and number like '%", number, "%' ");

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }


        if (visible == 1) {//1-全部（组织部）

            return R.ok(surveyRecordChatService.page(index, size, wKey));


        } else if (visible == 2) {//2-院级

            String academyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(academyCode)) {
                return R.error("请先完善您的学院信息");
            }

            String numbers = customService.queryUserNumbersByAcademyCode(academyCode);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            }
            //获取该学院代码下所有的学生的考察谈话
            wKey += S.apppend(" and number in (", numbers, ") ");


            return R.ok(surveyRecordChatService.page(index, size, wKey));

        } else if (visible == 3) {//3-个人(班主任、联络员)
            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }

            String numbers = customService.queryUserNumbersByLinkNumber(loginNumber);

            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            }
            wKey += S.apppend(" and number in (", numbers, ") ");

            return R.ok(surveyRecordChatService.page(index, size, wKey));
        } else {
            return R.error("您没有权限查看");
        }


    }


    private Map verify() {
        Map verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("surveyTime", "请输入考察/谈话时间");
        verify.put("surveyCondition", "请输入考察/谈话情况");
      /*  verify.put("approvalStatus", "请输入审核状态");
        verify.put("approvalOpinion", "请输入审核意见");
        verify.put("approvalResult", "请输入审核结果");
        verify.put("approverNumber", "请输入审核人学号/工号");*/
        verify.put("type", "请输入类型");
        verify.put("stage", "请输入阶段");
//        verify.put("count", "请输入次数");
        return verify;
    }

    private Map updateVerify() {
        Map verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("surveyTime", "请输入考察/谈话时间");
        verify.put("surveyCondition", "请输入考察/谈话情况");
        /*verify.put("approvalStatus", "请输入审核状态");
        verify.put("approvalOpinion", "请输入审核意见");
        verify.put("approvalResult", "请输入审核结果");
        verify.put("approverNumber", "请输入审核人学号/工号");*/
        verify.put("type", "请输入类型");
        verify.put("stage", "请输入阶段");
//        verify.put("count", "请输入次数");
        return verify;
    }


    @PutMapping("/survey_record_chat/approval/")
    @ApiOperation(value = "考察与谈话记录审核")
    public Object surveyRecordChatApproval(@RequestBody SurveyRecordChatApproval object,
                                           @RequestHeader("token") String token) {
        String userId = getUserIdByCache(token);
        System.out.println("userId:" + userId);

        //修改审核表（状态、结果、意见、修改人、修改时间）
        List<Approval> approvalList = approvalService.findByApprovalId(object.getId());
        if (approvalList == null || approvalList.size() <= 0)
            return R.error("审核失败：没有找到审核记录");
        Approval approval = approvalList.get(0);
        if (!checkUserDataAuth(token, approval.getNumber())) {
            return R.error("您没有权限修改" + approval.getNumber() + "的记录");
        }

        approval.setStatus(2);
        approval.setResult(object.getApprovalResult());
        approval.setOpinion(object.getApprovalOpinion());
        approval.setReviser(userId);

        //修改考察与谈话记录表（状态、结果、意见、修改人、修改时间）
        List<SurveyRecordChat> surveyRecordChatList = surveyRecordChatService.findByIds(object.getId());
        if (surveyRecordChatList == null || surveyRecordChatList.size() <= 0)
            return R.error("审核失败：没有找到思想汇报记录");
        SurveyRecordChat surveyRecordChat = surveyRecordChatList.get(0);
        surveyRecordChat.setApprovalStatus(2);
        surveyRecordChat.setApprovalOpinion(object.getApprovalOpinion());
        surveyRecordChat.setApprovalResult(object.getApprovalResult());
        surveyRecordChat.setReviser(userId);

        surveyRecordChat = surveyRecordChatService.updateSurveyRecordChat(surveyRecordChat);
        if (surveyRecordChat == null)
            return R.error("审核失败：审核记录修改失败");

        approval = approvalService.updateApproval(approval);
        if (approval == null)
            return R.error("审核失败：思想汇报记录修改失败");
        return R.ok("审核成功");
    }


}
