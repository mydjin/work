package com.qihsoft.webdev.api.admin;

import com.qihsoft.webdev.api.BaseApi;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(value = "104_系统相关配置")
@RestController
@RequestMapping("/v1/sys")
public class SysConfigApi extends BaseApi {

   /* @Autowired
    private SysGlobalConfigService globalConfigService;
    @GetMapping("/config/global")
    @ApiOperation(value = "读取配置")
    public Object getConfig(@RequestHeader("token") String token){

            return R.ok(globalConfigService.getServerSetting());

    }

    @PutMapping("/config/global")
    @ApiOperation(value = "配置基础信息")
    public Object setConfig(@RequestBody List<Config> models,
                            @RequestHeader("token") String token){

            List<SysGlobalConfig> configs = J.o2l(models, SysGlobalConfig.class);
            return R.ok(globalConfigService.config(configs));

    }*/


}
