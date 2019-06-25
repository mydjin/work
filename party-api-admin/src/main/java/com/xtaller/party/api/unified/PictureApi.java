package com.xtaller.party.api.unified;

import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Picture;
import com.xtaller.party.core.service.impl.PictureService;
import com.xtaller.party.core.service.impl.SysTpsConfigService;
import com.xtaller.party.doc.PictureCreate;
import com.xtaller.party.doc.PictureUpdate;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
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
@Api(value = "图片管理")
@RestController
@RequestMapping("/v1/tool")
public class PictureApi extends BaseApi {
    @Autowired
    private PictureService pictureService;


    @PostMapping("/picture")
    @ApiOperation(value = "图片新增")
    public Object createPicture(@RequestBody PictureCreate object,
                                @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String userId = getUserIdByCache(token);

            //映射对象
            Picture model = o2c(object, token, Picture.class);
            //数据校验
            JSONObject check = V.checkEmpty(verify(), object);
            if (check.getBoolean("check"))
                return R.error(check.getString("message"));

            model = pictureService.createPicture(model);
            if (model == null)
                return R.error("新增失败");
            return R.ok("新增成功", fm2(model));
        }
    }

    @PutMapping("/picture")
    @ApiOperation(value = "修改图片")
    public Object updatePicture(@RequestBody PictureUpdate object,
                                @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String userId = getUserIdByCache(token);
            //映射对象
            Picture model = o2c(object, token, Picture.class);
            //数据校验
            JSONObject check = V.checkEmpty(updateVerify(), object);
            if (check.getBoolean("check"))
                return R.error(check.getString("message"));
            model.setReviser(userId);
            model = pictureService.updatePicture(model);
            if (model == null)
                return R.error("修改失败");
            return R.ok("修改成功", fm2(model));
        }
    }

    @DeleteMapping("/picture/{id}")
    @ApiOperation(value = "图片删除")
    public Object deletePicture(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            if (!pictureService.existId(id))
                return R.error("Id数据异常");

            if (pictureService.deletePicture(id, cacheKit.getUserId(token)))
                return R.ok("删除成功");
            return R.error("删除失败");
        }
    }

    @GetMapping("/picture/{index}-{size}-{name}")
    @ApiOperation(value = "读取图片分页列表")
    public Object getPicture(@PathVariable("index") int index,
                             @PathVariable("size") int size,
                             @PathVariable("name") String name,
                             @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String wKey = "";
            if (!V.isEmpty(name))
                //搜索框，通过图片名称查找
                wKey = S.apppend(" and name like '%", name, "%' ");
            return R.ok(pictureService.page(index, size, wKey));
        }
    }

    @GetMapping("/picture")
    @ApiOperation(value = "读取图片所有列表")
    public Object getAllPicture(@RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            return R.ok(pictureService.queryAll(""));
        }
    }


    @GetMapping("/picture/tableId/{tableId}")
    @ApiOperation(value = "读取图片所有列表")
    public Object getAllPicture(@PathVariable("tableId") String tableId,
                                @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            return R.ok(pictureService.queryAll(" and tableId = " + tableId));
        }
    }

    @GetMapping("/getPictureURL/{id}")
    @ApiOperation(value = "获取图片URL")
    public Object getPictureURL(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String wKey = "";
            if (!V.isEmpty(id))
                //搜索框，通过图片名称查找
                wKey = S.apppend(" and id = ", id, " ");
            return R.ok(pictureService.getPictureURL(wKey));
        }
    }

    @GetMapping("/savePictureURL/{url}")
    @ApiOperation(value = "获取图片URL")
    public Object savePictureURL(@PathVariable("url") String url,
                                 @RequestHeader("token") String token) {

        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {

            return R.ok(pictureService.getPictureURL(""));
        }
    }


    @GetMapping("/checkPicture/{id}")
    @ApiOperation(value = "校验图片标识码")
    public Object checkPicture(@PathVariable("id") String id,
                               @RequestHeader("token") String token) {
        if (token == null || token == "") {
            return R.error("授权失效，请重新登陆");
        } else {
            String wKey = "";
            if (!V.isEmpty(id))
                //通过图片MD5查找
                wKey = S.apppend(" and code = '", id, "' ");
            List<JSONObject> list = pictureService.checkPicture(wKey);
            // System.out.println("校验md5"+list);
            JSONObject pic = new JSONObject();
            if (list.size() == 1) {
                pic = list.get(0);
                //  System.out.println(pic);
            }

            return R.ok(pic);
        }
    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("name", "缺少图片名称");
        verify.put("artworkURL", "缺少图片URL");
        verify.put("path", "缺少图片路径");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("name", "缺少图片名称");
        verify.put("artworkURL", "缺少图片URL");
        verify.put("path", "缺少图片路径");
        return verify;
    }


}
