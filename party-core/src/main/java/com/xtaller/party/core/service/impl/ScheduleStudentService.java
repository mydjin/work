package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ScheduleStudentMapper;
import com.xtaller.party.core.model.Message;
import com.xtaller.party.core.model.ScheduleStudent;
import com.xtaller.party.core.service.IScheduleStudentService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.kit.IdKit;
import com.xtaller.party.utils.kit.TimeKit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/08/14
 */
@Service
public class ScheduleStudentService extends TServiceImpl<ScheduleStudentMapper, ScheduleStudent> implements IScheduleStudentService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public ScheduleStudent createScheduleStudent(ScheduleStudent model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteScheduleStudent(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public ScheduleStudent updateScheduleStudent(ScheduleStudent model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<ScheduleStudent> findByIds(Object ids) {
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


    //通过关系id和安排类型(1-集中学习，2-会议)获取安排的详细信息和安排学生详细信息
    public List<JSONObject> queryInfo(String where) {
        List<JSONObject> list = baseMapper.queryInfo(where);
        return F.f2l(list, "id", "number", "scheduleId");
    }

    public int inserRows(List<ScheduleStudent> scheduleStudents) {
        String values = "";

        for (int i = 0; i < scheduleStudents.size(); i++) {
            ScheduleStudent object = scheduleStudents.get(i);
            object.setId(IdKit.getId(object.getClass()));//自动生成id
            object.setCreateTime((int) TimeKit.getTimestamp());
            String value = "";
            value += "('" + object.getId() + "',";
            value += "'" + object.getNumber() + "',";
            value += "'" + object.getScheduleId() + "',";
            value += "'" + object.getType() + "',";
            value += "'" + object.getCreator() + "',";
            value += "'" + object.getCreateTime() + "')";

            if (i + 1 != scheduleStudents.size()) {
                value += ",";
            }

            values += value;
        }
        int count = 0;
        if (scheduleStudents.size() != 0) {
            count = baseMapper.inserRows(values);
        }

        return count;
    }

}