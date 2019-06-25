package com.qihsoft.webdev.utils.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Qihsoft on 2017/8/23.
 */
public class Table {
    public static Map<String,Integer> BUSINESS = getTable();

    private static Map<String,Integer> getTable(){
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("sys_user", 101);//系统用户表
        map.put("sys_user_token",102);//用户Token关联表
        map.put("sys_user_role",103);//用户对应角色关联表

        map.put("sys_role",106);//角色表
        map.put("sys_role_menu",107);//角色菜单表
        map.put("sys_role_auth",108);//角色菜单权限表
        map.put("sys_menu", 109);//权限菜单表
        map.put("sys_menu_auth", 110);//权限菜单按钮权限表
        map.put("sys_operation_record",111);//系统操作记录
        map.put("sys_auto_code",112);//代码生成记录
        map.put("sys_tps_config",113);//系统第三方配置项
        map.put("sys_global_config",114);//系统基本配置

        map.put("sys_user_info",201);//用户信息表
        map.put("academy_info",202);//部门信息表
        map.put("picture",204);//图片表
        map.put("video",205);//视频表
        map.put("dictionary",208);//数据字典表
        map.put("note",209);//短信表
        map.put("message",210);//消息提醒表
        map.put("message_user",211);//消息提醒用户关系表

        map.put("article",301);//文章表

        return map;
    }
}

