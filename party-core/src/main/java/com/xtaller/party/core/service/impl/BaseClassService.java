package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.core.mapper.BaseClassMapper;
import com.xtaller.party.core.model.BaseClass;
import com.xtaller.party.core.service.IBaseClassService;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by Taller on 2017/11/08
 */
@Service
public class BaseClassService extends TServiceImpl<BaseClassMapper, BaseClass> implements IBaseClassService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public JSONObject createBaseClass(BaseClass model) {
        if (this.insert(model))
            return F.f2j(baseMapper.getById(model.getId()),
                    "id", "gradeId", "userId", "userId2", "userId3", "studentId");
        return null;
    }

    // 删除
    @Override
    public Boolean deleteBaseClass(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public JSONObject updateBaseClass(BaseClass model) {
        if (this.update(model))
            return F.f2j(baseMapper.getById(model.getId()),
                    "id", "gradeId", "userId", "userId2", "userId3", "studentId");
        return null;
    }

    // 查询
    @Override
    public List<BaseClass> findByIds(Object ids) {
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

        List<JSONObject> grades = baseMapper.getPage(w, limit, pageSize);

        return new Page(F.f2l(grades,
                "id", "gradeId", "userId", "userId2", "userId3", "studentId"), pageSize, totalCount, totalPage, currentPage);
    }

    public JSONObject getClassInfo(String classId) {
        return baseMapper.getById(classId);
    }

}