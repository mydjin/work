package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.SectionMapper;
import com.xtaller.party.core.model.Section;
import com.xtaller.party.core.service.ISectionService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/08/29
 */
@Service
public class SectionService extends TServiceImpl<SectionMapper, Section> implements ISectionService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public Section createSection(Section model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteSection(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public Section updateSection(Section model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<Section> findByIds(Object ids) {
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
    //分页查
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

        return new Page(F.f2l(grades, "id", "courseId"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查

    public List<JSONObject> query(String where) {
        List<JSONObject> list = baseMapper.query(where);
        return F.f2l(list, "id", "chapterId", "creator", "reverse");
    }

    //查询IDNAME
    public List<JSONObject> queryIdName(String where) {
        List<JSONObject> list = baseMapper.queryIdName(where);
        return F.f2l(list, "id");
    }

    //根据ID查找内容
    public JSONObject queryById(String id) {
        JSONObject json = baseMapper.queryById(id);
        return F.f2j(json, "id", "chapterId", "creator", "reverse");
    }

    //根据ID查找内容
    public JSONObject queryTypeById(String id) {
        JSONObject json = baseMapper.queryTypeById(id);
        return F.f2j(json);
    }

    //根据ID查找内容和URL
    public JSONObject queryById_url(String id) {
        JSONObject json = baseMapper.queryById_url(id);
        return F.f2j(json, "id", "chapterId", "creator", "reverse");
    }

    //根据ID查找URL
    public JSONObject queryUrlById(String id) {
        JSONObject url = baseMapper.queryUrlById(id);
        return url;
    }

    //根据ID查找导航数据
    public JSONObject queryBarById(String id) {
        JSONObject json = baseMapper.queryBarById(id);
        return F.f2j(json, "id", "chapterId", "courseId", "creator", "reverse");
    }

    //根据用户查找进度
    public List<JSONObject> query_user(String where, String number) {
        List<JSONObject> list = baseMapper.query_user(where, number);
        return F.f2l(list, "id");
    }

    //根据ID查课程
    public JSONObject query_course(String id) {
        JSONObject list = baseMapper.query_course(id);
        return F.f2j(list, "id");
    }


}