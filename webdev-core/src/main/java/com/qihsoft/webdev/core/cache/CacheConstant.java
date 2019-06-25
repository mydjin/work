package com.qihsoft.webdev.core.cache;

import com.qihsoft.webdev.core.service.impl.CustomService;
import com.qihsoft.webdev.core.tps.CacheKit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 公共缓存业务
 *
 * */
@Component
public class CacheConstant {
    // 后期优化

    @Autowired
    private CustomService customService;
    @Autowired
    private CacheKit cacheKit;


}
