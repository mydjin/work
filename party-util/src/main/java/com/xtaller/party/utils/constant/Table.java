package com.xtaller.party.utils.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taller on 2017/8/23.
 */
public class Table {
    public static Map<String,Integer> BUSINESS = getTable();

    private static Map<String,Integer> getTable(){
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("sys_user", 100);//系统用户表
        map.put("sys_user_token",102);//用户Token关联表
        map.put("sys_user_role",103);//用户对应角色关联表
        map.put("sys_user_resources",104);//用户资源文件
        map.put("sys_user_department",105);//用户部门所属关系

        map.put("sys_role",110);//角色表
        map.put("sys_role_menu",111);//角色菜单表
        map.put("sys_role_auth",112);//角色菜单权限表
        map.put("sys_category",120);//类型表

        map.put("sys_menu", 150);//权限菜单表
        map.put("sys_menu_auth", 151);//权限菜单按钮权限表

        map.put("sys_subsystem",198);//子系统
        map.put("sys_auto_code",199);//代码生成记录

        map.put("sys_tps_config",200);//系统第三方配置项
        map.put("sys_global_config",201);//系统基本配置
        map.put("sys_operation_record",202);//系统操作记录


        map.put("article_type",206);//文章类型表
        map.put("article_topic",207);//文章主题表
        map.put("article",208);//文章信息表
        map.put("depart",209);//组织机构信息表
        map.put("core_course",210);//课程信息表


        map.put("user_base_info",301);//用户基础信息表

        map.put("user_detail_info",302);//用户详细信息表

        map.put("note",303);//短信表

        map.put("nation_info",304);//民族信息表

        map.put("academy_info",305);//学院信息表

        map.put("major_info",306);//专业信息表

        map.put("branch",307);//党支部表

        map.put("dictionary",308);//数据字典表

        map.put("template",309);//模版管理表

        map.put("growth_record",310);//成长记录表

        map.put("application",311);//入党申请书表

        map.put("report",312);//思想汇报表

        map.put("schedule",313);//学习安排表

        map.put("schedule_student",314);//学习与学生关系表

        map.put("attendance",315);//考勤表

        map.put("section_user",316);//节内容与用户关系表

        map.put("vacate",317);//请假表

        map.put("notice",318);//通知公告表

        map.put("section",319);//网络课程节内容表

        map.put("picture",320);//图片表

        map.put("notice_review",322);//通知公告评论点赞表

        map.put("video",323);//视频表

        map.put("online_course",324);//网络课程表

        map.put("online_course_user",325);//网络课程与用户关系表

        map.put("chapter",326);//网络课程章目录表

        map.put("chapter_user",327);//章目录与用户关系表

        map.put("experience",328);//本人经历表

        map.put("household_society",329);//家庭和社会成员表

        map.put("survey_record_chat",330);//考察与谈话记录表

        map.put("question",331);//题库表

        map.put("approval",332);//审核信息表

        map.put("options",333);//选项表

        map.put("party_lecture_info",334);//党课结业信息表

        map.put("reward_punishment",335);//奖励和处分表

        map.put("stage_review",336);//阶段考察表

        map.put("link_user",337);//培养联系人表

        map.put("activist_check_info",338);//确定为入党积极分子情况表

        map.put("activist_train",339);//入党积极分子培养表

        map.put("activist_become_development",340);//积极分子确定为发展对象情况表

        map.put("development_check_info",341);//确定为入党发展对象情况表

        map.put("introduce_party",342);//入党介绍人表

        map.put("development_train",343);//发展对象培训情况表

        map.put("political_examination",344);//发展对象政审情况表

        map.put("before_party_check",345);//接受入党前审查和预审表

        map.put("probationary_party_train",346);//预备党员集中培训情况表

        map.put("probationary_party_education",347);//预备党员教育考察情况表

        map.put("probationary_party_get_people_opinion",348);//预备党员征求群众意见情况表

        map.put("probationary_party_publicity",349);//预备党员转正公示表

        map.put("exam",350);//试卷表

        map.put("exam_question",351);//试卷与题目关系表

        map.put("exam_candidate",352);//试卷与考生关系表

        map.put("exam_record",353);//答题记录表

        map.put("message",354);//消息提醒表

        map.put("message_user",355);//消息提醒用户关系表

        map.put("exam_room",356);//试卷考场关系表

        return map;
    }
}

