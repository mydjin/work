package com.xtaller.party.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.core.mapper.LinkUserMapper;
import com.xtaller.party.core.model.LinkUser;
import com.xtaller.party.core.service.ILinkUserService;
import com.xtaller.party.utils.base.TServiceImpl;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.F;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.bean.Where;

/**
 * Created by party on 2018/10/08
 */
@Service
public class LinkUserService extends TServiceImpl<LinkUserMapper, LinkUser> implements ILinkUserService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public LinkUser createLinkUser(LinkUser model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteLinkUser(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public LinkUser updateLinkUser(LinkUser model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<LinkUser> findByIds(Object ids) {
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

    //根据联络人工号查用户列表
    public List<JSONObject> queryNumberObjByLinkNumber(String linkNumber) {
        String wkey = S.apppend(" and linkNumber = '", linkNumber, "' ");
        List<JSONObject> list = baseMapper.queryAll(wkey);
        return F.f2l(list, "id", "creator", "reverse");
    }

    //根据联络人工号判断用户是否有权限
    public boolean checkUserByLinkNumber(String linkNumber, String number) {
        if (V.isEmpty(linkNumber) || V.isEmpty(number)) {
            return false;
        } else {
            String wkey = S.apppend(" and linkNumber = '", linkNumber, "' " + " and number = '", number, "' ");

            List<JSONObject> list = baseMapper.queryAll(wkey);

            if (V.isEmpty(list)) {
                return false;
            } else {
                return true;
            }

        }

    }


}