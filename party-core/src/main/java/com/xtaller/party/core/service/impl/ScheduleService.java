package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ScheduleMapper;
import com.xtaller.party.core.model.Schedule;
import com.xtaller.party.core.service.IScheduleService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/08/13
 */
@Service
public class ScheduleService extends TServiceImpl<ScheduleMapper, Schedule> implements IScheduleService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public Schedule createSchedule(Schedule model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteSchedule(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public Schedule updateSchedule(Schedule model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<Schedule> findByIds(Object ids) {
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

        List<JSONObject> grades = baseMapper.adminGetPage(w, limit, pageSize);

        return new Page(F.f2l(grades, "id"), pageSize, totalCount, totalPage, currentPage);
    }

    //根据用户分页查课程
    public Page pcUserCoursePage(int index, int pageSize, String w, String number) {
        // 总记录数
        int type = 1;//1-集中学习
        JSONObject row = baseMapper.pcGetUserPageCount(w, number, type);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.pcGetUserPage(w, number, type, limit, pageSize);

        return new Page(F.f2l(grades, "id"), pageSize, totalCount, totalPage, currentPage);
    }

    //根据用户分页查会议
    public Page pcUserMeetingPage(int index, int pageSize, String w, String number) {
        // 总记录数
        int type = 2;//2-会议安排
        JSONObject row = baseMapper.pcGetUserPageCount(w, number, type);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> grades = baseMapper.pcGetUserPage(w, number, type, limit, pageSize);

        return new Page(F.f2l(grades, "id"), pageSize, totalCount, totalPage, currentPage);
    }


    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list, "id", "creator", "reverse");
    }

    //全部详情
    public List<JSONObject> getAllDetail(String id, String number) {
        List<JSONObject> list = baseMapper.getAllDetail(id, number);
        return F.f2l(list, "id", "vacateId");
    }

    //请假详情
    public List<JSONObject> getVacateDetail(String id, String number) {
        List<JSONObject> list = baseMapper.getVacateDetail(id, number);
        return F.f2l(list, "id", "vacateId");
    }

    //签到详情
    public List<JSONObject> getAttendanceDetail(String id, String number) {
        List<JSONObject> list = baseMapper.getAttendanceDetail(id, number);
        return F.f2l(list, "id");
    }


}