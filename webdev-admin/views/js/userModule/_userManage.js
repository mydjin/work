var userData = [];
var roleData = [];
var userRoleData = {};
var userForm = '/form/_user_manage.html';
var userTitle = '管理员设置';
var userId;
var loginName;
var index = 1, size = 12, key = '';
var userStatus;


$(function () {
    if (auth.check(this)) {
        getUser();
        getAllRole();
    }
})

var info = {
    getAcademy: function () {
        var param = {url: toolModule.getAcademyInfoListApi, async: false};
        var request = ajax.get(param);
        request.done(function (d) {
            academyData = d.result;
            render.academySelect();
        })
    },

}


var user = {
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: userForm, fun: 'createUser();', title: userTitle});
        render.createRole();
    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        var model = result.get(userData, userId);
        openInfoLay({url: '/form/_userInfo.html', fun: 'updateUser();', title: '管理员信息'});
        info.getAcademy();
        form.set(model.info);
    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        tips.confirm({message: '是否要删除这条数据？', fun: "deleteUser();"});
    },
    status: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        userStatus = getData(event, 'data-status');
        var status = userStatus == 1 ? '冻结' : '恢复';
        tips.confirm({message: '确定要' + status + '该用户状态？', fun: "initStatus();", enter: '确定'});
    },
    random: function () {
        var pwd = string.random(8);
        $("input[name='password']").val(pwd);
    },
    info: function (event) {
        userId = getId(event);
        var model = result.get(userData, userId);

        openInfoLay({url: '/form/_userInfo_read.html', title: '管理员信息', hideEnter: true});

        info.getAcademy();
        form.set(model.info);

    },
    token: function (event) {
        var token = getId(event);
        getByToken(token);

    },
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        getUser();
    },
    initPwd: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        tips.confirm({message: '确定要重置该用户的登录密码？', fun: "initPwd();", enter: '重置'});
    },
    setAuth: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        initAuth();
    },
    changeRole: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);

        // var model = result.get(userData,userId);
        openLay({url: '/form/_change_role.html', fun: 'changeUserRole();', title: '修改角色'});
        getUserRole();
        getAllRole();
        render.changeRole();
        // form.set(model.info);
        $("#roleId").find("option[value = '" + userRoleData.roleId + "']").attr("selected", true);
    },
}

var render = {
    changeRole: function () {
        var template = doT.template($("#roleId-template").text());
        $('#roleId').html(template(roleData));
    },
    createRole: function () {
        var template = doT.template($("#roleId-template").text());
        $('#create-roleId').html(template(roleData));
    },
    academySelect: function () {
        var template = doT.template($("#academy-select-template").text());
        $('#academyCode').html(template(academyData));
    },
}

function changeUserRole() {
    var data = form.get("#opt-form");
    if (form.verifyPlus(data))
        return false;
    var roleId = data['roleId'];

    // var param = {url:userModule.userManageApi+'/'+index+'-'+size+'-'+loginName};
    var param = {url: userModule.userRoleChangeApi + '/' + userId + '-' + roleId};
    var request = ajax.put(param);
    request.done(function (d) {
        tips.ok(d.message);
        closeLay();
    })
}

function getAllRole() {
    var param = {url: roleModule.roleApi + '/getAll/' + " "};
    var request = ajax.get(param);
    request.done(function (d) {
        roleData = d.result;
    })
}

function getUserRole() {
    var param = {url: roleModule.roleApi + '/user/' + userId, async: false};
    var request = ajax.get(param);
    request.done(function (d) {
        userRoleData = d.result;
    })
}

function initStatus() {
    userStatus = userStatus == 1 ? 0 : 1;
    var param = {url: userModule.userStatusApi + '/' + userId + '-' + userStatus};
    var request = ajax.put(param);
    request.done(function (d) {
        tips.ok("用户状态调整成功");
        getUser();
    })
}

function initPwd() {
    var param = {url: userModule.userPwdApi + '/' + userId};
    var request = ajax.put(param);
    request.done(function (d) {
        tips.ok("密码重置成功请牢记新密码 <br> <span style='color:red;'>" + d.result.password + "</span>");
        closeLay();
    })
}

function renderList() {
    var tmpl = doT.template($('#user-tmpl').text());
    $("#user-list").html(tmpl(userData));
    renderTip();
    renderIChenk();
}


//读取
function getUser() {
    var param = {url: userModule.userApi + '/' + index + '-' + size + '-' + key};
    var request = ajax.get(param);
    request.done(function (d) {
        userData = d.result.data;
        renderList();
        if (d.result.totalPage > 1) {
            initPage(d.result.totalPage, d.result.totalCount);
        }
        auth.show();
    })
}

// 创建
function createUser() {
    var data = form.get("#opt-form");
    if (form.verify(data))
        return false;
    data = {
        "attr": data,
        "loginName": data.loginName,
        "password": data.password,
        "number": data['number'],
        "type": data['type']
    };
    var param = {url: userModule.userApi, data: data};
    var request = ajax.post(param);
    request.done(function (d) {
        tips.done(d);
        getUser();
    })
}

// 修改
function updateUser() {
    var data = form.get("#opt-form");
    if (form.verify(data))
        return false;
    var param = {url: userModule.userApi + '/' + userId, data: data};
    var request = ajax.put(param);
    request.done(function (d) {
        tips.ok(d.message);
        closeInfoLay();
        getUser();
    })
}

// 删除
function deleteUser() {
    var request = ajax.delete({url: userModule.userApi + '/' + userId});
    request.done(function (d) {
        tips.ok(d.message);
        userData = json.delete(userData, userId);
        renderList();
    })
}

//状态调整
function statususer(_status) {
    var data = {status: _status, id: userId};
    var param = {url: userModule.statusApi, data: data};
    var request = ajax.put(param);
    request.done(function (d) {
        tips.ok(d.message);
        closeLay();
        userData = json.update(userData, d.result);
        renderList();
    }).fail(function () {
        renderList();
    })
}

//状态显示
function format_status(_status) {
    return _status == 1 ? '正常' : '冻结';
}


function initPage(_pageSize, _total) {
    $('.list-page').pagination({
        pageCount: _pageSize,
        current: index,
        jump: true,
        coping: true,
        homePage: '首页',
        endPage: '末页',
        prevContent: '上页',
        nextContent: '下页',
        pageSize: size,
        totalCount: _total,
        callback: function (api) {
            index = api.getCurrent();
            getUser();
        }
    });
    if (_pageSize > 0)
        $('.list-page').show();
}

function pageChange(event) {
    size = $(event).val();
    index = 1;
    getUser();
}


var helper = {
    status: function (_status) {
        switch (parseInt(_status)) {
            case 1:
                return "正常";
            case 2:
                return "冻结";
            default:
                return "";
        }
    },
    type: function (_type) {
        switch (parseInt(_type)) {
            case 1:
                return "教职工";
            case 2:
                return "学生";
            default:
                return "";
        }
    }
}