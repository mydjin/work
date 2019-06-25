package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.AcademyInfo;
import com.xtaller.party.core.model.LinkUser;
import com.xtaller.party.core.service.impl.LinkUserService;
import com.xtaller.party.doc.LinkUserCreate;
import com.xtaller.party.doc.LinkUserUpdate;
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
 * Created by party on 2018/10/08
 */
@Api(value = "34_培养联系人管理")
@RestController
@RequestMapping("/v1/base")
public class LinkUserApi extends BaseApi {
    @Autowired
    private LinkUserService linkUserService;

    @PostMapping("/linkUser")
    @ApiOperation(value = "培养联系人新增")
    public Object createLinkUser(@RequestBody LinkUserCreate object,
                                 @RequestHeader("token") String token) {

        //映射对象
        LinkUser model = o2c(object, token, LinkUser.class);

        if (!checkUserDataAuth(token, model.getNumber())) {
            return R.error("您没有权限新增" + model.getNumber() + "的培养联系人");
        }

        if (!checkUserDataAuth(token, model.getLinkNumber())) {
            return R.error("您没有权限新增" + model.getLinkNumber() + "为" + model.getNumber() + "的培养联系人");
        }

        if (model.getNumber().equals(model.getLinkNumber())) {
            return R.error("培养联系人不能为本人");
        }

        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        Boolean exist = linkUserService.exist(W.f(
                W.and("number", "eq", model.getNumber()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该用户已添加入党联系人！");

        model = linkUserService.createLinkUser(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/linkUser")
    @ApiOperation(value = "修改培养联系人")
    public Object updateLinkUser(@RequestBody LinkUserUpdate object,
                                 @RequestHeader("token") String token) {
        String userId = getUserIdByCache(token);
        //映射对象
        LinkUser model = o2c(object, token, LinkUser.class);
        if (!checkUserDataAuth(token, model.getNumber())) {
            return R.error("您没有权限修改" + model.getNumber() + "的培养联系人");
        }

        if (!checkUserDataAuth(token, model.getLinkNumber())) {
            return R.error("您没有权限修改" + model.getLinkNumber() + "为" + model.getNumber() + "的培养联系人");
        }

        if (model.getNumber().equals(model.getLinkNumber())) {
            return R.error("培养联系人不能为本人");
        }

        LinkUser data = linkUserService.selectById(model.getId());
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
        model = linkUserService.updateLinkUser(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/linkUser/{id}")
    @ApiOperation(value = "培养联系人删除")
    public Object deleteLinkUser(@PathVariable("id") String id,
                                 @RequestHeader("token") String token) {
        LinkUser obj = linkUserService.selectById(id);

        if (V.isEmpty(obj))
            return R.error("Id数据异常");
        String number = obj.getNumber();

        if (!checkUserDataAuth(token, number)) {
            return R.error("您没有权限删除" + number + "的培养联系人");
        }


        if (linkUserService.deleteLinkUser(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/linkUser/{index}-{size}-{key}")
    @ApiOperation(value = "读取培养联系人分页列表")
    public Object getLinkUser(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("key") String key,
                              @RequestHeader("token") String token) {
        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");

        //获取数据可见性
        Integer visible = getVisibleByCache(token);
        if (V.isEmpty(visible)) {
            return R.error("您没有权限查看");
        }
        Page empty = new Page(new ArrayList<JSONObject>(), 0, 0, 0, 1);

        if (visible == 1) {//1-全部（组织部）

            return R.ok(linkUserService.page(index, size, wKey));


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
            //获取该学院代码下所有的用户信息
            wKey += S.apppend(" and number in (", numbers, ") ");


            return R.ok(linkUserService.page(index, size, wKey));

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

            return R.ok(linkUserService.page(index, size, wKey));
        } else {
            return R.error("您没有权限查看");
        }


    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("linkNumber", "请输入联系人学号/工号");
        verify.put("inDuty", "请输入联系人党内单位及职务");
        verify.put("outDuty", "请输入联系人党外单位及职务");
        verify.put("linkJoinTime", "请输入联系人入党时间");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("linkNumber", "请输入联系人学号/工号");
        verify.put("inDuty", "请输入联系人党内单位及职务");
        verify.put("outDuty", "请输入联系人党外单位及职务");
        verify.put("linkJoinTime", "请输入联系人入党时间");
        return verify;
    }

}
