package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.SectionUser;
import com.xtaller.party.core.service.impl.SectionUserService;
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
 * Created by party on 2018/11/05
 */
@Api(value = "16_节内容与用户关系管理")
@RestController
@RequestMapping("/v1/base")
public class SectionUserApi extends BaseApi {
    @Autowired
    private SectionUserService sectionUserService;


    @GetMapping("/sectionUser/{index}-{size}-{key}")
    @ApiOperation(value = "读取节内容与用户关系分页列表")
    public Object getSectionUser(@PathVariable("index") int index,
                                 @PathVariable("size") int size,
                                 @PathVariable("key") String key,
                                 @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(sectionUserService.page(index, size, wKey));

    }

    @GetMapping("/sectionUser/")
    @ApiOperation(value = "读取节内容与用户关系所有列表")
    public Object getAllSectionUser(@RequestHeader("token") String token) {

        return R.ok(sectionUserService.queryAll(""));

    }


    @GetMapping("/sectionUser/Note/{index}-{size}-{key}")
    @ApiOperation(value = "读取节内容与用户关系分页列表")
    public Object getSectionUser_admin(@PathVariable("index") int index,
                                       @PathVariable("size") int size,
                                       @PathVariable("key") String key,
                                       @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and sectionCode = '", key, "' ");
        return R.ok(sectionUserService.page_admin(index, size, wKey));

    }

    @GetMapping("/sectionUser/AllNote/{key}")
    @ApiOperation(value = "读取节内容与用户关系所有列表")
    public Object getAllSectionUser_admin(@PathVariable("key") String key, @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and sectionCode = '", key, "' ");
        return R.ok(sectionUserService.queryAll_admin(wKey));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入学生学号");
        verify.put("sectionCode", "请输入节内容代码");
        verify.put("testScore", "请输入测试分值");
        verify.put("isFinish", "请输入是否完成");
        verify.put("learnPercent", "请输入完成百分比");
        verify.put("note", "请输入进度存储");
        verify.put("lastLearnTime", "请输入上次学习时间");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("number", "请输入学生学号");
        verify.put("sectionCode", "请输入节内容代码");
        verify.put("testScore", "请输入测试分值");
        verify.put("isFinish", "请输入是否完成");
        verify.put("learnPercent", "请输入完成百分比");
        verify.put("note", "请输入进度存储");
        verify.put("lastLearnTime", "请输入上次学习时间");
        return verify;
    }

}
