package com.qihsoft.webdev.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qihsoft.webdev.api.BaseApi;
import com.qihsoft.webdev.doc.MessageCreate;
import com.qihsoft.webdev.core.model.Message;
import com.qihsoft.webdev.core.model.MessageUser;
import com.qihsoft.webdev.core.service.impl.AcademyInfoService;
import com.qihsoft.webdev.core.service.impl.MessageService;
import com.qihsoft.webdev.core.service.impl.MessageUserService;
import com.qihsoft.webdev.utils.convert.R;
import com.qihsoft.webdev.utils.convert.S;
import com.qihsoft.webdev.utils.convert.V;
import com.qihsoft.webdev.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by qihsoft on 2018/12/08
 */
@Api(value = "210_消息提醒管理")
@RestController
@RequestMapping("/v1/base")
public class MessageApi extends BaseApi {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageUserService messageUserService;
    @Autowired
    private AcademyInfoService academyInfoService;


    @PostMapping("/message")
    @ApiOperation(value = "消息提醒推送任务")
    public Object createMessage(@RequestBody MessageCreate object,
                                @RequestHeader("token") String token) {

        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        //根据发布范围添加学生关系
        Date date = new Date();
        cacheKit.setVal("message-Send-Task-" + date.getTime(), fm2(object).toJSONString(), 0);

        return R.ok("开始推送，请稍候！", "message-Send-Task-" + date.getTime());

    }

    @PostMapping("/message/send/{taskid}")
    @ApiOperation(value = "消息提醒推送")
    public Object sendMessage(@PathVariable("taskid") String taskid,
                              @RequestHeader("token") String token) {
        //数据校验
        JSONObject sendObj = JSON.parseObject(cacheKit.getVal(taskid));
        MessageCreate object = JSON.toJavaObject(sendObj, MessageCreate.class);
        cacheKit.deleteVal(taskid);

        //根据发布范围进行推送
        JSONArray rangeArray = JSON.parseArray(object.getRange());
        List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray, object.getType());
        List<Message> messages = new ArrayList<>();

        for (JSONObject number : checkNumbers) {

            Message message = new Message();
            message.setNumber(number.getString("number"));
            message.setTopic(object.getTopic());
            message.setContent(object.getContent());
            message.setType(0);
            message.setServer(object.getServer());
            messages.add(message);
        }
        int result = customService.sendMessages(messages, token);

        return R.ok("推送成功" + result + "条信息");

    }


    @GetMapping("/message/{index}-{size}-{key}-{server}")
    @ApiOperation(value = "读取消息提醒用户分页列表")
    public Object getMessage(@PathVariable("index") int index,
                             @PathVariable("size") int size,
                             @PathVariable("key") String key,
                             @PathVariable("server") String server,
                             @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            wKey += S.apppend(" and number like '%", key, "%' ");
        if (!V.isEmpty(server))
            wKey += S.apppend(" and server = '", server, "' ");

        return R.ok(messageService.page(index, size, wKey));

    }


    @GetMapping("/message/user/info/{id}")
    @ApiOperation(value = "读取消息提醒")
    public Object getMessageById(@PathVariable("id") String id,
                                 @RequestHeader("token") String token) {

        String number = getNumberByCache(token);

        return R.ok(fm2(messageService.getById(id, number)));


    }

    @DeleteMapping("/message/{id}")
    @ApiOperation(value = "消息提醒删除")
    public Object deleteMessage(@PathVariable("id") String id,
                                @RequestHeader("token") String token) {
        if (!messageService.existId(id))
            return R.error("Id数据异常");

        if (messageService.deleteMessage(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");

    }


    @PutMapping("/message/user/read/{id}")
    @ApiOperation(value = "消息提醒用户关系更新")
    public Object createMessageUser(@PathVariable("id") String id,
                                    @RequestHeader("token") String token) {
        String number = getNumberByCache(token);
        String wKey = "";
        wKey = S.apppend(" and number = '", number, "' and messageId = '", id, "' ");

        List<JSONObject> obj = messageUserService.queryAll(wKey);
        if (obj.size() > 0) {
            return R.error("阅读状态新增失败");
        } else {
            //映射对象
            MessageUser model = o2c(new MessageUser(), token, MessageUser.class);
            model.setNumber(number);
            model.setMessageId(id);
            model.setReadStatus(1);
            model.setReadTime((int) TimeKit.getTimestamp());
            model = messageUserService.createMessageUser(model);
            if (model == null)
                return R.error("阅读状态新增失败");
            return R.ok("阅读状态新增成功", fm2(model));
        }

    }


    @GetMapping("/message/user/{index}-{size}-{key}")
    @ApiOperation(value = "读取消息提醒用户分页列表")
    public Object getUserMessages(@PathVariable("index") int index,
                                  @PathVariable("size") int size,
                                  @PathVariable("key") String key,
                                  @RequestHeader("token") String token) {

        String number = getNumberByCache(token);
        String wKey = S.apppend("and server = 0 ");


        return R.ok(messageService.page_user(index, size, wKey, number));

    }


    @GetMapping("/message/user/index")
    @ApiOperation(value = "读取首页消息提醒")
    public Object getIndex(@RequestHeader("token") String token) {

        String number = getNumberByCache(token);
        String wKey = "";
        wKey = S.apppend("and server = 0 ");
        return R.ok(messageService.getIndex_user(wKey, number));
    }


    @GetMapping("/message/user/page/{id}")
    @ApiOperation(value = "读取消息上下条")
    public Object getPageById(@PathVariable("id") String id,
                              @RequestHeader("token") String token) {
        String number = getNumberByCache(token);
        JSONObject pageObj = new JSONObject();
        String wKey = "";
        wKey = S.apppend("and server = 0 ");
        List<JSONObject> messageList = messageService.queryIdName(wKey, number);
        if (messageList != null) {
            int i = 1;
            int local = 0;
            for (JSONObject message : messageList) {
                int start = 0, end = 0;
                if (i == 1) {
                    start = 1;
                }
                if (messageList.size() == i) {
                    end = 1;
                }
                if (message.getString("id").equals(id)) {
                    local = i - 1;
                }
                message.put("start", start);
                message.put("end", end);
                i++;
            }

            if (local != 0) {
                pageObj.put("pageUp", messageList.get(local - 1));
            }
            if (local != messageList.size() - 1) {
                pageObj.put("pageDown", messageList.get(local + 1));
            }
            pageObj.put("pageCurrent", messageList.get(local));
        }
        return R.ok(pageObj);


    }

    private Map verify() {
        Map verify = new HashMap<>();
        verify.put("topic", "请输入主题");
        verify.put("content", "请输入内容");
        verify.put("server", "请输入目的服务器");
        verify.put("range", "请输入推送范围");
        verify.put("type", "请输入推送人群");

        return verify;
    }


}
