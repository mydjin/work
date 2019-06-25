package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Chapter;
import com.xtaller.party.core.model.OnlineCourse;
import com.xtaller.party.core.model.Section;
import com.xtaller.party.core.service.impl.ChapterService;
import com.xtaller.party.core.service.impl.OnlineCourseService;
import com.xtaller.party.core.service.impl.SectionService;
import com.xtaller.party.doc.ChapterCreate;
import com.xtaller.party.doc.ChapterUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by party on 2018/08/28
 */
@Api(value = "26_网络课程章目录管理")
@RestController
@RequestMapping("/v1/base")
public class ChapterApi extends BaseApi {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private SectionService sectionService;


    @PostMapping("/chapter")
    @ApiOperation(value = "网络课程章目录新增")
    public Object createChapter(@RequestBody ChapterCreate object,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Chapter model = o2c(object, token, Chapter.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = chapterService.exist(W.f(
                W.and("code", "eq", model.getCode()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("代码已经存在请更换一个代码");

        Boolean nameexist = chapterService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("isDel", "eq", "0"))
        );
        if (nameexist)
            return R.error("名称已经存在请更换一个名称");


        model = chapterService.createChapter(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/chapter")
    @ApiOperation(value = "修改网络课程章目录")
    public Object updateChapter(@RequestBody ChapterUpdate object,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Chapter model = o2c(object, token, Chapter.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        model.setReviser(userId);
        model = chapterService.updateChapter(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/chapter/{id}")
    @ApiOperation(value = "网络课程章目录删除")
    public Object deleteChapter(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {

        if (!chapterService.existId(id))
            return R.error("Id数据异常");

        if (chapterService.deleteChapter(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/chapter/{index}-{size}-{key}")
    @ApiOperation(value = "读取网络课程章目录分页列表")
    public Object getChapter(@PathVariable("index") int index,
                             @PathVariable("size") int size,
                             @PathVariable("key") String key,
                             @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(chapterService.page(index, size, wKey));

    }

    @GetMapping("/chapter")
    @ApiOperation(value = "读取网络课程章目录所有列表")
    public Object getAllChapter(@RequestHeader("token") String token) {

        return R.ok(chapterService.query(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("courseId", "请输入网络课程id");
        verify.put("name", "请输入章目录名称");
//        verify.put("code", "请输入代码");
        verify.put("sort", "请输入排序");
        verify.put("type", "请输入类型");
//        verify.put("content", "请输入内容");
        return verify;

    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("courseId", "请输入网络课程id");
        verify.put("name", "请输入章目录名称");
//        verify.put("code", "请输入代码");
        verify.put("sort", "请输入排序");
        verify.put("type", "请输入类型");
//        verify.put("content", "请输入内容");
        return verify;
    }


    @GetMapping("/chapter/getById/{chapterId}")
    @ApiOperation(value = "读取章信息")
    public Object getChapterById(@PathVariable("chapterId") String chapterId,
                                 @RequestHeader("token") String token) {

        JSONObject json = chapterService.queryById(chapterId);

        return R.ok(json);


    }


    @GetMapping("/chapter/getCatalogue")
    @ApiOperation(value = "读取章节目录")
    public Object getChapterCatalogue(@RequestHeader("token") String token) {
        JSONArray Catalogue = new JSONArray();

        List<JSONObject> chapterList = chapterService.query("");
        if (chapterList.size() > 0) {
            for (JSONObject chapter : chapterList) {
                JSONObject chapterObj = new JSONObject();
                chapterObj.put("id", chapter.getString("id"));
                chapterObj.put("name", chapter.getString("name"));
                chapterObj.put("sort", chapter.getIntValue("sort"));
                chapterObj.put("type", chapter.getIntValue("type"));

                Catalogue.add(chapter);

                List<JSONObject> sectionList = sectionService.query("and chapterId = " + chapter.getString("id") + " ");
                for (JSONObject section : sectionList) {
                    JSONObject sectionObj = new JSONObject();
                    sectionObj.put("id", section.getString("id"));
                    sectionObj.put("name", section.getString("name"));
                    sectionObj.put("parentId", chapter.getString("id"));
                    sectionObj.put("sort", section.getIntValue("sort"));
                    sectionObj.put("type", section.getIntValue("type"));

                    Catalogue.add(sectionObj);
                }


            }
        }

        return R.ok(Catalogue);
    }


}
