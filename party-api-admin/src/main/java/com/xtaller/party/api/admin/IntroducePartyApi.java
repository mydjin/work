package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.IntroduceParty;
import com.xtaller.party.core.service.impl.IntroducePartyService;
import com.xtaller.party.doc.IntroducePartyCreate;
import com.xtaller.party.doc.IntroducePartyUpdate;
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
 * Created by party on 2018/10/08
 */
@Api(value = "35_入党介绍人管理")
@RestController
@RequestMapping("/v1/base")
public class IntroducePartyApi extends BaseApi {
    @Autowired
    private IntroducePartyService introducePartyService;

    @PostMapping("/introduceParty")
    @ApiOperation(value = "入党介绍人新增")
    public Object createIntroduceParty(@RequestBody IntroducePartyCreate object,
                                       @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        IntroduceParty model = o2c(object, token, IntroduceParty.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = introducePartyService.exist(W.f(
                W.and("number", "eq", model.getNumber()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该用户已有入党联系人！");

        model = introducePartyService.createIntroduceParty(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/introduceParty")
    @ApiOperation(value = "修改入党介绍人")
    public Object updateIntroduceParty(@RequestBody IntroducePartyUpdate object,
                                       @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        IntroduceParty model = o2c(object, token, IntroduceParty.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        model.setReviser(userId);
        model = introducePartyService.updateIntroduceParty(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/introduceParty/{id}")
    @ApiOperation(value = "入党介绍人删除")
    public Object deleteIntroduceParty(@PathVariable("id") String id,
                                       @RequestHeader("token") String token) {

        if (!introducePartyService.existId(id))
            return R.error("Id数据异常");

        if (introducePartyService.deleteIntroduceParty(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/introduceParty/{index}-{size}-{key}")
    @ApiOperation(value = "读取入党介绍人分页列表")
    public Object getIntroduceParty(@PathVariable("index") int index,
                                    @PathVariable("size") int size,
                                    @PathVariable("key") String key,
                                    @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(introducePartyService.page(index, size, wKey));

    }

    @GetMapping("/introduceParty")
    @ApiOperation(value = "读取入党介绍人所有列表")
    public Object getAllIntroduceParty(@RequestHeader("token") String token) {

        return R.ok(introducePartyService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("introduceNumber", "请输入介绍人学号/工号");
        verify.put("inDuty", "请输入介绍人党内单位及职务");
        verify.put("outDuty", "请输入介绍人党外单位及职务");
        verify.put("introduceJoinTime", "请输入介绍人入党时间");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入用户学号/工号");
        verify.put("introduceNumber", "请输入介绍人学号/工号");
        verify.put("inDuty", "请输入介绍人党内单位及职务");
        verify.put("outDuty", "请输入介绍人党外单位及职务");
        verify.put("introduceJoinTime", "请输入介绍人入党时间");
        return verify;
    }

}
