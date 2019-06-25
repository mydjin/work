package com.qihsoft.webdev.api.unified;

import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.core.model.Video;
import com.qihsoft.webdev.core.service.impl.VideoService;
import com.qihsoft.webdev.doc.VideoCreate;
import com.qihsoft.webdev.doc.VideoUpdate;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by party on 2018/08/29
 */
@Api(value = "视频管理")
@RestController
@RequestMapping("/v1/tool")
public class VideoApi extends BaseApi {
    @Autowired
    private VideoService videoService;

    @PostMapping("/video")
    @ApiOperation(value = "视频新增")
    public Object createVideo(@RequestBody VideoCreate object,
                              @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String userId = getUserIdByCache(token);

            //映射对象
            Video model = o2c(object, token, Video.class);
            //数据校验
            JSONObject check = V.checkEmpty(verify(), object);
            if (check.getBoolean("check"))
                return R.error(check.getString("message"));


            if (videoService.deleteBySec(model.getSectionId())) {
                model = videoService.createVideo(model);
                if (model == null)
                    return R.error("新增失败");
                return R.ok("新增成功", fm2(model));
            } else {
                return R.error("新增失败");
            }

        }
    }

    @PutMapping("/video")
    @ApiOperation(value = "修改视频")
    public Object updateVideo(@RequestBody VideoUpdate object,
                              @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String userId = getUserIdByCache(token);
            //映射对象
            Video model = o2c(object, token, Video.class);
            //数据校验
            JSONObject check = V.checkEmpty(updateVerify(), object);
            if (check.getBoolean("check"))
                return R.error(check.getString("message"));
            model.setReviser(userId);
            model = videoService.updateVideo(model);
            if (model == null)
                return R.error("修改失败");
            return R.ok("修改成功", fm2(model));
        }
    }

    @DeleteMapping("/video/{id}")
    @ApiOperation(value = "视频删除")
    public Object deleteVideo(@PathVariable("id") String id,
                              @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            if (!videoService.existId(id))
                return R.error("Id数据异常");

            if (videoService.deleteVideo(id, cacheKit.getUserId(token)))
                return R.ok("删除成功");
            return R.error("删除失败");
        }
    }

    @DeleteMapping("/video/sec-{id}")
    @ApiOperation(value = "视频删除")
    public Object deleteVideo_Bysec(@PathVariable("id") String id,
                                    @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {

            videoService.deleteBySec(id);

            //  if(!videoService.existId(id))
            //    return R.error("Id数据异常");

            // if(videoService.deleteVideo(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
            //  return R.error("删除失败");
        }
    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("sectionId", "请输入节内容id");
//        verify.put("name", "请输入名称");
        verify.put("url", "请输入地址");
//        verify.put("source", "请输入来源");
//        verify.put("describe", "请输入描述");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("sectionId", "请输入节内容id");
//        verify.put("name", "请输入名称");
        verify.put("url", "请输入地址");
//        verify.put("source", "请输入来源");
//        verify.put("describe", "请输入描述");
        return verify;
    }

    @GetMapping("/checkVideo/{id}")
    @ApiOperation(value = "校验视频Md5")
    public Object checkPicture(@PathVariable("id") String id,
                               @RequestHeader("token") String token) {

        List<JSONObject> list = videoService.checkVideo(id);
        // System.out.println("校验md5"+list);
        JSONObject pic = new JSONObject();
        if (list.size() == 1) {
            pic = list.get(0);
            //  System.out.println(pic);
        }

        return R.ok(pic);
    }
}
