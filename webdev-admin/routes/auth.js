/**
 * Created by Qihsoft on 2017/8/27.
 */

var express = require('express');
var router = express.Router();

//首页
router.get('/',function (req,res) {
    res.render('index');
});
//菜单管理
router.get('/menu',function (req,res) {
    res.render('menu');
});

//欢迎页
router.get('/welcome',function (req,res) {
    res.render('welcome');
});
//代码生成器
router.get('/code',function (req,res) {
    res.render('code');
});
//前端代码生成器
router.get('/front_code',function (req,res) {
    res.render('front_code');
});
//表单模板构建器
router.get('/component',function (req,res) {
    res.render('component');
});

//角色管理
router.get('/role',function (req,res) {
    res.render('role');
});
// 用户管理
router.get('/user',function (req,res) {
    res.render('user');
});




module.exports = router;