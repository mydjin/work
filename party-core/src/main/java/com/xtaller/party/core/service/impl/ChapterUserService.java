package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ChapterUserMapper;
import com.xtaller.party.core.model.ChapterUser;
import com.xtaller.party.core.model.SectionUser;
import com.xtaller.party.core.service.IChapterUserService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/11/05
 */
@Service
public class ChapterUserService extends TServiceImpl<ChapterUserMapper, ChapterUser> implements IChapterUserService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public ChapterUser createChapterUser(ChapterUser model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteChapterUser(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public ChapterUser updateChapterUser(ChapterUser model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<ChapterUser> findByIds(Object ids) {
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

        return new Page(F.f2l(grades, "id"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list, "id", "creator", "reverse");
    }

    //分页查(后台)
    public Page page_admin(int index, int pageSize, String w) {
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

        List<JSONObject> grades = baseMapper.getPage_admin(w, limit, pageSize);

        return new Page(F.f2l(grades, "id"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查(后台)
    public List<JSONObject> queryAll_admin(String where) {
        List<JSONObject> list = baseMapper.queryAll_admin(where);
        return F.f2l(list, "id");
    }


    //查询进度记录
    public JSONObject queryUserNote(String number, String chapterCode) {
        JSONObject note = baseMapper.queryUserNote(number, chapterCode);
        return F.f2j(note, "id", "lastLearnTime", "chapterCode");
    }

    //查询完成情况
    public int queryEnded(String number, String chapterCode) {
        int num = baseMapper.queryEnded(number, chapterCode);
        return num;

    }

    //查询节数量
    public int querySectionNum(String chapterCode) {
        int num = baseMapper.querySectionNum(chapterCode);
        return num;

    }

}