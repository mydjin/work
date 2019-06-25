package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.ExamRoomCreate;
import com.xtaller.party.doc.ExamRoomUpdate;
import com.xtaller.party.utils.bean.Page;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Created by party on 2019/05/28
 */
@Api(value = "56_考场管理")
@RestController
@RequestMapping("/v1/base")
public class ExamRoomApi extends BaseApi {
    @Autowired
    private ExamRoomService examRoomService;
    @Autowired
    private ExamCandidateService examCandidateService;
    @Autowired
    private ExamRecordService examRecordService;
    @Autowired
    private ExamQuestionService examQuestionService;

    @PostMapping("/examRoom")
    @ApiOperation(value = "考场新增")
    public Object createExamRoom(@RequestBody ExamRoomCreate object,
                              @RequestHeader("token") String token){

        //映射对象
        ExamRoom model = o2c(object,token, ExamRoom.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(),object);
        if(check.getBoolean("check"))
        return R.error(check.getString("message"));

        if(model.getIsUnseal()==0){
            model.setUnsealCode("");
        }else if(model.getIsUnseal()==1){
            if(V.isEmpty(model.getUnsealCode())){
                return  R.error("请输入启封码");
            }
        }

        Boolean exist = examRoomService.exist(W.f(
                W.and("examId","eq",model.getExamId()),
                W.and("name","eq",model.getName()),
                W.and("isDel","eq","0"))
        );
        if(exist)
            return R.error("考场名称已经存在，请更换一个名称");

        model = examRoomService.createExamRoom(model);
        if(model == null)
            return R.error("新增失败");
        return R.ok("新增成功",fm2(model));
    }

   @PutMapping("/examRoom")
   @ApiOperation(value = "修改考场")
   public Object updateExamRoom(@RequestBody ExamRoomUpdate object,
                             @RequestHeader("token") String token){
        String userId = getUserIdByCache(token);
        //映射对象
        ExamRoom model = o2c(object,token, ExamRoom.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(),object);
        if(check.getBoolean("check"))
            return R.error(check.getString("message"));
       JSONObject data= examRoomService.queryInfo(model.getId());

       if(data==null){
           return R.error("该信息不存在，无法修改");
       }

       Integer examStatus =data.getInteger("examStatus");
       if(V.isEmpty(examStatus)){
           return R.error("考试状态出错");
       }
       if(examStatus==1){
           return R.error("考试已经开始，无法进行操作！");
       }
       if(examStatus==2){
           return R.error("考试已经结束，无法进行操作！");
       }
       if(!data.getString("name").equals(model.getName())) {
               Boolean exist = examRoomService.exist(W.f(
                       W.and("examId", "eq", data.getString("examId")),
                       W.and("name", "eq", model.getName()),
                       W.and("isDel", "eq", "0"))
               );
               if (exist)
                   return R.error("考场名称已经存在，请更换一个名称");
           }
       if(model.getIsUnseal()==0){
           model.setUnsealCode("");
       }else if(model.getIsUnseal()==1){
           if(V.isEmpty(model.getUnsealCode())){
               return  R.error("请输入启封码");
           }
       }
        model.setReviser(userId);
        model = examRoomService.updateExamRoom(model);
        if(model == null)
            return R.error("修改失败");
        return R.ok("修改成功",fm2(model));
    }

    @DeleteMapping("/examRoom/{id}")
    @ApiOperation(value = "考场删除")
    public Object deleteExamRoom(@PathVariable("id") String id,
                                                  @RequestHeader("token") String token){

        JSONObject data= examRoomService.queryInfo(id);

        if(data==null){
            return R.error("该信息不存在，无法修改");
        }
        Integer examStatus =data.getInteger("examStatus");
        if(V.isEmpty(examStatus)){
            return R.error("考试状态出错");
        }
        if(examStatus==1){
            return R.error("考试已经开始，无法进行操作！");
        }
        if(examStatus==2){
            return R.error("考试已经结束，无法进行操作！");
        }

        if(examRoomService.deleteExamRoom(id, cacheKit.getUserId(token))) {
           examRoomService.deleteStudentByRoomId(id);


            return R.ok("删除成功");
        }else {
            return R.error("删除失败");
        }
    }

    @GetMapping("/examRoom/{index}-{size}-{examId}-{monitor}-{roomName}-{roomStatus}")
    @ApiOperation(value = "读取考场分页列表")
    public Object getExamRoom(@PathVariable("index") int index,
                              @PathVariable("size") int size,
                              @PathVariable("examId") String examId,
                              @PathVariable("monitor") String monitor,
                              @PathVariable("roomName") String roomName,
                              @PathVariable("roomStatus") String roomStatus,
                              @RequestHeader("token") String token) {
        String wKey = "";
        if (!V.isEmpty(examId))
            wKey += S.apppend(" and examId = '", examId, "' ");
        if (!V.isEmpty(monitor))
            wKey += S.apppend(" and monitor like '%", monitor, "%' ");
        if (!V.isEmpty(roomName))
            wKey += S.apppend(" and name like '%", roomName, "%' ");
        if (!V.isEmpty(roomStatus))
            wKey += S.apppend(" and status = '", roomStatus, "' ");
        return R.ok(examRoomService.page(index, size, wKey));
    }

    @PutMapping("/examRoom/finish/{roomid}")
    @ApiOperation(value = "结束考试")
    @Transactional
    public Object finishExam(@PathVariable("roomid") String roomid,  @RequestHeader("token") String token) {
        String userId = getUserIdByCache(token);
        String name = getName(token);
        String number = getNumberByCache(token);

        JSONObject data= examRoomService.queryInfo(roomid);

        if(data==null){
            return R.error("该信息不存在，无法修改");
        }

        Integer status =data.getInteger("status");
        if(!V.isEmpty( status )){
            if( status ==2){
                return R.ok("该考场已结束考试！");
            }
        }


        Integer examStatus =data.getInteger("examStatus");
        if(V.isEmpty(examStatus)){
            return R.error("考试状态出错");
        }
        if(examStatus!=1){
            return R.error("考试未处于进行中状态，无法结束考试！");
        }
        ExamRoom model = o2c(data,token, ExamRoom.class);

        List<ExamCandidate> examCandidateList = examCandidateService.query(W.f(
                    W.and("roomId", "eq", roomid),
                    W.and("status", "eq", 1),
                    W.and("isDel", "eq",0)
            ));
            for (ExamCandidate examCandidate :examCandidateList){
                examCandidate.setRemark("未按时作答考试结束");
                examCandidate.setReviser(userId);
                examCandidate.setScore("0");
                examCandidate.setStatus(2);
                closeExamCandidate(examCandidate);
            }

            model.setStatus(1);
            model.setRemark(""+name+"("+number+")结束考试！");
            examRoomService.updateExamRoom(model);

            return R.ok("结束考试成功！");

    }

    @PutMapping("/examRoom/sendNote/{roomid}")
    @ApiOperation(value = "发送短信")
    @Transactional
    public Object SendNote(@PathVariable("roomid") String roomid,  @RequestHeader("token") String token) {
        String[] numbers = examRoomService.queryStudentsByRoomId(roomid);
        JSONObject data= examRoomService.queryInfo(roomid);

        if(data==null){
            return R.error("该信息不存在，无法修改");
        }

        Integer examStatus =data.getInteger("examStatus");
        if(V.isEmpty(examStatus)){
            return R.error("考试状态出错");
        }


        StringBuffer content = new StringBuffer();
        content.append("#{name}同学你好，“" + data.getString("examName") + "”已经发布，请尽快参与考试！");
        int noteSum = 0;

        for (String number :numbers){

            Note model = o2c(new Note(), token, Note.class);
            model.setTheme("在线考试");

            //lw 对关系表中的已绑定手机号的学生循环推送消息 18.12.4
            if (customService.sendNote(number, "在线考试", content.toString(), 0, token)) {
                noteSum++;
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        return R.ok("“" +data.getString("examName") + "”短信推送成功,已推送" + noteSum  + "人。");


    }


    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("examId", "请输入试卷Id");
        verify.put("name", "请输入名称");
       // verify.put("monitor", "请输入监考员");
        verify.put("isUnseal", "请输入是否设定启封码");
      //  verify.put("unsealCode", "请输入启封码");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
       // verify.put("examId", "请输入试卷Id");
        verify.put("name", "请输入名称");
       // verify.put("monitor", "请输入监考员");
        verify.put("isUnseal", "请输入是否设定启封码");
      //  verify.put("unsealCode", "请输入启封码");
        return verify;
    }

    public Boolean closeExamCandidate(ExamCandidate model) {

        String[] examQuestions = null;
        String examId = model.getExamId();
        String number = model.getNumber();
        if(V.isEmpty(examId)||V.isEmpty(number)){
            return  false;
        }

        List<ExamRecord> examRecords = new ArrayList<>();

        String tmp_exam_question = cacheKit.getVal("exam_question_" + examId);

        if (!V.isEmpty(tmp_exam_question)) {
            JSONArray examQuestionArray = JSONArray.parseArray(tmp_exam_question);
            examQuestions = new String[examQuestionArray.size()];
            examQuestions = examQuestionArray.toArray(examQuestions);

        } else {
            examQuestions = examQuestionService.queryPCQuestionIds(examId);
            cacheKit.setVal("exam_question_" + examId, JSONObject.toJSONString(examQuestions), 1800);
        }

        for (int i = 0; examQuestions != null && i < examQuestions.length; i++) {
            ExamRecord examRecord = new ExamRecord();
            examRecord.setExamId(examId);
            examRecord.setNumber(number);
            examRecord.setQuestionId(examQuestions[i]);
            examRecord.setSituation(2);
            examRecord.setAnswerContent("");
            examRecords.add(examRecord);

        }

        //记录答题
        try {
            if (!V.isEmpty(examRecords)) {
                int userCount = examRecordService.insertRows(examRecords, model.getCreator());
            }
            model.setScore("0");
            model.setStatus(2);

            model = examCandidateService.updateExamCandidate(model);
            if (!V.isEmpty(model)) {
                cacheKit.deleteVal("examQuestion_AnsTime_" + number + "_" + examId);
                cacheKit.deleteVal("examQuestion_Options_" + examId+"_"+number);
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
