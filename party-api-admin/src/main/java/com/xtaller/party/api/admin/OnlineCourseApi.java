package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.OnlineCourseCreate;
import com.xtaller.party.doc.OnlineCourseUpdate;
import com.xtaller.party.utils.tool.RemoveHTML;
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

import static java.lang.Thread.sleep;

/**
 * Created by party on 2018/08/23
 */
@Api(value = "24_网络课程管理")
@RestController
@RequestMapping("/v1/base")
public class OnlineCourseApi extends BaseApi {
    @Autowired
    private OnlineCourseService onlineCourseService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private SectionService sectionService;


    @PostMapping("/onlineCourse")
    @ApiOperation(value = "网络课程新增")
    public Object createOnlineCourse(@RequestBody OnlineCourseCreate object,
                                     @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        OnlineCourse model = o2c(object, token, OnlineCourse.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = onlineCourseService.exist(W.f(
                W.and("title", "eq", model.getTitle()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("标题已经存在请更换一个标题");
        System.out.print(model);


        model = onlineCourseService.createOnlineCourse(model);
        if (model == null) {

            return R.error("新增失败");

        } else {

            return R.ok("新增成功", fm2(model));
        }
    }

    @PutMapping("/onlineCourse")
    @ApiOperation(value = "修改网络课程")
    public Object updateOnlineCourse(@RequestBody OnlineCourseUpdate object,
                                     @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        OnlineCourse model = o2c(object, token, OnlineCourse.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        OnlineCourse data = onlineCourseService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getTitle().equals(model.getTitle())) {
            Boolean exist = onlineCourseService.exist(W.f(
                    W.and("title", "eq", model.getTitle()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("标题已经存在请更换一个标题");
        }

        model.setReviser(userId);
        model = onlineCourseService.updateOnlineCourse(model);


        if (model == null) {

            return R.error("修改失败");

        } else {

            return R.ok("修改成功", fm2(model));
        }
    }

    @DeleteMapping("/onlineCourse/{id}")
    @ApiOperation(value = "网络课程删除")
    public Object deleteOnlineCourse(@PathVariable("id") String id,
                                     @RequestHeader("token") String token) {

        if (!onlineCourseService.existId(id))
            return R.error("Id数据异常");

        if (onlineCourseService.deleteOnlineCourse(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/onlineCourse/{index}-{size}-{title}")
    @ApiOperation(value = "读取网络课程分页列表")
    public Object getOnlineCourse(@PathVariable("index") int index,
                                  @PathVariable("size") int size,
                                  @PathVariable("title") String title,
                                  @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(title))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and a.title like '%", title, "%' ");
        return R.ok(onlineCourseService.page(index, size, wKey));

    }

    @GetMapping("/onlineCourse")
    @ApiOperation(value = "读取网络课程所有列表")
    public Object getAllOnlineCourse(@RequestHeader("token") String token) {

        return R.ok(onlineCourseService.queryAll(""));

    }

    @GetMapping("/onlineCourse/getNameById/{CourseId}")
    @ApiOperation(value = "读取课程名字")
    public Object getCourseNameById(@PathVariable("CourseId") String CourseId,
                                    @RequestHeader("token") String token) {


        return R.ok(onlineCourseService.queryNameById(CourseId));


    }

    @GetMapping("/onlineCourse/getById/{CourseId}")
    @ApiOperation(value = "读取课程信息")
    public Object getCourseById(@PathVariable("CourseId") String CourseId,
                                @RequestHeader("token") String token) {


        return R.ok(onlineCourseService.queryJsonById(CourseId));


    }


    @PutMapping("/onlineCourse/publish/{id}")
    @ApiOperation(value = "发布网络课程")
    public Object publishOnlineCourse(@PathVariable("id") String id,
                                      @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        List<OnlineCourse> onlineCourseList = onlineCourseService.findByIds(id);
        if (onlineCourseList == null || onlineCourseList.size() <= 0)
            return R.error("发布失败：找不到该课程");
        OnlineCourse onlineCourse = onlineCourseList.get(0);
        onlineCourse.setIsRelease(1);
        onlineCourse.setReviser(userId);
        onlineCourse = onlineCourseService.updateOnlineCourse(onlineCourse);


        if (onlineCourse == null) {

            return R.error("发布失败");

        } else {


            return R.ok("发布成功", fm2(onlineCourse));

        }


    }


    @GetMapping("/onlineCourse/getCatalogue-{key}")
    @ApiOperation(value = "读取章节目录")
    public Object getCourseCatalogue(@PathVariable("key") String key, @RequestHeader("token") String token) {

        JSONArray Catalogue = new JSONArray();
        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id  =  '", key, "' ");
        List<JSONObject> courseList = onlineCourseService.query(wKey);
        if (courseList.size() > 0) {
            int i = 1;
            for (JSONObject course : courseList) {
                JSONObject courseObj = new JSONObject();
                courseObj.put("id", course.getString("id"));
                courseObj.put("name", "[" + course.getString("topic") + "]" + course.getString("title"));
                courseObj.put("isRelease", course.getIntValue("isRelease"));
                String courseContent = RemoveHTML.Html2Text(course.getString("summary"));
                if (courseContent.length() > 100) {
                    courseContent = courseContent.substring(0, 100) + "…";
                }

                courseObj.put("content", courseContent);
                courseObj.put("type", 0);
                courseObj.put("level", 0);
                courseObj.put("sort", i);
                i++;

                List<JSONObject> chapterList = chapterService.query("and courseId =" + course.getString("id") + " ");
                Catalogue.add(courseObj);
                for (JSONObject chapter : chapterList) {
                    JSONObject chapterObj = new JSONObject();
                    chapterObj.put("id", chapter.getString("id"));
                    chapterObj.put("name", chapter.getString("name"));
                    chapterObj.put("sort", chapter.getIntValue("sort"));
                    chapterObj.put("type", chapter.getIntValue("type"));
                    String chapterContent = RemoveHTML.Html2Text(chapter.getString("content"));
                    if (chapterContent.length() > 100) {
                        chapterContent = chapterContent.substring(0, 100) + "…";
                    }
                    chapterObj.put("content", chapterContent);
                    chapterObj.put("level", 1);

                    chapterObj.put("parentId", course.getString("id"));

                    Catalogue.add(chapterObj);

                    List<JSONObject> sectionList = sectionService.query("and chapterId = " + chapter.getString("id") + " ");
                    for (JSONObject section : sectionList) {
                        JSONObject sectionObj = new JSONObject();
                        sectionObj.put("id", section.getString("id"));
                        sectionObj.put("name", section.getString("name"));
                        sectionObj.put("parentId", chapter.getString("id"));
                        sectionObj.put("sort", section.getIntValue("sort"));
                        sectionObj.put("type", section.getIntValue("type"));
                        sectionObj.put("level", 2);

                        String sectionContent = RemoveHTML.Html2Text(section.getString("content"));
                        if (sectionContent.length() > 100) {
                            sectionContent = sectionContent.substring(0, 100) + "…";
                        }
                        sectionObj.put("content", sectionContent);


                        Catalogue.add(sectionObj);
                    }


                }


            }

        }
        return R.ok(Catalogue);

    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("topic", "请输入主题");
        verify.put("title", "请输入标题");
        verify.put("summary", "请输入摘要");

        verify.put("startTime", "请选择开始时间");
        verify.put("endTime", "请选择结束时间");
        verify.put("property", "请选择性质");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("topic", "请输入主题");
        verify.put("title", "请输入标题");
        verify.put("summary", "请输入摘要");

        verify.put("startTime", "请选择开始时间");
        verify.put("endTime", "请选择结束时间");
        verify.put("property", "请选择性质");

        return verify;
    }


}
