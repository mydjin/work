package com.qihsoft.webdev.api.admin;

import com.qihsoft.webdev.api.BaseApi;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(value = "104_系统相关配置")
@RestController
@RequestMapping("/v1/sys")
public class SysTpsConfigApi extends BaseApi {

   /* @Autowired
    private SysTpsConfigService tpsConfigService;

    @GetMapping("/config/tps/{code}")
    @ApiOperation(value = "读取第三方配置")
    public Object getConfig(@PathVariable("code") String code,@RequestHeader("token") String token){

            return R.ok(tpsConfigService.getSettingByCode(code));

    }

    @PutMapping("/config/tps/{code}")
    @ApiOperation(value = "配置第三方信息")
    public Object setConfig(@RequestBody List<TpsConfig> models,
                            @PathVariable("code") String code,
                            @RequestHeader("token") String token){

            List<SysTpsConfig> configs = J.o2l(models, SysTpsConfig.class);

            return R.ok(tpsConfigService.config(code,configs));

    }

*/


}
