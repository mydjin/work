package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.Note;
import com.xtaller.party.core.service.impl.AcademyInfoService;
import com.xtaller.party.core.service.impl.NoteService;
import com.xtaller.party.core.service.impl.UserBaseInfoService;
import com.xtaller.party.doc.NoteCreate;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Created by party on 2018/08/17
 */
@Api(value = "07_短信管理")
@RestController
@RequestMapping("/v1/base")
public class NoteApi extends BaseApi {
    @Autowired
    private NoteService noteService;
    @Autowired
    private AcademyInfoService academyInfoService;
    @Autowired
    private UserBaseInfoService userBaseInfoService;

    @PostMapping("/note")
    @ApiOperation(value = "短信推送")
    public Object createNote(@RequestBody NoteCreate object,
                             @RequestHeader("token") String token) {

        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        //根据发布范围添加学生关系
        Date date = new Date();
        cacheKit.setVal("send-Task-" + date.getTime(), fm2(object).toJSONString(), 0);

        return R.ok("开始推送，请稍候！", "send-Task-" + date.getTime());

    }

    @PostMapping("/note/send/{taskid}")
    @ApiOperation(value = "短信推送")
    public Object sendNote(@PathVariable("taskid") String taskid,
                           @RequestHeader("token") String token) {

        //数据校验
        JSONObject sendObj = JSON.parseObject(cacheKit.getVal(taskid));
        NoteCreate object = JSON.toJavaObject(sendObj, NoteCreate.class);
        cacheKit.deleteVal(taskid);


        JSONObject check = V.checkEmpty(verify(), object);
        int sum = 0;
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        //根据发布范围添加学生关系
        JSONArray rangeArray = JSON.parseArray(object.getRange());
        List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray);
        for (JSONObject number : checkNumbers) {
            StringBuffer content = new StringBuffer();
            content.append(object.getDetail());
            if (customService.sendNote(number.getString("number"), object.getTheme(), content.toString(), 0, token)) {
                sum++;
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        return R.ok("推送成功:" + sum + "人");

    }


    @GetMapping("/note/{index}-{size}-{key}")
    @ApiOperation(value = "读取短信分页列表")
    public Object getNote(@PathVariable("index") int index,
                          @PathVariable("size") int size,
                          @PathVariable("key") String key,
                          @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            wKey = S.apppend(" and phone like '%", key, "%' ");
        return R.ok(noteService.page(index, size, wKey));

    }

    @GetMapping("/note")
    @ApiOperation(value = "读取短信所有列表")
    public Object getAllNote(@RequestHeader("token") String token) {

        return R.ok(noteService.queryAll(""));

    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("theme", "请输入短信主题");
        verify.put("detail", "请输入短信内容");
        verify.put("range", "请输入推送范围");
        return verify;
    }


}
