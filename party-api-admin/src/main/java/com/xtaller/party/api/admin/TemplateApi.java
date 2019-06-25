package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Template;
import com.xtaller.party.core.service.impl.TemplateService;
import com.xtaller.party.doc.TemplateCreate;
import com.xtaller.party.doc.TemplateUpdate;
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
 * Created by party on 2018/08/10
 */
@Api(value = "09_模版管理")
@RestController
@RequestMapping("/v1/base")
public class TemplateApi extends BaseApi {
    @Autowired
    private TemplateService templateService;

    @PostMapping("/template")
    @ApiOperation(value = "模版管理新增")
    public Object createTemplate(@RequestBody TemplateCreate object,
                                 @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Template model = o2c(object, token, Template.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        Boolean exist = templateService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");


        Boolean versionexist = templateService.exist(W.f(
                W.and("type", "eq", model.getType()),
                W.and("versionNumber", "eq", model.getVersionNumber()),
                W.and("isDel", "eq", "0"))
        );
        if (versionexist)
            return R.error("版本号已经存在请更换一个版本号");

        model = templateService.createTemplate(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/template")
    @ApiOperation(value = "修改模版管理")
    public Object updateTemplate(@RequestBody TemplateUpdate object,
                                 @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Template model = o2c(object, token, Template.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));


        Template data = templateService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getCode().equals(model.getCode())) {
            Boolean exist = templateService.exist(W.f(
                    W.and("code", "eq", model.getCode()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("代码已经存在请更换一个代码");
        }

        if (!data.getType().equals(model.getType()) || !data.getVersionNumber().equals(model.getVersionNumber())) {

            Boolean versionexist = templateService.exist(W.f(
                    W.and("type", "eq", model.getType()),
                    W.and("versionNumber", "eq", model.getVersionNumber()),
                    W.and("isDel", "eq", "0"))
            );
            if (versionexist)
                return R.error("版本号已经存在请更换一个版本号");

        }

        model.setReviser(userId);
        model = templateService.updateTemplate(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/template/{id}")
    @ApiOperation(value = "模版管理删除")
    public Object deleteTemplate(@PathVariable("id") String id,
                                 @RequestHeader("token") String token) {

        if (!templateService.existId(id))
            return R.error("Id数据异常");

        if (templateService.delete(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/template/{index}-{size}-{key}")
    @ApiOperation(value = "读取模版管理分页列表")
    public Object getTemplate(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("key") String key,
                              @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))

            wKey = S.apppend(" and code = '", key, "' ");
        return R.ok(templateService.page(index, size, wKey));

    }

    @GetMapping("/template")
    @ApiOperation(value = "读取模版管理所有列表")
    public Object getAllTemplate(@RequestHeader("token") String token) {

        return R.ok(templateService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("code", "请输入代码");
        verify.put("detail", "请输入模板内容");
        verify.put("type", "请选择模板类型");
        verify.put("versionNumber", "请输入版本号");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("code", "请输入代码");
        verify.put("detail", "请输入模板内容");
        verify.put("type", "请选择模板类型");
        verify.put("versionNumber", "请输入版本号");
        return verify;
    }

}
