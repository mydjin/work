package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.ReportApproval;
import com.xtaller.party.doc.ReportCreate;
import com.xtaller.party.doc.ReportUpdate;
import com.xtaller.party.utils.tool.RemoveHTML;
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
 * Created by party on 2018/08/10
 */
@Api(value = "12_思想汇报管理")
@RestController
@RequestMapping("/v1/base")
public class ReportApi extends BaseApi {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/report/info/{id}-{stage}")
    @ApiOperation(value = "思想汇报信息")
    public Object getReportById(@PathVariable("id") String id,
                                @PathVariable("stage") int stage,
                                @RequestHeader("token") String token) {

        JSONObject back = reportService.queryById(id, stage);

        return R.ok("操作成功", back);

    }

    @GetMapping("/report/{index}-{size}-{number}")
    @ApiOperation(value = "读取思想汇报分页列表")
    public Object getReport(@PathVariable("index") int index,
                            @PathVariable("size") int size,
                            @PathVariable("number") String number,
                            @RequestHeader("token") String token) {


        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }

        String wKey = "";

        Page back = new Page(new ArrayList<JSONObject>(), size, 0, 0, 1);

        if (visible == 1) {//1-全部（组织部）

            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");

            back = reportService.personPage(index, size, wKey);
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

            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");
            back = reportService.personPage(index, size, wKey);
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

            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");
//            if (!V.isEmpty(stage))
//                wKey += S.apppend(" and stage = ", stage, " ");
            back = reportService.personPage(index, size, wKey);
        } else {
            back = new Page();
        }
        return R.ok(back);

    }


    @PutMapping("/report/approval")
    @ApiOperation(value = "审核思想汇报:初审")
    public Object reportApproval(@RequestBody ReportApproval object,
                                 @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        List<SysUser> sysUserList = sysUserService.findByIds(userId);
        if (sysUserList == null || sysUserList.size() == 0) {
            return R.error("登录超时，请重新登录");
        }

        SysUser sysUser = sysUserList.get(0);
        String number = sysUser.getNumber();

        //修改审核表（状态、结果、意见、修改人、修改时间）
        List<Approval> approvalList = approvalService.findByApprovalId(object.getId());
        if (approvalList == null || approvalList.size() <= 0)
            return R.error("审核失败：没有找到审核记录");
        Approval approval = approvalList.get(0);
        if (!checkUserDataAuth(token, approval.getNumber())) {
            return R.error("您没有权限审核" + approval.getNumber() + "的记录");
        }

        approval.setApproverNumber(number);
        approval.setStatus(2);
        approval.setResult(object.getResult());
        approval.setOpinion(object.getOpinion());
        approval.setReviser(userId);

        //修改思想汇报表（状态、结果、意见、修改人、修改时间）
        List<Report> reportList = reportService.findByIds(object.getId());
        if (reportList == null || reportList.size() <= 0)
            return R.error("审核失败：没有找到思想汇报记录");
        Report report = reportList.get(0);
//        report.setStatus(2);
//        report.setOpinion(object.getOpinion());
//        report.setResult(object.getResult());
        report.setReviser(userId);

        report = reportService.updateReport(report);
        if (report == null)
            return R.error("审核失败：审核记录修改失败");

        approval = approvalService.updateApproval(approval);
        if (approval == null)
            return R.error("审核失败：思想汇报记录修改失败");


        StringBuffer content = new StringBuffer();
        int result = object.getResult();

        content.append("#{name}同学您好，您的第" + report.getCount() + "次思想汇报初审结果为：");
        if (result == 1) {
            content.append("“通过”，");
            Approval recheckApproval = new Approval();
            recheckApproval.setNumber(approval.getNumber());
            recheckApproval.setStage(2);
            recheckApproval.setIsConfirm(1);//1-未确认提交，审核时不可见
            recheckApproval.setApproverNumber(approval.getApproverNumber());
            recheckApproval.setApprovalType(approval.getApprovalType());
            recheckApproval.setStatus(1);
            recheckApproval.setApprovalId(approval.getApprovalId());
            recheckApproval = approvalService.createApproval(recheckApproval);
            if (recheckApproval == null) {
                return R.error("新增到复审失败");
            }

        } else if (result == 2) {
            content.append("“不通过”，");
        }
        content.append("审核人意见为：“" + RemoveHTML.Html2Text(object.getOpinion()) + "”。");
        Message message = new Message();
        message.setNumber(number);
        message.setTopic("第" + report.getCount() + "次思想汇报初审结果");
        message.setContent(content.toString());
        message.setType(5);
        message.setServer(pcServer);
        message.setLinkId(report.getId());
        //发送消息提醒
        customService.sendMessage(message, token);
        //fixme:发送微信推送 网址为默认网址
        customService.sendWeixinPush(report.getProposerNumber(), "第" + report.getCount() + "次思想汇报初审结果", content.toString(), "http://wechat.party.gxun.club/", "info");
        //发送短信提醒
        customService.sendNote(report.getProposerNumber(), "第" + report.getCount() + "次思想汇报初审结果", content.toString(), 0, token);


        return R.ok("审核成功");


    }


    //审核纸质版照片
    @PutMapping("/report/recheckApproval")
    @ApiOperation(value = "审核思想汇报:复审")
    public Object recheckApprovalReport(@RequestBody ReportApproval object,
                                        @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //获取登录者的学号
        List<SysUser> sysUsers = sysUserService.findByIds(userId);
        if (sysUsers == null || sysUsers.size() == 0) {
            return R.error("请先完善你的学号");
        }

        SysUser sysUser = sysUsers.get(0);
        String approverNumber = sysUser.getNumber();

        //修改思想汇报表（状态、结果、意见、修改人、修改时间）
        List<Report> reportList = reportService.findByIds(object.getId());
        if (reportList == null || reportList.size() <= 0)
            return R.error("审核失败：没有找到思想汇报记录");
        Report report = reportList.get(0);
        if (!checkUserDataAuth(token, report.getProposerNumber())) {
            return R.error("您没有权限审核" + report.getProposerNumber() + "的记录");
        }

        List<Approval> approvalList = approvalService.query(W.f(
                W.and("stage", "eq", "2"),//stage: 2-复审
                W.and("approvalId", "eq", object.getId())
        ));

        if (approvalList == null || approvalList.size() <= 0)
            return R.error("审核失败：没有找到审核记录");

        Approval approval = approvalList.get(0);
        approval.setApproverNumber(approverNumber);
        approval.setStatus(2);
        if (object.getResult() == 2) {
            approval.setIsConfirm(1);
        }
        approval.setResult(object.getResult());
        approval.setOpinion(object.getOpinion());
        approval.setReviser(userId);

        approval = approvalService.updateApproval(approval);
        if (approval == null) {
            return R.error("审核失败：思想汇报修改失败");
        } else {

            StringBuffer content = new StringBuffer();
            int result = object.getResult();

            content.append("#{name}同学您好，您的第" + report.getCount() + "次思想汇报复审结果为：");
            if (result == 1) {
                content.append("“不通过”，");
            } else if (result == 2) {
                content.append("“不通过”，");
            }
            content.append("审核人意见为：“" + RemoveHTML.Html2Text(object.getOpinion()) + "”。");
            Message message = new Message();
            message.setNumber(report.getProposerNumber());
            message.setTopic("第" + report.getCount() + "次思想汇报复审结果");
            message.setContent(content.toString());
            message.setType(5);
            message.setServer(pcServer);
            message.setLinkId(report.getId());
            //发送消息提醒
            customService.sendMessage(message, token);
            //fixme:发送微信推送 网址为默认网址
            customService.sendWeixinPush(report.getProposerNumber(), "第" + report.getCount() + "次思想汇报复审结果", content.toString(), "http://wechat.party.gxun.club/", "info");
            //发送短信提醒
            customService.sendNote(report.getProposerNumber(), "第" + report.getCount() + "次思想汇报复审结果", content.toString(), 0, token);


            return R.ok("审核成功");
        }

    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("content", "请输入内容");
        verify.put("status", "请选择审核状态");
//        verify.put("opinion", "审核意见");
//        verify.put("result", "审核结果");
        verify.put("proposerNumber", "请输入用户学号/工号");
        verify.put("lastContent", "上版本内容");
//        verify.put("verifierNumber", "审核人学号/工号");
        verify.put("type", "请选择类型");
        verify.put("referTime", "请选择提交时间");
        verify.put("stage", "请选择阶段");
        verify.put("count", "请输入次数");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("content", "请输入内容");
        verify.put("status", "请选择审核状态");
//        verify.put("opinion", "审核意见");
//        verify.put("result", "审核结果");
        verify.put("proposerNumber", "请输入用户学号/工号");
        verify.put("lastContent", "上版本内容");
//        verify.put("verifierNumber", "审核人学号/工号");
        verify.put("type", "请选择类型");
        verify.put("referTime", "请选择提交时间");
        verify.put("stage", "请输入阶段");
        verify.put("count", "次数");
        return verify;
    }

}
