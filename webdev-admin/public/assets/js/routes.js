/**
 * Created by Qihsoft on 2017/9/10.
 */

var fileSavepath = "qihsoft-1251217219.cosgz.myqcloud.com";

// var path = 'http://localhost:9999';
var path = (window.location.host).indexOf('localhost') >= 0 || (window.location.host).indexOf('127.0.0.1') >= 0?
    'http://localhost:9091' :'//api.admin.wechat.gxun.club'; //'http://10.1.151.88:9091';
var commonV1 = path + '/v1/common';   //公共路由
var sysV1 = path + '/v1/sys';        //系统路由
var baseV1 = path + '/v1/base';      //基础路由
var otherV1 = path + '/v1/other';    //辅助路由
var unifiedV1 = path + '/v1/unified';        //统一管理api
var toolV1 = path  + '/v1/tool';        //系统工具api

/*********************工具路由************************/
var toolModule = {
    getAcademyInfoListApi: toolV1+ '/getAcademyInfoList',
    getAcademyInfoTreeApi: toolV1+ '/getAcademyInfoTree',
    savePicUrlApi: toolV1+ '/savePicUrl',//存储外网图片
    pictureApi:toolV1+ '/picture',//图片管理
    checkPicApi:toolV1+'/checkPicture',//检测图片识别码
    videoApi:toolV1 + '/video',//视频管理
    checkVideoApi:toolV1+'/checkVideo',//检测视频识别码
    getDicFieldNameApi: toolV1 + '/getDicFieldName',//获取字典信息
}


/*********************系统相关配置路由************************/
var globalModule = {
    //optApi: sysV1 + '/config/global',
    dictionaryApi: sysV1 + '/dictionary',
}

/*********************第三方配置路由************************/
var tpsModule = {
    configApi: unifiedV1 + '/config',
    ossApi: unifiedV1 + '/oss',
    optApi: sysV1 + '/config/tps',
    cosApi: commonV1 + '/cos',
}

/*********************辅助管理路由*************************/
var otherModule = {
    codeApi: otherV1 + '/code',
    frontCodeApi: otherV1 + '/front_code',
}
/*********************公共路由*************************/
var commonModule = {
    verifyCodeApi: commonV1 + '/verify-code',         //验证码
    verifyCodePicApi: commonV1 + '/verify-code/jpg',         //验证码
    sysLoginApi: commonV1 + '/login',                      //登录
    sysVerifyCodeApi: commonV1 + '/verify_code',//获取手机验证码
    sysForgetApi: commonV1 + '/forget',//忘记密码

}
/*********************用户模块路由*************************/
var userModule = {
    lock: unifiedV1 + '/user-lock-system/sys-config',   //操作界面锁
    loginOutApi: unifiedV1 + '/user-login-out',         //退出系统
    cacheUpdateApi: unifiedV1 + '/user-cache',
    userApi: sysV1 + '/user',
    userTokenApi: unifiedV1 + '/user-token',
    userPwdApi: sysV1 + '/user-pwd',
    userStatusApi: sysV1 + '/user-status',
    userRoleChangeApi: sysV1 + '/change-role',
    updatePasswordApi: sysV1 + '/updatePassword',
    getPersonBaseAndDetailApi: sysV1 + '/getPersonInfo',
    updatePersonalBaseInfoApi: sysV1 + '/updatePersonalInfo',
}
/*********************权限模块路由*************************/
var authModule = {
    moduleApi: sysV1 + '/menu-module',               //权限模块
    menuApi: sysV1 + '/menu',                        //权限菜单
    subApi: sysV1 + '/sub-menu',                     //子菜单
    subAuthApi: sysV1 + '/sub-menu-auth',            //子菜单权限
}


/*********************角色管理路由*************************/
var roleModule = {
    roleApi: sysV1 + '/role',
    authApi: sysV1 + '/role-auth',
}
/*********************基础数据************************/
var baseModule = {

    sysOperationRecordApi:baseV1 + '/sysOperationRecord',//操作日志
    academyInfoApi: baseV1 + '/academyInfo',//部门信息
    userManageApi:sysV1 + '/user',//管理员设置
    noteApi:baseV1+'/note',//短信管理
    messageApi:baseV1+'/message',//消息管理
    userMessageApi:baseV1+'/message/user',//消息用户关系管理
    indexMessageListApi:baseV1+ '/message/user/index',//首页消息
    articleApi: baseV1 + '/article',//新闻

}

