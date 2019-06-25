package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.ApplicationApproval;
import com.xtaller.party.doc.ApplicationCreate;
import com.xtaller.party.doc.ApplicationUpdate;
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
 * Created by party on 2018/08/13
 */
@Api(value = "11_入党申请书管理")
@RestController
@RequestMapping("/v1/base")
public class ApplicationApi extends BaseApi {
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/application/info/{id}-{stage}")
    @ApiOperation(value = "入党申请书信息")
    public Object getApplicationById(@PathVariable("id") String id,
                                     @PathVariable("stage") int stage,
                                     @RequestHeader("token") String token) {

        JSONObject back = applicationService.queryById(id, stage);

        return R.ok("操作成功", back);

    }

    @GetMapping("/application/{index}-{size}-{number}-{stage}")
    @ApiOperation(value = "读取入党申请书分页列表")
    public Object getApplication(@PathVariable("index") int index,
                                 @PathVariable("size") int size,
                                 @PathVariable("number") String number,
                                 @PathVariable("stage") String stage,
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
            if (!V.isEmpty(number))
                wKey += S.apppend(" and number like '%", number, "%' ");
            if (!V.isEmpty(stage))
                wKey += S.apppend(" and stage = ", stage, " ");
            back = applicationService.personPage(index, size, wKey);
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
            if (!V.isEmpty(stage))
                wKey += S.apppend(" and stage = ", stage, " ");
            back = applicationService.personPage(index, size, wKey);
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
            if (!V.isEmpty(stage))
                wKey += S.apppend(" and stage = ", stage, " ");
            back = applicationService.personPage(index, size, wKey);
        } else {
            back = new Page();
        }
        return R.ok(back);

    }


    @PutMapping("/application/firstApproval")
    @ApiOperation(value = "审核入党申请书:初审")
    public Object firstApprovalApplication(@RequestBody ApplicationApproval object,
                                           @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //获取登录者的学号
        List<SysUser> sysUsers = sysUserService.findByIds(userId);
        if (sysUsers == null || sysUsers.size() == 0) {
            return R.error("请先完善你的学号");
        }

        SysUser sysUser = sysUsers.get(0);
        String approverNumber = sysUser.getNumber();


        //修改审核表（状态、结果、意见、修改人、修改时间）
        List<Approval> approvalList = approvalService.query(W.f(
                W.and("stage", "eq", "1"),//stage: 1-初审
                W.and("id", "eq", object.getId())
        ));

        if (approvalList == null || approvalList.size() <= 0)
            return R.error("审核失败：没有找到审核记录");

        Approval approval = approvalList.get(0);
        if (!checkUserDataAuth(token, approval.getNumber())) {
            return R.error("您没有权限审核" + approval.getNumber() + "的记录");
        }


        approval.setApproverNumber(approverNumber);
        approval.setStatus(2);
        approval.setResult(object.getResult());
        approval.setOpinion(object.getOpinion());
        approval.setReviser(userId);

        approval = approvalService.updateApproval(approval);
        if (approval == null)
            return R.error("审核失败：申请书记录修改失败");

        //初审通过，向审批表插入一条复审记录
        if (object.getResult() == 1) {//1-同意
            Approval recheckApproval = new Approval();
            recheckApproval.setStage(2);//2-复审
            recheckApproval.setStatus(1);//1-未审
            recheckApproval.setIsConfirm(1);//1-未提交审核，审核人不可见
            recheckApproval.setApprovalId(approval.getApprovalId());
            recheckApproval.setApprovalType(1);
            recheckApproval.setNumber(approval.getNumber());
            recheckApproval.setApproverNumber(approval.getApproverNumber());
            approvalService.createApproval(recheckApproval);
            if (recheckApproval == null) {
                return R.error("新增到复审失败");
            }

        }


        StringBuffer content = new StringBuffer();
        int result = object.getResult();

        content.append("#{name}同学您好，");
        if (result == 1) {
            content.append("您的入党申请书初审结果为：“通过”，");
        } else if (result == 2) {
            content.append("您的入党申请书初审结果为：“不通过”，");
        }
        content.append("审核人意见为：“" + RemoveHTML.Html2Text(object.getOpinion()) + "”。");
        Message message = new Message();
        message.setNumber(approval.getNumber());
        message.setTopic("入党申请书初审结果");
        message.setContent(content.toString());
        message.setType(4);
        message.setServer(pcServer);
        message.setLinkId(approval.getApprovalId());
        //发送消息提醒
        customService.sendMessage(message, token);
        //fixme:发送微信推送 网址暂时默认
        customService.sendWeixinPush(approval.getNumber(), "入党申请书初审结果", content.toString(), "http://wechat.party.gxun.club/", "info");
        //发送短信提醒
        customService.sendNote(approval.getNumber(), "入党申请书初审结果", content.toString(), 0, token);


        return R.ok("审核成功");

    }


    //审核纸质版照片
    @PutMapping("/application/recheckApproval")
    @ApiOperation(value = "审核入党申请书:复审")
    public Object recheckApprovalApplication(@RequestBody ApplicationApproval object,
                                             @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //获取登录者的学号
        List<SysUser> sysUsers = sysUserService.findByIds(userId);
        if (sysUsers == null || sysUsers.size() == 0) {
            return R.error("请先完善你的学号");
        }

        SysUser sysUser = sysUsers.get(0);
        String approverNumber = sysUser.getNumber();


        List<Approval> approvalList = approvalService.query(W.f(
                W.and("stage", "eq", "2"),//stage: 2-复审
                W.and("id", "eq", object.getId())
        ));

        if (approvalList == null || approvalList.size() <= 0)
            return R.error("审核失败：没有找到审核记录");

        Approval approval = approvalList.get(0);

        if (!checkUserDataAuth(token, approval.getNumber())) {
            return R.error("您没有权限审核" + approval.getNumber() + "的记录");
        }


        approval.setApproverNumber(approverNumber);
        approval.setStatus(2);
        if (object.getResult() == 2) {//复审不通过的话设置isConfirm为1-未提交审核，且审核人不可见该复审记录
            approval.setIsConfirm(1);
        }
        approval.setResult(object.getResult());
        approval.setOpinion(object.getOpinion());
        approval.setReviser(userId);

        approval = approvalService.updateApproval(approval);
        if (approval == null) {
            return R.error("审核失败：申请书记录修改失败");
        } else {

            StringBuffer content = new StringBuffer();
            int result = object.getResult();

            content.append("#{name}同学您好，");
            if (result == 1) {
                content.append("您的入党申请书复审结果为：“通过”，");
            } else if (result == 2) {
                content.append("您的入党申请书复审结果为：“不通过”，");
            }
            content.append("审核人意见为：“" + RemoveHTML.Html2Text(object.getOpinion()) + "”。");
            Message message = new Message();
            message.setNumber(approval.getNumber());
            message.setTopic("入党申请书复审结果");
            message.setContent(content.toString());
            message.setType(4);
            message.setServer(pcServer);
            message.setLinkId(approval.getApprovalId());
            //发送消息提醒
            customService.sendMessage(message, token);
            //fixme:发送微信推送 网址暂时默认
            customService.sendWeixinPush(approval.getNumber(), "入党申请书复审结果", content.toString(), "http://wechat.party.gxun.club/", "info");
            //发送短信通知
            customService.sendNote(approval.getNumber(), "入党申请书复审结果", content.toString(), 0, token);


            return R.ok("审核成功");
        }

    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("fileURL", "文件URL");
        verify.put("status", "请选择审核状态");
        verify.put("opinion", "请输入审核意见");
        verify.put("result", "请输入审核结果");
        verify.put("proposerNumber", "请输入用户学号/工号");
//        verify.put("lastContent", "上版本文件URL");
        verify.put("verifierNumber", "请输入审核人学号/工号");
        verify.put("referTime", "请选择提交时间");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("fileURL", "文件URL");
        verify.put("status", "请选择审核状态");
        verify.put("opinion", "请输入审核意见");
        verify.put("result", "请输入审核结果");
        verify.put("proposerNumber", "请输入用户学号/工号");
//        verify.put("lastContent", "上版本文件URL");
        verify.put("verifierNumber", "请输入审核人学号/工号");
        verify.put("referTime", "请选择提交时间");
        return verify;
    }

}
