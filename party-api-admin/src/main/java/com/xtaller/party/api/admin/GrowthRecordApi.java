package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.GrowthRecord;
import com.xtaller.party.core.service.impl.GrowthRecordService;
import com.xtaller.party.doc.GrowthRecordCreate;
import com.xtaller.party.doc.GrowthRecordUpdate;
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
 * Created by party on 2018/08/20
 */
@Api(value = "10_成长记录管理")
@RestController
@RequestMapping("/v1/base")
public class GrowthRecordApi extends BaseApi {
    @Autowired
    private GrowthRecordService growthRecordService;

    @PostMapping("/growthRecord")
    @ApiOperation(value = "成长记录新增")
    public Object createGrowthRecord(@RequestBody GrowthRecordCreate object,
                                     @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        GrowthRecord model = o2c(object, token, GrowthRecord.class);

        if (!checkUserDataAuth(token, model.getNumber())) {
            return R.error("您没有权限新增" + model.getNumber() + "的记录");
        }
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = growthRecordService.exist(W.f(
                W.and("number", "eq", model.getNumber()),
                W.and("type", "eq", model.getType()),
                W.and("stage", "eq", model.getStage()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("当前阶段该类型记录已存在！");


        model = growthRecordService.createGrowthRecord(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/growthRecord")
    @ApiOperation(value = "修改成长记录")
    public Object updateGrowthRecord(@RequestBody GrowthRecordUpdate object,
                                     @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        GrowthRecord model = o2c(object, token, GrowthRecord.class);


        if (!checkUserDataAuth(token, model.getNumber())) {
            return R.error("您没有权限修改" + model.getNumber() + "的记录");
        }

        GrowthRecord data = growthRecordService.selectById(model.getId());
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
        model = growthRecordService.updateGrowthRecord(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }


    @GetMapping("/growthRecord/{index}-{size}-{number}")
    @ApiOperation(value = "读取成长记录分页列表")
    public Object getGrowthRecord(@PathVariable("index") int index,
                                  @PathVariable("size") int size,
                                  @PathVariable("number") String number,
                                  @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(number))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and number like '%", number, "%' ");


        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }
        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);

        if (visible == 1) {//1-全部（组织部）

            return R.ok(growthRecordService.page(index, size, wKey));


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
            //获取该学院代码下所有的学生的成长记录
            wKey += S.apppend(" and number in (", numbers, ") ");


            return R.ok(growthRecordService.page(index, size, wKey));

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

            return R.ok(growthRecordService.page(index, size, wKey));
        } else {
            return R.error("您没有权限查看");
        }


    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("recordTime", "请选择记录时间");
        verify.put("record", "请输入记录内容");
        verify.put("type", "请选择类型");
        verify.put("stage", "请选择阶段");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("recordTime", "请选择记录时间");
        verify.put("record", "请输入记录内容");
        verify.put("type", "请选择类型");
        verify.put("stage", "请选择阶段");
        return verify;
    }

}
