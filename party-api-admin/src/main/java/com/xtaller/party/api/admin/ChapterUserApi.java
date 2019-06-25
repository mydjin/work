package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.ChapterUser;
import com.xtaller.party.core.service.impl.ChapterUserService;
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
@Api(value = "27_章目录与用户关系管理")
@RestController
@RequestMapping("/v1/base")
public class ChapterUserApi extends BaseApi {
    @Autowired
    private ChapterUserService chapterUserService;

    @GetMapping("/chapterUser/{index}-{size}-{key}")
    @ApiOperation(value = "读取章目录与用户关系分页列表")
    public Object getChapterUser(@PathVariable("index") int index,
                                 @PathVariable("size") int size,
                                 @PathVariable("key") String key,
                                 @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(chapterUserService.page(index, size, wKey));

    }

    @GetMapping("/chapterUser")
    @ApiOperation(value = "读取章目录与用户关系所有列表")
    public Object getAllChapterUser(@RequestHeader("token") String token) {

        return R.ok(chapterUserService.queryAll(""));

    }

    @GetMapping("/chapterUser/Note/{index}-{size}-{key}")
    @ApiOperation(value = "读取章目录与用户关系分页列表")
    public Object getChapterUser_admin(@PathVariable("index") int index,
                                       @PathVariable("size") int size,
                                       @PathVariable("key") String key,
                                       @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and chapterCode = '", key, "' ");
        return R.ok(chapterUserService.page_admin(index, size, wKey));

    }

    @GetMapping("/chapterUser/AllNote/{key}")
    @ApiOperation(value = "读取章目录与用户关系所有列表")
    public Object getAllChapterUser_admin(@PathVariable("key") String key, @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and chapterCode = '", key, "' ");
        return R.ok(chapterUserService.queryAll_admin(wKey));

    }


}
