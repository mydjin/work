package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ExamRecordMapper;
import com.xtaller.party.core.model.ExamRecord;
import com.xtaller.party.core.service.IExamRecordService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.kit.IdKit;
import com.xtaller.party.utils.kit.TimeKit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/09/11
 */
@Service
public class ExamRecordService extends TServiceImpl<ExamRecordMapper, ExamRecord> implements IExamRecordService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public ExamRecord createExamRecord(ExamRecord model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteExamRecord(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public ExamRecord updateExamRecord(ExamRecord model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<ExamRecord> findByIds(Object ids) {
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
        return F.f2l(list, "id","questionId", "creator", "reverse");
    }

    public int insertRows(List<ExamRecord> examRecords, String userId) throws Exception {
        String values = "";

        for (int i = 0; i < examRecords.size(); i++) {
            ExamRecord exam = examRecords.get(i);
            exam.setId(IdKit.getId(exam.getClass()));//自动生成id
            exam.setCreateTime((int) TimeKit.getTimestamp());
            if(V.isEmpty(exam.getOptionOrdered())){
                exam.setOptionOrdered("");
            }
            if(V.isEmpty(exam.getOrdered())){
                exam.setOrdered(-1);
            }
            String value = "";
            value += "('" + exam.getId() + "',";
            value += "'" + exam.getNumber() + "',";
            value += "'" + exam.getExamId() + "',";
            value += "'" + exam.getQuestionId() + "',";
            value += "'" + exam.getSituation() + "',";
            value += "'" + exam.getAnswerContent() + "',";
            value += "'" + exam.getOrdered() + "',";
            value += "'" + exam.getOptionOrdered() + "',";
            value += "'" + userId + "',";
            value += "'" + exam.getCreateTime() + "')";

            if (i + 1 != examRecords.size()) {
                value += ",";
            }

            values += value;
        }

        int count = baseMapper.insertRows(values);

        return count;
    }

}