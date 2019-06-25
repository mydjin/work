package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Notice;
import com.xtaller.party.core.service.impl.NoticeService;
import com.xtaller.party.doc.NoticeCreate;
import com.xtaller.party.doc.NoticePublish;
import com.xtaller.party.doc.NoticeUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by party on 2018/08/15
 */
@Api(value = "18_通知公告管理")
@RestController
@RequestMapping("/v1/base")
public class NoticeApi extends BaseApi {
    @Autowired
    private NoticeService noticeService;

    @PostMapping("/notice")
    @ApiOperation(value = "通知公告新增")
    public Object createNotice(@RequestBody NoticeCreate object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Notice model = o2c(object, token, Notice.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        Boolean exist = noticeService.exist(W.f(
                W.and("title", "eq", model.getTitle()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该标题已存在请更换！");

        model.setReleaseTime(Integer.parseInt(TimeKit.getTimestamp() + ""));

        model = noticeService.createNotice(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/notice")
    @ApiOperation(value = "修改通知公告")
    public Object updateNotice(@RequestBody NoticeUpdate object,
                               @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Notice model = o2c(object, token, Notice.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        Notice data = noticeService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getTitle().equals(model.getTitle())) {
            Boolean exist = noticeService.exist(W.f(
                    W.and("title", "eq", model.getTitle()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("该标题已存在请更换！");
        }

        model.setReleaseTime(Integer.parseInt(TimeKit.getTimestamp() + ""));

        model.setReviser(userId);
        model = noticeService.updateNotice(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/notice/{id}")
    @ApiOperation(value = "通知公告删除")
    public Object deleteNotice(@PathVariable("id") String id,
                               @RequestHeader("token") String token) {

        if (!noticeService.existId(id))
            return R.error("Id数据异常");

        if (noticeService.deleteNotice(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/notice/{index}-{size}-{title}-{author}")
    @ApiOperation(value = "读取通知公告分页列表")
    public Object getNotice(@PathVariable("index") int index,
                            @PathVariable("size") int size,
                            @PathVariable("title") String title,
                            @PathVariable("author") String author,
                            @RequestHeader("token") String token) {

        String wKey = "";

        //搜索框，通过标题查询（title）
        if (!V.isEmpty(title))
            wKey += S.apppend(" and title like '%", title, "%' ");
        //作者
        if (!V.isEmpty(author))
            wKey += S.apppend(" and author like '%", author, "%' ");
        Page page = noticeService.page(index, size, wKey);
        return R.ok(page);

    }

    @GetMapping("/notice")
    @ApiOperation(value = "读取通知公告所有列表")
    public Object getAllNotice(@RequestHeader("token") String token) {

        return R.ok(noticeService.queryAll(""));

    }

    @PutMapping("/notice/publish/{id}")
    @ApiOperation(value = "发布通知公告")
    public Object publishNotice(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        List<Notice> noticeList = noticeService.findByIds(id);
        if (noticeList == null || noticeList.size() <= 0)
            return R.error("发布失败：找不到该记录");
        Notice notice = noticeList.get(0);
        if (notice.getIsPublish() == 2)
            return R.error("发布失败：该通知公告已发布");

        notice.setReleaseTime(Integer.parseInt(TimeKit.getTimestamp() + ""));
        notice.setIsPublish(2);
        notice.setReviser(userId);

        notice = noticeService.updateNotice(notice);
        if (notice == null)
            return R.error("发布失败");
        return R.ok("发布成功");

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("title", "请输入标题");
        verify.put("content", "请输入内容");
        verify.put("author", "请输入作者");
        verify.put("source", "请输入来源");
//        verify.put("pictureId", "图片id");
//        verify.put("hits", "阅读次数");
        verify.put("summary", "请输入摘要");
//        verify.put("likenum", "点赞数");
//        verify.put("isReview", "是否允许评论");
//        verify.put("isEssence", "精华置顶");
//        verify.put("isCheck", "是否需要审核");
//        verify.put("result", "审核结果");
//        verify.put("status", "审核状态");
//        verify.put("opinion", "审核意见");
//        verify.put("approverNumber", "审核人学号/工号");
//        verify.put("isPublish","是否发布");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("title", "请输入标题");
        verify.put("content", "请输入内容");
        verify.put("author", "请输入作者");
        verify.put("source", "请输入来源");
//        verify.put("pictureId", "图片id");
//        verify.put("hits", "阅读次数");
        verify.put("summary", "请输入摘要");
//        verify.put("likenum", "点赞数");
//        verify.put("isReview", "是否允许评论");
//        verify.put("isEssence", "精华置顶");
//        verify.put("isCheck", "是否需要审核");
//        verify.put("result", "审核结果");
//        verify.put("status", "审核状态");
//        verify.put("opinion", "审核意见");
//        verify.put("approverNumber", "审核人学号/工号");
//        verify.put("isPublish","是否发布");
        return verify;
    }

}
