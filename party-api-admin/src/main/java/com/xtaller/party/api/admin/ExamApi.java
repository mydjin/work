package com.xtaller.party.api.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtaller.party.api.BaseApi;
import com.xtaller.party.core.model.*;
import com.xtaller.party.core.service.impl.*;
import com.xtaller.party.doc.ExamCreate;
import com.xtaller.party.doc.ExamUserImport;
import com.xtaller.party.doc.ExamUpdate;
import com.xtaller.party.utils.bean.Where;
import com.xtaller.party.utils.convert.R;
import com.xtaller.party.utils.convert.S;
import com.xtaller.party.utils.convert.V;
import com.xtaller.party.utils.convert.W;
import com.xtaller.party.utils.kit.TimeKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Created by party on 2018/08/29
 */
@Api(value = "36_试卷管理")
@RestController
@RequestMapping("/v1/base")
public class ExamApi extends BaseApi {
    @Autowired
    private ExamService examService;
    @Autowired
    private AcademyInfoService academyInfoService;
    @Autowired
    private ExamCandidateService examCandidateService;
    @Autowired
    private ExamRecordService examRecordService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private ExamQuestionService examQuestionService;


    @PostMapping("/exam")
    @ApiOperation(value = "试卷新增")
    public Object createExam(@RequestBody ExamCreate object,
                             @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);

        //映射对象
        Exam model = o2c(object, token, Exam.class);
        //数据校验
        JSONObject check = V.checkEmpty(verify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));

        model.setStatus(0);

        double single = Double.parseDouble(object.getSingle());
        double gap = Double.parseDouble(object.getGap());
        double multiple = Double.parseDouble(object.getMultiple());
        double trueOrFalse = Double.parseDouble(object.getTrueOrFalse());
        double score = Double.parseDouble(object.getScore());

        if (single + gap + multiple + trueOrFalse == 0 || score == 0) {
            return R.error("总分值不能为0");
        }
        Boolean exist = examService.exist(W.f(
                W.and("name", "eq", model.getName()),
                W.and("isDel", "eq", "0"))
        );
        if (exist)
            return R.error("该试卷名称已存在，请更换！");

        if (V.isEmpty(model.getStartTime()) || V.isEmpty(model.getEndTime())) {
            return R.error("未设置开始时间和结束时间！");
        }

        Long now = TimeKit.getTimestamp();

        if (model.getStartTime() > model.getEndTime()) {
            return R.error("开始时间应小于结束时间");
        }

        if (model.getEndTime() <= now) {
            return R.error("结束时间应大于现在时间");
        }

        model.setIsMake(1);

        model = examService.createExam(model);
        if (model == null) {

            return R.error("新增失败");

        } else {

            return R.ok("新增成功请去组卷", fm2(model));
        }
    }

    @PutMapping("/exam")
    @ApiOperation(value = "修改试卷")
    public Object updateExam(@RequestBody ExamUpdate object,
                             @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        //映射对象
        Exam model = o2c(object, token, Exam.class);
        //数据校验
        JSONObject check = V.checkEmpty(updateVerify(), object);
        if (check.getBoolean("check"))
            return R.error(check.getString("message"));
        Exam data = examService.selectById(model.getId());
        if (data == null) {
            return R.error("该信息不存在，无法修改");
        }
        if (!data.getName().equals(model.getName())) {
            Boolean exist = examService.exist(W.f(
                    W.and("name", "eq", model.getName()),
                    W.and("isDel", "eq", "0"))
            );
            if (exist)
                return R.error("该标题已存在请更换！");
        }

        if (V.isEmpty(model.getStartTime()) || V.isEmpty(model.getEndTime())) {
            return R.error("未设置开始时间和结束时间！");
        }

        Long now = TimeKit.getTimestamp();

        if (model.getStartTime() > model.getEndTime()) {
            return R.error("开始时间应小于结束时间");
        }

        if (model.getEndTime() <= now) {
            return R.error("结束时间应大于现在时间");
        }


        model.setReviser(userId);
        model = examService.updateExam(model);

        if (model == null) {

            return R.error("修改失败");

        } else {

            return R.ok("修改成功", fm2(model));
        }
    }

    @DeleteMapping("/exam/{id}")
    @ApiOperation(value = "试卷删除")
    public Object deleteExam(@PathVariable("id") String id,
                             @RequestHeader("token") String token) {

        if (!examService.existId(id))
            return R.error("Id数据异常");

        if (examService.deleteExam(id, cacheKit.getUserId(token)))
            return R.ok("删除成功");
        return R.error("删除失败");
    }

    @GetMapping("/exam/{index}-{size}-{key}")
    @ApiOperation(value = "读取试卷分页列表")
    public Object getExam(@PathVariable("index") int index,
                          @PathVariable("size") int size,
                          @PathVariable("key") String key,
                          @RequestHeader("token") String token) {

        String wKey = "";
        if (!V.isEmpty(key))
            //FIXME 修改id为需要查询的字段
            wKey = S.apppend(" and id like '%", key, "%' ");
        return R.ok(examService.page(index, size, wKey));

    }

    @GetMapping("/exam/all/{status}")
    @ApiOperation(value = "根据状态读取试卷所有列表")
    public Object getAllExamByStatus(@PathVariable("status") String status,
                                     @RequestHeader("token") String token) {

        //若有status则说明不是全查，发布后的试卷不再重新组卷
        String where = "";
        if (!V.isEmpty(status)) {
            where += " and releaseStatus = " + status + " ";
        }
        return R.ok(examService.queryAll(where));

    }

    @GetMapping("/exam/import/selectList")
    @ApiOperation(value = "获取可导入试卷列表")
    public Object getImportExamList(@RequestHeader("token") String token) {

        String where = " and status !=2 ";

        return R.ok(examService.queryAll(where));
    }

    @GetMapping("/exam/ready/selectList")
    @ApiOperation(value = "获取未开始试卷列表")
    public Object getReadyExamList(@RequestHeader("token") String token) {

        String where = " and status =0 ";

        return R.ok(examService.queryAll(where));
    }


    @GetMapping("/exam/all/selectList")
    @ApiOperation(value = "获取全部试卷列表")
    public Object getAllExamList(@RequestHeader("token") String token) {


        return R.ok(examService.queryAll(""));
    }

    @GetMapping("/exam/info/{id}")
    @ApiOperation(value = "读取试卷信息")
    public Object getExamById(@PathVariable("id") String id,
                              @RequestHeader("token") String token) {


        if (!V.isEmpty(id))
            return R.ok(examService.queryAll(" and id = '" + id + "' "));

        return R.ok(examService.queryAll(""));

    }


    @PutMapping("/exam/release/{id}")
    @ApiOperation(value = "发布试卷")
    public Object releaseExam(@PathVariable("id") String id,
                              @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        List<Exam> examList = examService.findByIds(id);
        if (examList == null || examList.size() == 0)
            return R.error("id数据异常");

        Exam exam = examList.get(0);

      Boolean examQuestions = examQuestionService.exist(W.f(
                W.and("examId", "eq", exam.getId()),
              W.and("isDel", "eq", 0)

      ));

        if (!examQuestions) {
            return R.error("该试卷还没有题目");
        }

        if (V.isEmpty(exam.getStartTime()) || V.isEmpty(exam.getEndTime())) {
            return R.error("未设置开始时间和结束时间！");
        }

        Long now = TimeKit.getTimestamp();

        if (exam.getStartTime() > exam.getEndTime()) {
            return R.error("开始时间应小于结束时间");
        }

        if (exam.getEndTime() <= now) {
            return R.error("结束时间应大于现在时间");
        }


        exam.setReleaseStatus(2);//2-已发布
        exam.setReviser(userId);
        exam = examService.updateExam(exam);


        if (exam == null) {

            return R.error("发布失败");

        } else {


            return R.ok("正在发布，请稍候！", fm2(exam));
        }

    }


    @PutMapping("/exam/releaseSend/{id}")
    @ApiOperation(value = "发布试卷推送")
    public Object releaseExamSend(@PathVariable("id") String id,
                                  @RequestHeader("token") String token) {

        String userId = getUserIdByCache(token);
        Exam exam = examService.selectById(id);
        if (V.isEmpty(exam))
            return R.error("发布试卷推送异常");

        Boolean examQuestions = examQuestionService.exist(W.f(
                W.and("examId", "eq", exam.getId()),
                W.and("isDel", "eq", 0)

        ));

        if (!examQuestions) {
            return R.error("该试卷还没有题目");
        }

        if (exam.getReleaseStatus() == 2) {
            if(V.isEmpty(exam.getRange())){
                return  R.ok("未设置发布范围，请通过考生导入方式选择考生！");
            }

            JSONArray rangeArray = JSON.parseArray(exam.getRange());
            List<JSONObject> checkNumbers = academyInfoService.queryUserByRange(rangeArray);

            //根据发布范围添加学生关系，默认推送信息提醒
            StringBuffer content = new StringBuffer();
            content.append("#{name}同学你好，“" + exam.getName() + "”已经发布，请尽快参与考试！");

            List<Message> messages = new ArrayList<>();
            List<ExamCandidate> examCandidates = new ArrayList<>();

            for (JSONObject number : checkNumbers) {
                ExamCandidate examStudent = o2c(new ExamCandidate(), token, ExamCandidate.class);
                examStudent.setCreator(userId);
                examStudent.setExamId(exam.getId());
                examStudent.setNumber(number.getString("number"));
                String wKey = "";

                wKey = S.apppend(" and number = '", number.getString("number"), "' and examId = '", exam.getId(), "' ");

                List<JSONObject> checkmodel = examCandidateService.queryAll(wKey);

                if (checkmodel == null || checkmodel.size() <= 0) {
                    examCandidates.add(examStudent);
                } else {
                    examStudent.setId(checkmodel.get(0).getString("id"));
                    examStudent.setReviser(userId);
                    try {
                        examCandidateService.updateExamCandidate(examStudent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Message message = new Message();
                message.setNumber(number.getString("number"));
                message.setTopic("“" + exam.getName() + "”试卷发布通知");
                message.setContent(content.toString());
                message.setType(3);
                message.setServer(pcServer);
                message.setLinkId(exam.getId());
                messages.add(message);

            }
            int result = customService.sendMessages(messages, token);

            if (!V.isEmpty(examCandidates)) {
                int userCount = examCandidateService.insertRows(examCandidates);
            }

            int noteSum = 0, pushSum = 0;
            if (exam.getIsNote() == 2 || exam.getIsPush() == 2)
                // lw 根据发布范围推送消息 18.12.10
                for (JSONObject number : checkNumbers) {
                    Note model = o2c(new Note(), token, Note.class);
                    model.setTheme("在线考试");
                    // lw 对关系表中的已绑定手机号的学生循环推送消息 18.12.4
                    if (exam.getIsNote() == 2) {
                        if (customService.sendNote(number.getString("number"), "在线考试", content.toString(), 0, token)) {
                            noteSum++;
                        }

                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    //lw 对关系表中的已绑定微信号的学生循环推送消息 19.2.23
                    if (exam.getIsPush() == 2) {
                        //fixme:发送微信推送 网址为默认网址
                        if (customService.sendWeixinPush(number.getString("number"), "“" + exam.getName() + "”试卷发布通知", content.toString(), "http://wechat.party.gxun.club/", "info"))
                            pushSum++;
                    }

                }

            String back = "";
            if (exam.getIsNote() == 2 && noteSum > 0) {
                back += "，短信推送" + noteSum + "人";
            }
            if (exam.getIsPush() == 2 && pushSum > 0) {
                back += "，微信推送" + pushSum + "人";

            }

            return R.ok("“" + exam.getName() + "”试卷发布推送成功,已消息推送" + result + "人" + back + "。", fm2(exam));

        } else {
            return R.error("“" + exam.getName() + "”试卷发布推送失败");

        }


    }

    @PutMapping("/exam/sendNote/{id}")
    @ApiOperation(value = "试卷短信推送")
    public Object ExamSendNote(@PathVariable("id") String id,
                                  @RequestHeader("token") String token) {

        Exam exam = examService.selectById(id);
        if (V.isEmpty(exam))
            return R.error("试卷短信推送异常");


        String[] numbers = examCandidateService.queryStudentsByExamId(exam.getId());

        //根据发布范围添加学生关系，默认推送信息提醒
            StringBuffer content = new StringBuffer();
            content.append("#{name}同学你好，“" + exam.getName() + "”已经发布，请尽快参与考试！");


            int noteSum = 0, pushSum = 0;
                // lw 根据发布范围推送消息 18.12.10
                for (String number : numbers) {
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

                exam.setIsNote(2);
                exam=examService.updateExam(exam);


            return R.ok("“" + exam.getName() + "”短信推送成功,已推送" + noteSum  + "人。", fm2(exam));



    }

    @PutMapping("/exam/syncCache/{examid}")
    @ApiOperation(value = "重置试卷缓存")
    @Transactional
    public Object syncExamCache(@PathVariable("examid") String examid,  @RequestHeader("token") String token) {

        cacheKit.deleteVal("exam_info_" + examid);
        cacheKit.deleteVals("exam_question_" +examid);

        cacheKit.deleteVals("examQuestion_type_" + examid+"*");
        cacheKit.deleteVals("exam_question_score_" + examid +"*");

        cacheKit.deleteVals("exam_questionId_options_unans_*");
        cacheKit.deleteVals("exam_questionId_Options_*");
        cacheKit.deleteVals("exam_questionId_trueOptionIds_*");


        return R.ok( "重置试卷缓存成功！");
    }


    @PutMapping("/exam/finish/{examid}")
    @ApiOperation(value = "结束考试")
    @Transactional
    public Object finishExam(@PathVariable("examid") String examid,  @RequestHeader("token") String token) {
        String userId = getUserIdByCache(token);

        Exam exam = examService.selectById(examid);
        if (V.isEmpty(exam))
            return R.error("考试不存在！");
        exam.setEndTime((int)TimeKit.getTimestamp());
        exam.setStatus(2);
        exam.setReviser(userId);
        exam =examService.updateExam(exam);
        if(exam==null){
            return R.error("结束考试失败！");
        }else {
            List<ExamCandidate> examCandidateList = examCandidateService.query(W.f(
                    W.and("examId", "eq", examid),
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


            return R.ok("结束考试成功！");
        }
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
            JSONArray   examQuestionArray = JSONArray.parseArray(tmp_exam_question);
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

    @GetMapping("/exam/grade/{index}-{size}/examid-{examid}/academyCode-{academyCode}/number-{number}")
    @ApiOperation(value = "读取测试结果信息")
    public Object getExamGrade(@PathVariable("index") int index,
                               @PathVariable("size") int size, @PathVariable("examid") String examid,@PathVariable("academyCode") String academyCode,  @PathVariable("number") String number,@RequestHeader("token") String token) {


        String examWhere = "";

        String ubiWhere = "";


        if (!V.isEmpty(examid))
            examWhere += S.apppend(" and examid = '" + examid + "' ");
        if (!V.isEmpty(number))
            examWhere += S.apppend(" and number = '" + number + "' ");
        if (!V.isEmpty(academyCode))
            ubiWhere += S.apppend(" and academyCode = '" + academyCode + "' ");

        return R.ok(examCandidateService.gradePage(index, size, examWhere,ubiWhere));
    }


    @GetMapping("/exam/grade/all/examid-{examid}/academyCode-{academyCode}/number-{number}")
    @ApiOperation(value = "读取全部测试结果信息")
    public Object getAllExamGrade(@PathVariable("examid") String examid,@PathVariable("academyCode") String academyCode,@PathVariable("number") String number, @RequestHeader("token") String token) {

        String where = "";

        String ubiWhere = "";

        if (!V.isEmpty(examid))
            where += S.apppend(" and examid = '" + examid + "' ");
        if (!V.isEmpty(number))
            where += S.apppend(" and number = '" + number + "' ");
        if (!V.isEmpty(academyCode))
            ubiWhere += S.apppend(" and academyCode = '" + academyCode + "' ");

        return R.ok(examCandidateService.queryAllGrade(where,ubiWhere));
    }

    @PutMapping("/exam/unlockStudent/{number}")
    @ApiOperation(value = "解除考生登陆锁")
    @Transactional
    public Object unlockStudent( @PathVariable("number") String number, @RequestHeader("token") String token) {

        cacheKit.deleteVal("verify_" + number + "_count");

        return R.ok( "解除考生登陆锁成功！");
    }

    @PutMapping("/exam/syncStudentStatus/{examid}-{number}")
    @ApiOperation(value = "重置考生状态")
    @Transactional
    public Object syncExamStudentStatus(@PathVariable("examid") String examid, @PathVariable("number") String number, @RequestHeader("token") String token) {

        if (V.isEmpty(examid)||V.isEmpty(number)){
            return R.error("试卷编号或学号不存在！");
        }
        Exam exam = examService.selectById(examid);
        if (V.isEmpty(exam))
            return R.error("考试不存在！");
        Integer status = exam.getStatus();
        if(2==status){
            return R.error("考试已结束，无法重置考生！");

        }

        //删除考试回顾缓存
        cacheKit.deleteVal("exam_candidate_" + examid + "_" +number);

        for (int i = 0; i < 4; i++) {
            cacheKit.deleteVal("examRecord_type_" + examid + "_" + (i + 1)+"_"+number);

        }
        cacheKit.deleteVal("exam_candidate_examInfo_" + examid + "_" + number);

        //删除考试记录
        cacheKit.deleteVal("exam_ans_lock_" + number + "_" + examid);

        cacheKit.deleteVal("examQuestion_AnsTime_" + number + "_" +examid);

        cacheKit.deleteVal("examQuestion_Options_" + examid + "_" + number);

        examService.updateExamCandidate(examid,number);
        examService.deleteExamRecord(examid,number);

        return R.ok( "重置考生状态成功！");
    }


    @PostMapping("/exam/ImportUserSetting")
    @ApiOperation(value = "导入用户配置")

    public Object ImportUserSetting(@RequestBody ExamUserImport object,
                                    @RequestHeader("token") String token) {

        if (object.getExamId() == null || object.getExamId() == "") {
            return R.error("未选择试卷!");
        }
        if (object.getAcademyCode() == null || object.getAcademyCode() == "") {
            return R.error("未选择学院!");
        }

        if (object.getType() == null || object.getType() == 0) {
            return R.error("未选择用户类型!");
        }


        Boolean exist = examService.exist(W.f(
                W.and("id", "eq", object.getExamId()),
                W.and("isDel", "eq", "0"))
        );

        if (!exist)
            return R.error("试卷数据异常");

        String batch = S.getToken();

        cacheKit.setVal(batch + "examId", object.getExamId(), 0);
        cacheKit.setVal(batch + "academyCode", object.getAcademyCode(), 0);
        cacheKit.setVal(batch + "type", object.getType() + "", 0);


        return R.ok("配置成功", batch);

    }


    @PostMapping("/exam/ImportUser/{batch}")
    @ApiOperation(value = "导入用户")
    public Object ImportUser(
            HttpServletRequest req,
            @PathVariable("batch") String batch,
            @RequestHeader("token") String token) {
        if (V.isEmpty(batch)) {
            return R.error("导入批次有误！");
        }


        if (cacheKit.getVal(batch + "examId") == null) {
            return R.error("未选择试卷!");
        }
        if (cacheKit.getVal(batch + "academyCode") == null) {
            return R.error("未选择学院!");
        }
        if (cacheKit.getVal(batch + "type") == null) {
            return R.error("未选择用户类型!");
        }

        String academyCode = cacheKit.getVal(batch + "academyCode");
        String examId = cacheKit.getVal(batch + "examId");
        String roleId = "";//学生角色Id 前端选择
        List<Where> wheres = W.f(
                W.and("name", "eq", "前端用户")
        );
//        String wKey = "";
//        if (!V.isEmpty(name))
//            wKey = S.apppend(" and (name like '%", name, "%')");
        List<SysRole> roles = roleService.query(wheres);
        if (!V.isEmpty(roles)) {
            roleId = roles.get(0).getId();
        } else {
            return R.error("角色配置出错，请联系管理员处理！");
        }

        int type = Integer.parseInt(cacheKit.getVal(batch + "type")); //学生用户类型

        String userId = getUserIdByCache(token);

        String filepath = uploadFile(req);
        if (V.isEmpty(filepath)) {
            return R.error("文件上传失败！");
        }

        JSONObject result = new JSONObject();


        JSONObject obj = new JSONObject();
        obj.put("academyCode", academyCode);
        obj.put("examId", examId);
        obj.put("roleId", roleId);
        obj.put("type", type);
        obj.put("userid", userId);
        obj.put("token", token);
        obj.put("filepath", filepath);

        List<Exam> examList = examService.findByIds(examId);
        if (examList == null || examList.size() == 0)
            return R.error("试卷数据异常！");

        JSONObject res = SolvedFile(obj, batch);
        if (res == null) {
            return R.error("考生导入异常！");
        }

        Boolean flag =res.getBoolean("flag");
        if(!flag){
            return  R.error(res.getString("message"));
        }

        result.put("total", res.getInteger("total"));
        result.put("truecount", res.getInteger("truecount"));
        result.put("errcount", res.getInteger("errcount"));

        if (res.getInteger("truecount") == 0 && res.getInteger("errcount") == 0) {
            return R.error("导入数据为空！");
        }


        if (res.getInteger("errcount") > 0) {
            result.put("errlist", res.getJSONArray("errlist"));
            result.put("success", 0);
        } else {
            result.put("success", 1);

        }


        return R.ok("导入成功", result);
    }

    @PostMapping("/exam/ImportUserStatus/{batch}")
    @ApiOperation(value = "导入用户状态")
    public Object getImportUserStatus(@PathVariable("batch") String batch) {
        if (V.isEmpty(batch)) {
            return R.error("导入批次有误！");
        }

        JSONObject obj = getImportStatusObj(batch);

        if (V.isEmpty(batch)) {
            return R.error("导入状态异常！");
        }

        return R.ok(obj);

    }


    public JSONObject SolvedFile(JSONObject obj, String batch) {
        JSONObject res = new JSONObject();
        JSONArray errors = new JSONArray();
        if (obj.getString("filepath") == null || obj.getString("filepath") == "") {
            res.put("flag",false);
            res.put("message","上传文件有误！");
            return res;
        }
        try {
            File excel = new File(obj.getString("filepath"));
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    wb = new XSSFWorkbook(excel);
                } else {
                    res.put("flag",false);
                    res.put("message","上传文件有误！");
                    return res;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();

                int userCount = 0, trueCount = 0;

                for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行

                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int lastCellIndex = row.getLastCellNum();
                        if ( lastCellIndex != 5&&lastCellIndex != 4 && lastCellIndex != 3) {
                            continue;
                        } else {
                            String myclass="", name="", number="", phone="",room="";
                            if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null) {
                                JSONObject userobj = new JSONObject();
                                name = row.getCell(0).toString().trim();
                                myclass = row.getCell(1).toString().trim();
                                number = row.getCell(2).toString().trim();
                                if (row.getCell(3) != null) {
                                    phone = row.getCell(3).toString().trim();
                                    userobj.put("phone", phone);
                                }
                                if (row.getCell(4) != null) {
                                    room = row.getCell(4).toString().trim();
                                    userobj.put("room", room);
                                }
                                userobj.put("name", name);
                                userobj.put("myClass", myclass);
                                userobj.put("number", number);
                                if (number == null || number == "") {
                                    continue;
                                }

                                userobj.put("academyCode", obj.getString("academyCode"));
                                userobj.put("examId", obj.getString("examId"));
                                userobj.put("roleId", obj.getString("roleId"));
                                userobj.put("type", obj.getInteger("type"));
                                userobj.put("userid", obj.getString("userid"));
                                userobj.put("token", obj.getString("token"));

                                JSONObject examUserObj = examService.ImportUser(userobj);

                                userobj.remove("academyCode");
                                userobj.remove("examId");
                                userobj.remove("roleId");
                                userobj.remove("type");
                                userobj.remove("userid");
                                userobj.remove("token");

                                userCount++;
                                if (examUserObj != null) {
                                    boolean userFlag = examUserObj.getBoolean("flag");
                                    if (!userFlag) {
                                        userobj.put("info", examUserObj.getString("info"));
                                        errors.add(userobj);
                                        cacheKit.setVal(batch + "status", 2 + "", 0);
                                    } else {
                                        trueCount++;
                                        cacheKit.setVal(batch + "status", 1 + "", 0);
                                    }

                                } else {
                                    cacheKit.setVal(batch + "status", 2 + "", 0);
                                    userobj.put("info", "其他原因");
                                    errors.add(userobj);
                                }
                                cacheKit.setVal(batch + "now", rIndex + "", 0);
                            }

                            cacheKit.setVal(batch + "total", lastRowIndex + "", 0);

                            if (rIndex != lastRowIndex) {
                                cacheKit.setVal(batch + "res", 2 + "", 0);
                            }
                        }
                    }


                }

                cacheKit.setVal(batch + "res", 1 + "", 0);

                res.put("truecount", trueCount);

                res.put("errcount", errors.size());

                res.put("total", userCount);

                res.put("errlist", errors);

                res.put("flag",true);

                return res;

            } else {
                res.put("flag", false);
                res.put("message", "上传文件有误！");
                System.out.println("找不到指定的文件");
                return  res;

            }
        } catch (Exception e) {
            e.printStackTrace();
            res.put("flag", false);
            res.put("message", "考生处理出错！");
            return res;

        }
    }

    private Map<String, String> verify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("name", "请输入考试名称");
        verify.put("type", "请输入cd考试类型");
        verify.put("score", "请输入试卷总分值");
        verify.put("trueOrFalse", "请输入判断题总分值");
        verify.put("single", "请输入单选题总分值");
        verify.put("multiple", "请输入多选题总分值");
        verify.put("gap", "请输入填空题总分值");
        verify.put("duration", "请输入考试时长");
        return verify;
    }

    private Map<String, String> updateVerify() {
        Map<String, String> verify = new HashMap<>();
        verify.put("name", "请输入考试名称");
        verify.put("type", "请输入考试类型");
        verify.put("score", "请输入试卷总分值");
        verify.put("trueOrFalse", "请输入判断题总分值");
        verify.put("single", "请输入单选题总分值");
        verify.put("multiple", "请输入多选题总分值");
        verify.put("gap", "请输入填空题总分值");
        verify.put("duration", "请输入考试时长");
        return verify;
    }

}
