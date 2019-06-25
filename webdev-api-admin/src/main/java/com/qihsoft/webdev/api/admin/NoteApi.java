package com.qihsoft.webdev.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.core.service.impl.AcademyInfoService;
import com.qihsoft.webdev.doc.NoteCreate;
import com.qihsoft.webdev.core.service.impl.NoteService;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
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
 * Created by qihsoft on 2018/08/17
 */
@Api(value = "209_短信管理")
@RestController
@RequestMapping("/v1/base")
public class NoteApi extends BaseApi {
    @Autowired
    private NoteService noteService;
    @Autowired
    private AcademyInfoService academyInfoService;


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
        cacheKit.setVal("note-Send-Task-" + date.getTime(), fm2(object).toJSONString(), 0);

        return R.ok("开始推送，请稍候！", "note-Send-Task-" + date.getTime());
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
        List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray, object.getType());
        for (JSONObject number : checkNumbers) {

            JSONObject values = new JSONObject();
            values = customService.getMssageUserByNumber(number.getString("number"));
            if (V.isEmpty(values)) {
                values = customService.getWechatMessageUserByNumber(number.getString("number"));
            }
            if (!V.isEmpty(values)) {
                String phone = values.getString("phone");
                StringBuffer content = new StringBuffer();
                content.append(object.getDetail());
                if (customService.sendNote(phone, number.getString("number"), object.getTheme(), content.toString(), 0, token)) {
                    sum++;
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


        return R.ok("推送成功:" + sum + "人");

    }


    @GetMapping("/note/{index}-{size}-{key}-{number}")
    @ApiOperation(value = "读取短信分页列表")
    public Object getNote(@PathVariable("index") int index,
                          @PathVariable("size") int size,
                          @PathVariable("key") String key,
                          @PathVariable("number") String number,

                          @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            wKey = S.apppend(" and phone like '%", key, "%' ");
        if (!V.isEmpty(number))
            wKey += S.apppend(" and number like '%", number, "%' ");

        return R.ok(noteService.page(index, size, wKey));

    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("theme", "短信主题");
        verify.put("detail", "短信内容");
        verify.put("range", "请输入推送范围");
        verify.put("type", "请输入推送人群");
        return verify;
    }


}
