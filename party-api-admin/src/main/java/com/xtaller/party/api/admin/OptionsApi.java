package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Options;
import com.xtaller.party.core.service.impl.OptionsService;
import com.xtaller.party.doc.OptionsCreate;
import com.xtaller.party.doc.OptionsUpdate;
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
 * Created by party on 2018/08/23
 */
@Api(value = "33_选项管理")
@RestController
@RequestMapping("/v1/base")
public class OptionsApi extends BaseApi {
    @Autowired
    private OptionsService optionsService;

    @PostMapping("/options")
    @ApiOperation(value = "选项新增")
    public Object createOptions(@RequestBody OptionsCreate object,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Options model = o2c(object, token, Options.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = optionsService.exist(W.f(
                W.and("questionId", "eq", object.getQuestionId()),
                W.and("optionNumber", "eq", model.getOptionNumber()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("此选项已存在");

        model = optionsService.createOptions(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/options")
    @ApiOperation(value = "修改选项")
    public Object updateOptions(@RequestBody OptionsUpdate object,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Options model = o2c(object, token, Options.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        model.setReviser(userId);
        model = optionsService.updateOptions(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/options/{id}")
    @ApiOperation(value = "选项删除")
    public Object deleteOptions(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {

        if (!optionsService.existId(id))
            return R.error("Id数据异常");

        if (optionsService.deleteOptions(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/options/{index}-{size}-{key}")
    @ApiOperation(value = "读取选项分页列表")
    public Object getOptions(@PathVariable("index") int index,
                             @PathVariable("size") int size,
                             @PathVariable("key") String key,
                             @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(optionsService.page(index, size, wKey));

    }

    @GetMapping("/options")
    @ApiOperation(value = "读取选项所有列表")
    public Object getAllOptions(@RequestHeader("token") String token) {

        return R.ok(optionsService.queryAll(""));

    }


    @GetMapping("/options/questionId/{questionId}")
    @ApiOperation(value = "通过问题Id读取选项")
    public Object getByQuestionId(@PathVariable("questionId") String questionId,
                                  @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(questionId))
            wKey = S.apppend(" and questionId = '", questionId, "' ");
        return R.ok(optionsService.queryByQuestionId(wKey));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("questionId", "请输入题目id");
        verify.put("optionInfo", "请输入选项内容");
        verify.put("optionNumber", "请输入选项号");
        verify.put("isAnswer", "请输入是否正确选项");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("questionId", "请输入题目id");
        verify.put("optionInfo", "请输入选项内容");
        verify.put("optionNumber", "请输入选项号");
        verify.put("isAnswer", "请输入是否正确选项");
        return verify;
    }

}
