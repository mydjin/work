package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.mapper.VideoMapper;
import com.qihsoft.webdev.core.model.Video;
import com.qihsoft.webdev.core.service.IVideoService;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.W;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by party on 2018/08/29
 */
@Service
public class VideoService extends TServiceImpl<VideoMapper, Video> implements IVideoService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public Video createVideo(Video model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteVideo(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public Video updateVideo(Video model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<Video> findByIds(Object ids) {
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

    public List<JSONObject> getURL(String where) {
        List<JSONObject> list = baseMapper.getURL(where);
        return F.f2l(list, "url");
    }

    //只返回URL
    public List<JSONObject> checkVideo(String code) {
        List<JSONObject> list = baseMapper.checkVideo(code);
        return F.f2l(list, "id", "creator", "reverse");
    }

    public boolean deleteBySec(String section) {
        baseMapper.deleteBySec(section);
        return true;
    }


}