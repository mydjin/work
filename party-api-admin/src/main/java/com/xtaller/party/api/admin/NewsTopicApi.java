package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.ArticleTopic;
import com.xtaller.party.core.service.impl.ArticleTopicService;
import com.xtaller.party.doc.ArticleTopicCreate;
import com.xtaller.party.doc.ArticleTopicUpdate;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taller on 2017/11/30.
 */
@Api(value = "29_文章主题类型api")
@RestController
@RequestMapping("/v1/base")
public class NewsTopicApi extends BaseApi {
    @Autowired
    private ArticleTopicService articleTopicService;

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("topicTypeCode", "请输入主题类型代码");
        return verify;
    }

    @PostMapping("/articleTopic")
    @ApiOperation(value = " 新增主题类型信息")
    public Object createArticleTopic(@RequestBody ArticleTopicCreate object
            , @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);//获取操作人id
        //映射对象
        ArticleTopic model = o2c(object, token, ArticleTopic.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        //验证代码是否存在
        Boolean exist = articleTopicService.exist(W.f(
                W.and("topicTypeCode", "eq", model.getTopicTypeCode()))
        );

        if (exist)
            return R.error("主题类型代码已经存在请更换一个代码");


        Boolean nameexist = articleTopicService.exist(W.f(
                W.and("name", "eq", model.getTopicTypeName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("名称已经存在请更换一个名称");


        model = articleTopicService.createArticleTopic(model);
        if (model == null)
            return R.error("创建失败");

        return R.ok("创建成功", fm2(model));
    }

    @PutMapping("/articleTopic")
    @ApiOperation(value = " 修改主题类型信息")
    public Object updateArticleTopic(@RequestBody ArticleTopicUpdate object, @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);//获取操作人id
        //映射对象
        ArticleTopic model = o2c(object, token, ArticleTopic.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        //验证  学号/工号是否存在,id不等于object.id
        Boolean exist = articleTopicService.exist(W.f(
                W.and("topicTypeCode", "eq", object.getTopicTypeCode()))
        );
        if (exist)
            return R.error("主题类型代码已经存在请更换");
        model.setReviser(userId);
        model = articleTopicService.updateArticleTopic(model);
        if (model == null)
            return R.error("修改失败");

        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/articleTopic/{id}")
    @ApiOperation(value = "删除主题类型信息")
    public Object deleteArticleTopic(@PathVariable("id") String id, @RequestHeader("token") String token) {

        if (!articleTopicService.existId(id))
            return R.error("Id数据异常");

        if (articleTopicService.delete(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/articleTopic/{index}-{size}-{topicTypeCode}")
    @ApiOperation(value = "读取用户基本信息分页列表")
    public Object getArticleTopic(@PathVariable("index") int index,
                                  @PathVariable("size") int size,
                                  @PathVariable("topicTypeCode") String topicTypeCode,
                                  @RequestHeader("token") String token) {


        String wKey = "";
        if (!V.isEmpty(topicTypeCode))
            wKey = S.apppend(" and (topicTypeName like '%", topicTypeCode, "%')");
        return R.ok(articleTopicService.page(index, size, wKey));

    }

}
