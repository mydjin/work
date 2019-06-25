package com.qihsoft.webdev.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.core.mapper.ArticleMapper;
import com.qihsoft.webdev.core.model.Article;
import com.qihsoft.webdev.core.service.IArticleService;
import com.qihsoft.webdev.utils.base.TServiceImpl;
import com.qihsoft.webdev.utils.bean.Page;
import com.qihsoft.webdev.utils.bean.Where;
import com.qihsoft.webdev.utils.convert.F;
import com.qihsoft.webdev.utils.convert.W;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taller on 2018/06/10
 */
@Service
public class ArticleService extends TServiceImpl<ArticleMapper, Article> implements IArticleService {
    /**************************CURD begin******************************/
    // 创建
    @Override
    public Article createArticle(Article model) {
        if (this.insert(model))
            return this.selectById(model.getId());
        return null;
    }

    // 删除
    @Override
    public Boolean deleteArticle(Object ids, String reviser) {
        return this.delete(ids, reviser);
    }

    // 修改
    @Override
    public Article updateArticle(Article model) {
        if (this.update(model))
            return this.selectById(model.getId());
        return null;
    }

    // 查询
    @Override
    public List<Article> findByIds(Object ids) {
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
    @Override
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

        List<JSONObject> article = baseMapper.getPage(w, limit, pageSize);

        return new Page(F.f2l(article, "id", "pictureId"), pageSize, totalCount, totalPage, currentPage);
    }

    public List<JSONObject> queryIdName() {
        List<JSONObject> list = baseMapper.getIdName();
        return F.f2l(list, "id");
    }


    public Page getLists(int index, int pageSize, String w) {
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

        List<JSONObject> courses = baseMapper.getList(w, limit, pageSize);

        return new Page(F.f2l(courses, "id", "pictureId"), pageSize, totalCount, totalPage, currentPage);
    }


    //全查
    public List<JSONObject> queryAll(String where) {
        List<JSONObject> list = baseMapper.queryAll(where);
        return F.f2l(list, "id", "pictureId", "creator", "reverse");
    }

    //查询所有列表
    public List<JSONObject> WqueryAll(String where) {
        List<JSONObject> list = baseMapper.WqueryAll(where);
        return F.f2l(list, "id", "title", "hits", "name", "artworkURL", "thumbnailURL");
    }

    //查单个信息
    public JSONObject queryById(String where) {
        JSONObject list = baseMapper.getInfo(where);
        return F.f2j(list, "id", "pictureId", "creator", "reverse");
    }


    //查询图片
    public List<JSONObject> getPicture(String where) {
        List<JSONObject> list = baseMapper.getPicture(where);
        return F.f2l(list, "id", "pictureId");
    }

    public int getHits(String table, String id) {
        int hits = baseMapper.getHits(table, id);
        return hits;
    }

    public void setHits(String table, String id, int hits) {
        baseMapper.setHits(table, id, hits);
    }


    public Page WgetLists(int index, int pageSize, String w) {
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

        List<JSONObject> courses = baseMapper.WgetList(w, limit, pageSize);

        return new Page(F.f2l(courses, "id", "title", "hits", "name", "artworkURL", "thumbnailURL"), pageSize, totalCount, totalPage, currentPage);
    }


}