/**
 * Created by Taller on 2017/8/27.
 */
var express = require('express');
var router = express.Router();

router.get('/',function (req,res) {
    res.render('login');
});
router.get('/forgetPassword',function (req,res) {
    res.render('forgetPassword');
});


module.exports = router;