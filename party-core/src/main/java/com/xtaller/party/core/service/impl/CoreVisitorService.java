package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.core.mapper.CoreVisitorMapper;
import com.xtaller.party.core.model.CoreVisitor;
import com.xtaller.party.core.service.ICoreVisitorService;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.convert.S;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by Taller on 2018/01/02
 */
@Service
public class CoreVisitorService extends TServiceImpl<CoreVisitorMapper, CoreVisitor> implements ICoreVisitorService {
    @Autowired
    private SysGlobalConfigService configService;

    /**************************CURD begin******************************/
    // 创建
    @Override
    public CoreVisitor createCoreVisitor(CoreVisitor model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteCoreVisitor(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public CoreVisitor updateCoreVisitor(CoreVisitor model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<CoreVisitor> findByIds(Object ids) {
        return this.selectByIds(ids);
    }

    // 属于
    @Override
    public Boolean exist(List<Where> w) {
        w.add(new Where("1"));
        return this.query(w).size() > 0;
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
    @Override
    public Page page(int index, int pageSize, String w) {
        // 总记录数
        JSONObject row = baseMapper.getPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> list = baseMapper.getPage(w, limit, pageSize);

        // 获取基础网址
        JSONObject setting = configService.getSetting();
        list = F.f2l(list, "id");
        for (JSONObject obj : list) {
            obj.put("idCardImageRealPath", S.apppend(setting.getString("readPath"), obj.getString("idCardImage")));
            obj.put("curImageRealPath", S.apppend(setting.getString("readPath"), obj.getString("curImage")));
        }
        return new Page(list, pageSize, totalCount, totalPage, currentPage);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     */
    public CoreVisitor findById(String id) {
        CoreVisitor back = null;
        List<CoreVisitor> list = this.findByIds(id);
        if (list != null && list.size() > 0) {
            back = list.get(0);
        }
        return back;
    }
}