package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.OnlineCourseUser;
import com.xtaller.party.core.service.impl.OnlineCourseUserService;
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
 * Created by party on 2018/11/06
 */
@Api(value = "25_网络课程与用户关系管理")
@RestController
@RequestMapping("/v1/base")
public class OnlineCourseUserApi extends BaseApi {
    @Autowired
    private OnlineCourseUserService onlineCourseUserService;


    @GetMapping("/onlineCourseUser/{index}-{size}-{key}")
    @ApiOperation(value = "读取网络课程与用户关系分页列表")
    public Object getOnlineCourseUser(@PathVariable("index") int index,
                                      @PathVariable("size") int size,
                                      @PathVariable("key") String key,
                                      @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(onlineCourseUserService.page(index, size, wKey));

    }

    @GetMapping("/onlineCourseUser")
    @ApiOperation(value = "读取网络课程与用户关系所有列表")
    public Object getAllOnlineCourseUser(@RequestHeader("token") String token) {

        return R.ok(onlineCourseUserService.queryAll(""));

    }


    @GetMapping("/onlineCourseUser/Note/{index}-{size}-{key}")
    @ApiOperation(value = "读取网络课程与用户关系分页列表")
    public Object getOnlineCourseUser_admin(@PathVariable("index") int index,
                                            @PathVariable("size") int size,
                                            @PathVariable("key") String key,
                                            @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and courseId = '", key, "' ");
        return R.ok(onlineCourseUserService.page_admin(index, size, wKey));

    }

    @GetMapping("/onlineCourseUser/AllNote/{key}")
    @ApiOperation(value = "读取网络课程与用户关系所有列表")
    public Object getAllOnlineCourseUser_admin(@PathVariable("key") String key, @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and courseId = '", key, "' ");

        return R.ok(onlineCourseUserService.queryAll_admin(wKey));

    }


}
