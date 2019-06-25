package com.qihsoft.webdev.api.unified;

import com.qihsoft.webdev.doc.VisitorAdd;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author Qihsoft
 * @create 2017-12-05 15:10
 */
@Api(value = "访客")
@RestController
@RequestMapping("/v1/unified")
@CrossOrigin   //跨域服务注解
public class visitorApi {


    @PostMapping("/passlog/blackList")
    @ApiOperation(value = "黑名单查询")
    public void blackList(@RequestBody VisitorAdd partyAdd) {
        System.out.println("收到值：" + partyAdd.getCardNo());
    }
}
