package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.SysOperationRecord;
import com.xtaller.party.core.service.impl.SysOperationRecordService;
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
@Api(value = "SYS_系统操作记录管理")
@RestController
@RequestMapping("/v1/base")
public class SysOperationRecordApi extends BaseApi {
    @Autowired
    private SysOperationRecordService sysOperationRecordService;


    @GetMapping("/sysOperationRecord/{index}-{size}-{key}")
    @ApiOperation(value = "读取系统操作记录分页列表")
    public Object getSysOperationRecord(@PathVariable("index") int index,
                                        @PathVariable("size") int size,
                                        @PathVariable("key") String key,
                                        @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(sysOperationRecordService.page(index, size, wKey));

    }

    @GetMapping("/sysOperationRecord")
    @ApiOperation(value = "读取系统操作记录所有列表")
    public Object getAllSysOperationRecord(@RequestHeader("token") String token) {

        return R.ok(sysOperationRecordService.queryAll(""));

    }


}
