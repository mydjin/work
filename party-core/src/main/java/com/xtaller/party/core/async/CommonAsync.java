package com.xtaller.party.core.async;

import com.xtaller.party.core.cache.CacheConstant;
import com.xtaller.party.core.service.impl.SysUserResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Taller on 2017/10/11
 * 公共异步
 */
@Component
public class CommonAsync {
    @Autowired
    private SysUserResourcesService userResourcesService;
    @Autowired
    private CacheConstant cacheConstant;
    @Async
    public void initUserResources(String systemId,String userId){
        userResourcesService.getUserResources(systemId,userId);
    }


}
