var express = require('express');
var router = express.Router();


router.get('/message',function (req,res) {
    res.render('userModule/userMessage');
});

router.get('/todo',function (req,res) {
    res.render('userModule/userTodo');
});

router.get('/todo',function (req,res) {
    res.render('userModule/userTodo');
});

router.get('/operationRecord',function (req,res) {
    res.render('userModule/userOperationRecord');
});

module.exports = router;