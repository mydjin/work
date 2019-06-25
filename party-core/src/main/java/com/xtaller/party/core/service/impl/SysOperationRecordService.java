package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.SysOperationRecordMapper;
import com.xtaller.party.core.model.SysOperationRecord;
import com.xtaller.party.core.service.ISysOperationRecordService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.convert.W;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qihsoft on 2018/05/08
 */
@Service
public class SysOperationRecordService extends TServiceImpl<SysOperationRecordMapper, SysOperationRecord> implements ISysOperationRecordService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public SysOperationRecord createSysOperationRecord(SysOperationRecord model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteSysOperationRecord(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public SysOperationRecord updateSysOperationRecord(SysOperationRecord model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<SysOperationRecord> findByIds(Object ids) {
        return this.selectByIds(ids);
    }

    // 属于createVarietiesList
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

        List<JSONObject> courses = baseMapper.getPage(w, limit, pageSize);
        return new Page(F.f2l(courses, "id", "isDefault"), pageSize, totalCount, totalPage, currentPage);
    }

    //全查
    public List queryAll(String where) {
        List list = baseMapper.queryAll(where);
        return F.f2l(list, "id", "creator", "reverse");
    }

}