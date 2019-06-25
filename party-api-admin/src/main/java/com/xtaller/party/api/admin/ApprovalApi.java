package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Approval;
import com.xtaller.party.core.service.impl.ApprovalService;
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
 * Created by party on 2018/08/14
 */
@Api(value = "32_待办事项/审核管理")
@RestController
@RequestMapping("/v1/base")
public class ApprovalApi extends BaseApi {
    @Autowired
    private ApprovalService approvalService;


    @GetMapping("/approval/todo/{index}-{size}-{approvalType}/")
    @ApiOperation(value = "通过类型读取待办事项分页列表")
    public Object getToDoApproval(@PathVariable("index") int index,
                                  @PathVariable("size") int size,
                                  @PathVariable("approvalType") String approvalType,
                                  @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(approvalType)) {
            wKey = S.apppend(" approvalType =", approvalType, " ");
        } else {
            wKey = S.apppend("  ");
        }
        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }

        if (visible == 1) {//1-全部（组织部）
            return R.ok(approvalService.todoPage(index, size, wKey));
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
            //获取该学院代码下所有的学生的申请内容
            wKey += S.apppend(" and number in (", numbers, ") ");
            return R.ok(approvalService.todoPage(index, size, wKey));
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
            return R.ok(approvalService.todoPage(index, size, wKey));
        } else {
            return R.error("您没有权限查看");
        }
    }

    @GetMapping("/approval/todo/index/")
    @ApiOperation(value = "通过类型读取待办事项分页列表_首页")
    public Object getToDoApprovalIndex(
            @RequestHeader("token") String token) {

        String wKey = "";
        int index = 1, size = 5;

        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }
        if (visible == 1) {//1-全部（组织部）
            return R.ok(approvalService.todoPage(index, size, wKey));

        } else if (visible == 2) {//2-院级
            String academyCode = getAcademyCodeByCache(token);
            if (V.isEmpty(academyCode)) {
                return R.error("请先完善您的学院信息");
            }
            String numbers = customService.queryUserNumbersByAcademyCode(academyCode);
            if (V.isEmpty(numbers)) {
                // return R.error("当前账号暂无可操作的用户信息");
                return R.ok(empty);
            } else {
                //获取该学院代码下所有的学生的申请内容
                wKey += S.apppend(" and number in (", numbers, ") ");
                return R.ok(approvalService.todoPage(index, size, wKey));
            }
        } else if (visible == 3) {//3-个人(班主任、联络员)
            //获取学号/工号
            String loginNumber = getLoginNumberCache(token);
            if (V.isEmpty(loginNumber)) {
                return R.error("请先完善你的学号/工号");
            }
            String numbers = customService.queryUserNumbersByLinkNumber(loginNumber);
            if (V.isEmpty(numbers)) {
                return R.ok(empty);
            } else {
                wKey += S.apppend(" and number in (", numbers, ") ");

                return R.ok(approvalService.todoPage(index, size, wKey));
            }

        } else {
            return R.error("您没有权限查看");
        }
    }


}
