package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ChapterMapper;
import com.xtaller.party.core.model.Chapter;
import com.xtaller.party.core.service.IChapterService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/08/28
 */
@Service
public class ChapterService extends TServiceImpl<ChapterMapper, Chapter> implements IChapterService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public Chapter createChapter(Chapter model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteChapter(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public Chapter updateChapter(Chapter model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<Chapter> findByIds(Object ids) {
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
        return F.f2l(list, "id", "courseId", "creator", "reverse");
    }

    //查询IDNAME
    public List<JSONObject> queryIdName(String where) {
        List<JSONObject> list = baseMapper.queryIdName(where);
        return F.f2l(list, "id");
    }

    //查询用户进度
    public List<JSONObject> query_user(String where, String number) {
        List<JSONObject> list = baseMapper.query_user(where, number);
        return list;
    }

    //根据ID查找
    public JSONObject queryById(String id) {
        JSONObject json = baseMapper.queryById(id);
        return F.f2j(json, "id");
    }

}