package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Chapter;
import com.xtaller.party.core.model.Section;
import com.xtaller.party.core.service.impl.SectionService;
import com.xtaller.party.doc.SectionCreate;
import com.xtaller.party.doc.SectionUpdate;
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
 * Created by party on 2018/08/29
 */
@Api(value = "19_网络课程节内容管理")
@RestController
@RequestMapping("/v1/base")
public class SectionApi extends BaseApi {
    @Autowired
    private SectionService sectionService;

    @PostMapping("/section")
    @ApiOperation(value = "网络课程节内容新增")
    public Object createSection(@RequestBody SectionCreate object,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Section model = o2c(object, token, Section.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = sectionService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");

        Boolean nameexist = sectionService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("名称已经存在请更换一个名称");

        model = sectionService.createSection(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/section")
    @ApiOperation(value = "修改网络课程节内容")
    public Object updateSection(@RequestBody SectionUpdate object,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Section model = o2c(object, token, Section.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        model.setReviser(userId);
        model = sectionService.updateSection(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/section/{id}")
    @ApiOperation(value = "网络课程节内容删除")
    public Object deleteSection(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {

        if (!sectionService.existId(id))
            return R.error("Id数据异常");

        if (sectionService.deleteSection(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/section/{index}-{size}-{key}")
    @ApiOperation(value = "读取网络课程节内容分页列表")
    public Object getSection(@PathVariable("index") int index,
                             @PathVariable("size") int size,
                             @PathVariable("key") String key,
                             @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(sectionService.page(index, size, wKey));

    }

    @GetMapping("/section")
    @ApiOperation(value = "读取网络课程节内容所有列表")
    public Object getAllSection(@RequestHeader("token") String token) {

        return R.ok(sectionService.query(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("chapterId", "请输入章目录id");
        verify.put("name", "请输入节名称");
//        verify.put("code", "请输入节代码");
//        verify.put("sort", "请输入排序");
        verify.put("type", "请输入类型");
//        verify.put("content", "请输入内容");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("chapterId", "请输入章目录id");
        verify.put("name", "请输入节名称");
//        verify.put("code", "请输入节代码");
//        verify.put("sort", "请输入排序");
        verify.put("type", "请输入类型");
//        verify.put("content", "请输入内容");
        return verify;
    }

    @GetMapping("/section/getById/{sectionId}")
    @ApiOperation(value = "读取章信息")
    public Object getSectionById(@PathVariable("sectionId") String sectionId,
                                 @RequestHeader("token") String token) {

        JSONObject json = sectionService.queryById_url(sectionId);

        return R.ok(json);

    }


}
