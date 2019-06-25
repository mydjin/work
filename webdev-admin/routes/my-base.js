var express = require('express');
var router = express.Router();

//修改密码
router.get('/core/updatePassword',function (req,res) {
    res.render('userModule/updatePassword');
});
//修改信息
router.get('/core/updatePersonalInfo',function (req,res) {
    res.render('userModule/updatePersonalInfo');
});
//数据字典
router.get('/core/dictionary',function (req,res) {
    res.render('sysModule/dictionary');
});

//操作记录
router.get('/core/operationRecord',function (req,res) {
    res.render('sysModule/operationRecord');
});

//部门信息管理
router.get('/core/academyInfo',function (req,res) {//对应前端路由
    res.render('baseModule/academyInfo');//对应ejs文件
});

//管理员设置
router.get('/core/userManage',function (req,res) {//管理员设置对应前端菜单权限配置的路由
    res.render('userModule/userManage');//对应ejs文件
});

//短信管理
router.get('/core/note',function (req,res) {
    res.render('sysModule/note');
});

//消息管理
router.get('/core/message',function (req,res) {
    res.render('sysModule/message');
});

//文章管理
router.get('/core/news',function (req,res) {
    res.render('baseModule/news');
});

module.exports = router;