package com.qihsoft.webdev.api.unified;

import com.qihsoft.webdev.doc.ArticleCreate;
import com.qihsoft.webdev.doc.ArticleUpdate;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.core.model.Article;
import com.qihsoft.webdev.core.service.impl.ArticleService;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "28_文章管理api")
@RestController
@RequestMapping("/v1/base")
public class NewsApi extends BaseApi {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/article")
    @ApiOperation(value = "添加文章")
    public Object createArticle(@RequestBody ArticleCreate object
            , @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);//获取操作人id
        //映射对象
        Article model = o2c(object, token, Article.class);
        //数据校验
        //JSONObject check = V.checkEmpty(verify(),object);

        Boolean exist = articleService.exist(W.f(
                W.and("title", "eq", model.getTitle()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该标题已存在请更换！");

        model = articleService.createArticle(model);
        if (model == null)
            return R.error("添加失败");

        return R.ok("添加成功", fm2(model));
    }

    @PutMapping("/article")
    @ApiOperation(value = " 修改文章")
    public Object updateArticle(@RequestBody ArticleUpdate object, @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);//获取操作人id
        //映射对象
        Article model = o2c(object, token, Article.class);
        //数据校验
        Article data = articleService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getTitle().equals(model.getTitle())) {
            Boolean exist = articleService.exist(W.f(
                    W.and("title", "eq", model.getTitle()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("该标题已存在请更换！");
        }

        //验证  学号/工号是否存在,id不等于object.id
        model.setReviser(userId);
        model = articleService.updateArticle(model);
        if (model == null)
            return R.error("修改失败");

        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/article/{id}")
    @ApiOperation(value = "删除文章")
    public Object deleteArticle(@PathVariable("id") String id, @RequestHeader("token") String token) {

        if (!articleService.existId(id))
            return R.error("Id数据异常");

        if (articleService.delete(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/article/{index}-{size}-{key}")
    @ApiOperation(value = "读取文章分页列表")
    public Object getArticle(@PathVariable("index") int index,
                             @PathVariable("size") int size,
                             @PathVariable("key") String key,
                             @RequestHeader("token") String token) {


        String wKey = "";
        if (!V.isEmpty(key))
            wKey = S.apppend(" and title like '%", key, "%' ");
        return R.ok(articleService.page(index, size, wKey));

    }


    @GetMapping("/article/info/{id}")
    @ApiOperation(value = "查询文章信息")
    public Object getArticle(@PathVariable("id") String id,
                             @RequestHeader("token") String token) {

        return R.ok(articleService.queryById(id));

    }

    @PutMapping("/article/release/{id}")
    @ApiOperation(value = "发布文章")
    public Object releaseArticle(@PathVariable("id") String id,
                                 @RequestHeader("token") String token) {

        List<Article> articleList = articleService.findByIds(id);
        if (V.isEmpty(articleList)) {
            return R.error("找不到该文章");
        }

        Article article = articleList.get(0);
        article.setReleaseStatus(2);
        article = articleService.updateArticle(article);
        if (article == null)
            return R.error("发布失败");
        return R.ok("发布成功");

    }

}
