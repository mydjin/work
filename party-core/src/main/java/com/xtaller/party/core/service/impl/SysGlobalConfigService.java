package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.tps.CacheKit;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.kit.IdKit;

import com.xtaller.party.utils.convert.J;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.constant.Global;
import com.xtaller.party.core.mapper.SysGlobalConfigMapper;
import com.xtaller.party.core.model.SysGlobalConfig;
import com.xtaller.party.core.service.ISysGlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Taller on 2017/10/08
 */
@Service
public class SysGlobalConfigService extends TServiceImpl<SysGlobalConfigMapper, SysGlobalConfig> implements ISysGlobalConfigService {

    @Autowired
    private CacheKit cacheKit;

    /**************************CURD begin******************************/
    // 创建
    @Override
    public SysGlobalConfig createGlobalConfig(SysGlobalConfig model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteGlobalConfig(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public SysGlobalConfig updateGlobalConfig(SysGlobalConfig model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<SysGlobalConfig> findByIds(Object ids) {
        return this.selectByIds(ids);
    }

    // 属于
    @Override
    public Boolean exist(List<Where> w) {
        w.add(new Where("1"));
        return this.query(w).size() > 0;
    }

    @Override
    public Boolean existSystemId(Object systemId) {
        where = W.f(
                W.and("systemId", "eq", systemId),
                W.field("1")
        );
        return this.query(where).size() > 0;
    }

    // 查询一个id是否存在
    @Override
    public Boolean existId(Object id) {
        where = W.f(
                W.and("id", "eq", id),
                W.field("1")
        );
        return this.query(where).size() > 0;
    }

    /**************************CURD end********************************/

    @Transactional
    @Override
    public Boolean config(List<SysGlobalConfig> models) {
        baseMapper.deleteBySystemId();
        String redisKey = S.apppend("global-setting");
        if (models.size() > 0) {
            for (SysGlobalConfig model : models) {
                model.setId(IdKit.getId(SysGlobalConfig.class));
                this.insert(model);
            }
        }
        cacheKit.setVal(redisKey, J.o2s(getData()), 0);
        return true;
    }

    @Override
    public JSONObject getSetting() {
        String redisVal = null;
        String redisKey = S.apppend("global-setting");
        try {
            redisVal = cacheKit.getVal(redisKey);
        } catch (Exception e) {
        }
        if (!V.isEmpty(redisVal)) {
            return J.s2j(redisVal);
        } else {
            JSONObject model = getData();
            cacheKit.setVal(redisKey, J.o2s(model), 0);
            return model;
        }
    }

    private JSONObject getData() {
        JSONObject model = J.arr2j(Global.CONFIG);
        where = W.f(
                W.and("1", "eq", 1)
        );
        List<SysGlobalConfig> sysGlobalConfigs = this.query(where);
        if (sysGlobalConfigs.size() > 0) {
            for (SysGlobalConfig gc : sysGlobalConfigs) {
                model.put(gc.getAttr(), gc.getVal());
            }
        }
        return model;
    }

}