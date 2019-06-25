package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.OnlineCourseMapper;
import com.xtaller.party.core.model.OnlineCourse;
import com.xtaller.party.core.model.Section;
import com.xtaller.party.core.service.IOnlineCourseService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/08/23
 */
@Service
public class OnlineCourseService extends TServiceImpl<OnlineCourseMapper, OnlineCourse> implements IOnlineCourseService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public OnlineCourse createOnlineCourse(OnlineCourse model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteOnlineCourse(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public OnlineCourse updateOnlineCourse(OnlineCourse model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<OnlineCourse> findByIds(Object ids) {
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

        return new Page(F.f2l(grades, "id", "pictureId", "artworkURL"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list, "id", "pictureId", "creator", "reverse");
    }

    //全查
    public List<JSONObject> queryAll_user(String where, String number) {
        List<JSONObject> list = baseMapper.queryAll_user(where, number);
        return F.f2l(list, "id", "pictureId", "creator", "reverse");
    }

    //查名字
    public String queryNameById(String id) {
        String json = baseMapper.queryNameById(id);
        return json;
    }

    //根据ID查课程
    public JSONObject queryJsonById(String id) {
        JSONObject json = baseMapper.queryById(id);
        return json;
    }


    //    pc分页列表
    public Page pcPage(int index, int pageSize, String w) {
        // 总记录数
        JSONObject row = baseMapper.pcGetPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.pcGetPage(w, limit, pageSize);

        return new Page(F.f2l(grades, "id", "pictureId", "artworkURL"), pageSize, totalCount, totalPage, currentPage);
    }

    public Page pcPage_note(int index, int pageSize, String w, String number) {
        // 总记录数
        JSONObject row = baseMapper.pcGetPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.pcGetPage_note(w, limit, pageSize, number);

        return new Page(F.f2l(grades, "id", "pictureId", "artworkURL"), pageSize, totalCount, totalPage, currentPage);
    }


    public Page pcPage_range(int index, int pageSize, String w, String number) {
        // 总记录数
        JSONObject row = baseMapper.pcGetPageByRangeCount(w, number);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.pcGetPageByRange(w, limit, pageSize, number);

        return new Page(F.f2l(grades, "id", "pictureId", "artworkURL"), pageSize, totalCount, totalPage, currentPage);
    }

    public List<JSONObject> query(String where) {
        List<JSONObject> list = baseMapper.query(where);
        return F.f2l(list, "id", "pictureId", "artworkURL");
    }


    //wechat分页列表
    public Page wechatPage(int index, int pageSize, String w) {
        // 总记录数
        JSONObject row = baseMapper.pcGetPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.pcGetPage(w, limit, pageSize);

        return new Page(F.f2l(grades, "id", "pictureId", "artworkURL", "pictureId"), pageSize, totalCount, totalPage, currentPage);
    }

    public Page wechatPage_user(int index, int pageSize, String w, String number) {
        // 总记录数
        JSONObject row = baseMapper.pcGetPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> CourseLearn = baseMapper.wechatGetPage_CourseLearn(limit, pageSize, number);

        return new Page(F.f2l(CourseLearn, "id", "pictureId", "artworkURL", "pictureId"), pageSize, totalCount, totalPage, currentPage);
    }


    public Page wechatPage_range(int index, int pageSize, String w, String number) {
        // 总记录数
        JSONObject row = baseMapper.pcGetPageByRangeCount(w, number);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> CourseLearn = baseMapper.wechatGetPage_CourseLearn(limit, pageSize, number);

        return new Page(F.f2l(CourseLearn, "id", "pictureId", "artworkURL", "pictureId"), pageSize, totalCount, totalPage, currentPage);
    }

    //wechat_courselearn
    public List<JSONObject> wechatqueryAll_CourseLearn(int index, int size, String number) {
        List<JSONObject> list = baseMapper.wechatGetPage_CourseLearn(index, size, number);
        return F.f2l(list, "id", "pictureId", "creator", "reverse");
    }

    //wechat_coursename(index)
    public List<JSONObject> wechatqueryAll_CourseName(String number) {
        List<JSONObject> list = baseMapper.queryAll_CourseName(number);
        return F.f2l(list);
    }
}