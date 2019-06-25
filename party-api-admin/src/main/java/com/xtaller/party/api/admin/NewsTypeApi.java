package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.ArticleType;
import com.xtaller.party.core.service.impl.ArticleTypeService;
import com.xtaller.party.doc.ArticleTypeCreate;
import com.xtaller.party.doc.ArticleTypeUpdate;
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
 * Created by party on 2018/12/08
 */
@Api(value = "29_文章类型管理")
@RestController
@RequestMapping("/v1/base")
public class NewsTypeApi extends BaseApi {
    @Autowired
    private ArticleTypeService articleTypeService;

    @PostMapping("/articleType")
    @ApiOperation(value = "文章类型新增")
    public Object createArticleType(@RequestBody ArticleTypeCreate object,
                                    @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        ArticleType model = o2c(object, token, ArticleType.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = articleTypeService.exist(W.f(
                W.and("code", "eq", model.getTypeCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该代码已经存在请更换一个代码");
        Boolean nameexist = articleTypeService.exist(W.f(
                W.and("name", "eq", model.getTypeName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("该名称已经存在请更换一个名称");

        model = articleTypeService.createArticleType(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/articleType")
    @ApiOperation(value = "修改文章类型")
    public Object updateArticleType(@RequestBody ArticleTypeUpdate object,
                                    @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        ArticleType model = o2c(object, token, ArticleType.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        model.setReviser(userId);
        model = articleTypeService.updateArticleType(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/articleType/{id}")
    @ApiOperation(value = "文章类型删除")
    public Object deleteArticleType(@PathVariable("id") String id,
                                    @RequestHeader("token") String token) {

        if (!articleTypeService.existId(id))
            return R.error("Id数据异常");

        if (articleTypeService.deleteArticleType(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/articleType/{index}-{size}-{key}")
    @ApiOperation(value = "读取文章类型分页列表")
    public Object getArticleType(@PathVariable("index") int index,
                                 @PathVariable("size") int size,
                                 @PathVariable("key") String key,
                                 @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(articleTypeService.page(index, size, wKey));

    }

    @GetMapping("/articleType")
    @ApiOperation(value = "读取文章类型所有列表")
    public Object getAllArticleType(@RequestHeader("token") String token) {

        return R.ok(articleTypeService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("typeCode", "请输入文章类型代码");
        verify.put("typeName", "请输入文章类型名称");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("typeCode", "请输入文章类型代码");
        verify.put("typeName", "请输入文章类型名称");
        return verify;
    }

}
