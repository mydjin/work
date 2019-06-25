package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.ExamCandidateMapper;
import com.xtaller.party.core.model.ExamCandidate;
import com.xtaller.party.core.model.Message;
import com.xtaller.party.core.service.IExamCandidateService;
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
 * Created by party on 2018/09/01
 */
@Service
public class ExamCandidateService extends TServiceImpl<ExamCandidateMapper, ExamCandidate> implements IExamCandidateService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public ExamCandidate createExamCandidate(ExamCandidate model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteExamCandidate(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public ExamCandidate updateExamCandidate(ExamCandidate model) throws Exception {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<ExamCandidate> findByIds(Object ids) {
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


    public int insertRows(List<ExamCandidate> examCandidates) {
        String values = "";

        for (int i = 0; i < examCandidates.size(); i++) {
            ExamCandidate exam = examCandidates.get(i);
            exam.setId(IdKit.getId(exam.getClass()));//自动生成id
            exam.setCreateTime((int) TimeKit.getTimestamp());
            String value = "";
            value += "('" + exam.getId() + "',";
            value += "'" + exam.getNumber() + "',";
            value += "'" + exam.getExamId() + "',";
            value += "'" + exam.getCreator() + "',";
            value += "'" + exam.getCreateTime() + "')";

            if (i + 1 != examCandidates.size()) {
                value += ",";
            }

            values += value;
        }

        int count = baseMapper.insertRows(values);

        return count;
    }

    //分页查试卷详情
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

        List<JSONObject> examStudents  = baseMapper.getPage(w, limit, pageSize);

        return new Page(F.f2l(examStudents , "id","examId","roomId"), pageSize, totalCount, totalPage, currentPage);
    }


    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list, "id","examId","roomId", "creator", "reverse","startTimeStr","endTimeStr");
    }

    //全查个人的在线考试
    public List<JSONObject> queryPersonExamAll(String where) {
        List<JSONObject> list = baseMapper.queryPersonExamAll(where);
        return F.f2l(list, "id", "examId","roomId", "creator", "reverse");
    }

    //分页全查某课程下面的考试成绩
    public Page gradePage(int index, int pageSize, String examWhere,String ubiWhere) {
        // 总记录数
        JSONObject row = new JSONObject();
        if(V.isEmpty(ubiWhere)&&V.isEmpty( ubiWhere)){
            row =   baseMapper.getPageGradeCount_examSelect(examWhere);
        }else{
            row =   baseMapper.getPageGradeCount_userSelect(examWhere,ubiWhere);
        }

        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> examStudents  = new ArrayList<>();

        if(V.isEmpty(ubiWhere)){
            examStudents   =    baseMapper.getPageGrade_examSelect(examWhere, limit, pageSize);
        }else{
            examStudents   =   baseMapper.getPageGrade_userSelect(examWhere,ubiWhere, limit, pageSize);
        }


        return new Page(F.f2l(examStudents , "id","examId","roomId"), pageSize, totalCount, totalPage, currentPage);
    }


    //全查
    public List<JSONObject> queryAllGrade(String where,String userWhere) {
        List<JSONObject> list = new ArrayList<>();
        if(V.isEmpty(userWhere)){
            list = baseMapper.queryAllGrade(where);
        }else{
            list = baseMapper.queryAllGrade_user(where,userWhere);
        }
        return F.f2l(list, "score","examId","roomId");
    }

    //全查个人的在线考试排名
    public int getRank(String examId, String number) {
        int rank = baseMapper.getRank(examId, number);
        return rank;
    }


    //查询总年榜
    public Page rankYearPage(int index, int pageSize, String w) {
        // 总记录数
        JSONObject row = baseMapper.getRankYearPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> examStudents  = baseMapper.getRankYearPage(w, limit, pageSize);

        return new Page(F.f2l(examStudents ), pageSize, totalCount, totalPage, currentPage);
    }


    //查询个人年榜
    public JSONObject rankYearUser(String w, String number) {

        JSONObject user = baseMapper.getRankYearUser(w, number);

        return user;
    }


    //查询总月榜
    public Page rankMonthPage(int index, int pageSize, String w) {
        // 总记录数
        JSONObject row = baseMapper.getRankMonthPageCount(w);
        int totalCount = row.getInteger("total");
        if (totalCount == 0)
            return new Page(new ArrayList<JSONObject>(), pageSize, 0, 0, 1);
        // 分页数据
        index = index < 0 ? 1 : index;
        int limit = (index - 1) * pageSize;
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        int currentPage = index;

        List<JSONObject> examStudents  = baseMapper.getRankMonthPage(w, limit, pageSize);

        return new Page(F.f2l(examStudents ), pageSize, totalCount, totalPage, currentPage);
    }

    //查询个人月榜
    public JSONObject rankMonthUser(String w, String number) {

        JSONObject user = baseMapper.getRankMonthUser(w, number);

        return user;
    }

    //根据试卷Id查询全部考生
    public String[] queryStudentsByExamId(String examId) {
        String[] list = baseMapper.queryStudentsByExamId(examId);
        return list;
    }

}