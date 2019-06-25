package com.qihsoft.webdev.api.unified;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.exception.AbstractCosException;
import com.qcloud.cos.sign.Credentials;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.config.cos.CosConfig;
import com.qihsoft.webdev.core.service.impl.SysTpsConfigService;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Taller
 * @create 2017-12-08 13:24
 */
@Api(value = "cos对象存储")
@RestController
@RequestMapping("/v1/common")
@CrossOrigin   //跨域服务注解
public class CosApi extends BaseApi {

    @Autowired
    private CosConfig cosConfig;
    @Autowired
    private SysTpsConfigService tpsConfigService;

    private Map<String, String> baseVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("type", "签名类型不能为空");
        verify.put("appId", "appId不能为空");
        verify.put("bucket", "bucket不能为空");
        return verify;
    }

    @GetMapping("/cos")
    @ApiOperation(value = "获取配置")
    public Object getCos() {
        JSONObject config = tpsConfigService.getByCode("cos");
        JSONObject res = new JSONObject();
        res.put("bucket", config.getString("bucket"));
        if (config.getString("bucket").equals("qihsoft")) {
            res.put("appId", config.getString("appId"));
            res.put("region", config.getString("region"));
            return R.ok(res);
        } else {
            JSONObject syncConfig = tpsConfigService.syncByCode("cos");
            JSONObject syncRes = new JSONObject();
            syncRes.put("appId", syncConfig.getString("appId"));
            syncRes.put("bucket", syncConfig.getString("bucket"));
            syncRes.put("region", syncConfig.getString("region"));
            return R.ok(syncRes);
        }

    }


    @GetMapping("/cos/sign")
    @ApiOperation(value = "获取签名")
    public Object getCosSign() throws AbstractCosException {
        JSONObject cos = tpsConfigService.getByCode("cos");
        Credentials cred = new Credentials(cos.getLong("appId"),
                cos.getString("secretId"),
                cos.getString("secretKey"));
        Long expired = TimeKit.getTimestamp() + cos.getInteger("expired");
        String signStr = cosConfig.getPeriodEffectiveSign(cos.getString("bucket"), "", cred, expired);
        JSONObject model = new JSONObject();
        model.put("sign", signStr);
        return R.ok(model);
    }


}
