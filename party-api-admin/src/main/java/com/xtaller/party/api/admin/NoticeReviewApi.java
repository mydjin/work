package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.NoticeReview;
import com.xtaller.party.core.service.impl.NoticeReviewService;
import com.xtaller.party.doc.NoticeReviewCreate;
import com.xtaller.party.doc.NoticeReviewUpdate;
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
 * Created by party on 2018/08/23
 */
@Api(value = "22_通知公告评论点赞管理")
@RestController
@RequestMapping("/v1/base")
public class NoticeReviewApi extends BaseApi {
    @Autowired
    private NoticeReviewService noticeReviewService;

    @PostMapping("/noticeReview")
    @ApiOperation(value = "通知公告评论点赞新增")
    public Object createNoticeReview(@RequestBody NoticeReviewCreate object,
                                     @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        NoticeReview model = o2c(object, token, NoticeReview.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        model = noticeReviewService.createNoticeReview(model);
        if (model == null)
            return R.error("新增失败");
        return R.ok("新增成功", fm2(model));
    }

    @PutMapping("/noticeReview")
    @ApiOperation(value = "修改通知公告评论点赞")
    public Object updateNoticeReview(@RequestBody NoticeReviewUpdate object,
                                     @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        NoticeReview model = o2c(object, token, NoticeReview.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        model.setReviser(userId);
        model = noticeReviewService.updateNoticeReview(model);
        if (model == null)
            return R.error("修改失败");
        return R.ok("修改成功", fm2(model));
    }

    @DeleteMapping("/noticeReview/{id}")
    @ApiOperation(value = "通知公告评论点赞删除")
    public Object deleteNoticeReview(@PathVariable("id") String id,
                                     @RequestHeader("token") String token) {

        if (!noticeReviewService.existId(id))
            return R.error("Id数据异常");

        if (noticeReviewService.deleteNoticeReview(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/noticeReview/{index}-{size}-{key}")
    @ApiOperation(value = "读取通知公告评论点赞分页列表")
    public Object getNoticeReview(@PathVariable("index") int index,
                                  @PathVariable("size") int size,
                                  @PathVariable("key") String key,
                                  @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(noticeReviewService.page(index, size, wKey));

    }

    @GetMapping("/noticeReview")
    @ApiOperation(value = "读取通知公告评论点赞所有列表")
    public Object getAllNoticeReview(@RequestHeader("token") String token) {

        return R.ok(noticeReviewService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("noticeId", "请输入通知公告id");
        verify.put("number", "请输入评论者学号/工号");
        verify.put("reviewTime", "请输入评论时间");
        verify.put("content", "请输入内容");
        verify.put("star", "请输入评论星级");
        verify.put("status", "请输入点赞状态");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("noticeId", "请输入通知公告id");
        verify.put("number", "请输入评论者学号/工号");
        verify.put("reviewTime", "请输入评论时间");
        verify.put("content", "请输入内容");
        verify.put("star", "请输入评论星级");
        verify.put("status", "请输入点赞状态");
        return verify;
    }

}
