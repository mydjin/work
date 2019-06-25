package com.xtaller.party.core.cache;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.service.impl.BaseClassService;
import com.xtaller.party.core.service.impl.CustomService;
import com.xtaller.party.core.service.impl.SysCategoryService;
import com.xtaller.party.core.tps.CacheKit;
import com.xtaller.party.utils.convert.J;

import com.xtaller.party.utils.convert.V;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 公共缓存业务
 *
 * */
@Component
public class CacheConstant {
    // 后期优化

    @Autowired
    private SysCategoryService categoryService;

    @Autowired
    private BaseClassService classService;
    @Autowired
    private CustomService customService;
    @Autowired
    private CacheKit cacheKit;
    /**
     * 教师功能基础数据
     * 科目 部门 年级
     * */

}
