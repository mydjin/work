var userData = [], passwordData = [], rolesData = [], tagsData = [];
var problemData = [];
var index = 1, size = 12, key = '', loginName = '';
var config = {
    title: '修改密码',
}

function validPwd() {
    var pwd = document.getElementById('password').value;
    if ((pwd.length > 16 || pwd.length < 6) && pwd != "") {
        // tips.warning("密码长度应该在 6 - 16 位");
        $('#show').show();
        $('#hide').hide();
        return false;
    }
    return true;
}

$(function () {
    //自适应
    if (auth.check(this)) {
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
    }
});

var updatePassword = {

    update: function () {
        //  debugger;
        //if 语句中的括号只有返回true才执行 return false，validPwd()返回的是false，所以要取反
        if (!validPwd())
            return false;

        var data = form.get("#opts-form");
        if (form.verify(data))
            return false;

        var param = {url: userModule.updatePasswordApi + '/', data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.ok(d.message);
        })
    },


    reset: function () {
        $("input[name='oldPassword']").val("");
        $("input[name='password']").val("");
        $("input[name='passwordCheck']").val("");
    }
}

var opt = {}


// 渲染
var render = {}

// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}


