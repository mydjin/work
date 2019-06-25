package com.qihsoft.webdev.api.admin;

import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.core.service.impl.SysOperationRecordService;
import com.qihsoft.webdev.doc.SysOperationRecordSelect;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qihsoft on 2018/12/08
 */
@Api(value = "107_系统操作记录管理")
@RestController
@RequestMapping("/v1/base")
public class SysOperationRecordApi extends BaseApi {
    @Autowired
    private SysOperationRecordService sysOperationRecordService;

    @PostMapping("/sysOperationRecord/{index}-{size}")
    @ApiOperation(value = "读取系统操作记录分页列表")
    public Object getSysOperationRecord(
            @RequestBody SysOperationRecordSelect object,
            @PathVariable("index") int index,
            @PathVariable("size") int size,
            @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(object.getNumber()))
            wKey += S.apppend(" and number like '%", object.getNumber(), "%' ");
        if (!V.isEmpty(object.getServer()))
            wKey += S.apppend(" and server = '", object.getServer(), "' ");
        if (!V.isEmpty(object.getControl()))
            wKey += S.apppend(" and control like '%", object.getControl(), "%' ");
        if (!V.isEmpty(object.getStatus()))
            wKey += S.apppend(" and status like '%", object.getStatus(), "%' ");
        if (!V.isEmpty(object.getFunction()))
            wKey += S.apppend(" and function like '%", object.getFunction(), "%' ");
        if (!V.isEmpty(object.getIpAddr()))
            wKey += S.apppend(" and ipAddr  = '", object.getIpAddr(), "' ");

        return R.ok(sysOperationRecordService.page(index, size, wKey));

    }


    @PostMapping("/sysOperationRecord/user/{index}-{size}")
    @ApiOperation(value = "读取系统操作记录分页列表")
    public Object getUserSysOperationRecord(
            @RequestBody SysOperationRecordSelect object,
            @PathVariable("index") int index,
            @PathVariable("size") int size,
            @RequestHeader("token") String token) {
        String number = getNumberByCache(token);
        if (number == null || number == "") {
            return R.error("用户登录状态有误");
        }

        String wKey = S.apppend(" and number = '", number, "' ");

        if (!V.isEmpty(object.getServer()))
            wKey += S.apppend(" and server = '", object.getServer(), "' ");
        if (!V.isEmpty(object.getControl()))
            wKey += S.apppend(" and control like '%", object.getControl(), "%' ");
        if (!V.isEmpty(object.getStatus()))
            wKey += S.apppend(" and status like '%", object.getStatus(), "%' ");
        if (!V.isEmpty(object.getFunction()))
            wKey += S.apppend(" and function like '%", object.getFunction(), "%' ");
        if (!V.isEmpty(object.getIpAddr()))
            wKey += S.apppend(" and ipAddr  = '", object.getIpAddr(), "' ");

        return R.ok(sysOperationRecordService.page(index, size, wKey));

    }


}
