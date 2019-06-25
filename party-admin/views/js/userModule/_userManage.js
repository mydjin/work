var userData = [];
var roleData = [];
var userRoleData = {};
var userForm = '../../form/_user_manage.html';
var userTitle = '人员管理';
var importForm = '../../form/_userImport.html';
var importTitle = '导入用户';
var userId;
var loginName="",role_select_Id="",academyCode="";
var index = 1, size = 12, key = '';
var userStatus,userRoleId;
var detailData = [], nationData = [], academyData = [], majorData = [], branchData = [], personalData = [];

$(function () {
    if (auth.check(this)) {
        getUser();
        getAllRole();
        personalBaseInfo.getSelectAcademy();
        render.roleSelect();

    }
})

var user = {
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: userForm, fun: 'createUser();', title: userTitle});
        render.createRole();
        render.createDepart();
        personalBaseInfo.getAcademy();
        personalBaseInfo.getMajorByAcademyCode();
        personalBaseInfo.getBranchByAcademyCode();

    },
    import: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: importForm, fun: 'importUser();', title: importTitle, enter: "导入"});
        personalBaseInfo.getAcademy();
        render.createRole();
        render.createAcademy();
    },
    exportError: function () {
        $("td.td-number").attr("data-tableexport-msonumberformat", "\\@");
        $("td.td-phone").attr("data-tableexport-msonumberformat", "\\@");
        $("#error-table").tableExport({type: 'excel', fileName: 'exportError'});
    },
    importModel: function (event) {
        if (auth.refuse(event))
            return false;
        downFile('../../model/userImport.xls');
    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        var param = {url: userModule.userApi + '/getUserInfoById/' + userId};
        var request = ajax.get(param);
        request.done(function (d) {
            openInfoLay({url: '../../form/_userInfo.html', fun: 'updateUser();', title: '用户信息'});
            var model = d.result;
            personalData = model;
            detailData = model;
            renderBase.page();
            personalBaseInfo.getNation();
            renderDetail.page();
            personalBaseInfo.getAcademy();
            personalBaseInfo.getMajorByAcademyCode();
            personalBaseInfo.getBranchByAcademyCode();
        })
    },
    info: function (event) {
        if (auth.refuse(event))
            return false;
        userId = getId(event);
        var param = {url: userModule.userApi + '/getUserInfoById/' + userId};
        var request = ajax.get(param);
        request.done(function (d) {
            openLay({url: '../../form/_userInfoRead.html', title: '用户信息', enterHidden: true});
            var model = d.result;
            form.set(model);
            form.disabled();
        })


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
    token: function (event) {
        var token = getId(event);
        getByToken(token);

    },
    select: function (event) {
        key = $.trim($('#select-key').val());
        role_select_Id= $.trim($('#role-select-Id').val());
        academyCode= $.trim($('#select-academyCode').val());
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
        var model = result.get(userData, userId);
        userRoleId=model.roleId;
        openLay({url: '../../form/_change_role.html', fun: 'changeUserRole();', title: '修改角色'});
        render.changeRole();

    },
}

var render = {
    changeRole: function () {
        var template = doT.template($("#roleId-template").text());
        $('#roleId').html(template(roleData));
        if(userRoleId!=null&&userRoleId!="") {
            $("#roleId").val(userRoleId);
        }
    },  
    roleSelect: function () {
        var template = doT.template($("#roleId-select-template").text());
        $('#role-select-Id').html(template(roleData));
    },
    
    createRole: function () {
        var template = doT.template($("#roleId-template").text());
        $('#roleId').html(template(roleData));
    }
    , createDepart: function () {
        var template = doT.template($("#departInfo-template").text());
        $('#departInfo').html(template(detailData));
    },
    createAcademy: function () {
        var template = doT.template($("#academy-template").text());
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
        getUser();

    })

}

function getAllRole() {
    var param = {url: toolModule.getRoleListApi + '/' + " ",async:false};
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

function initAuth() {
    var param = {url: userModule.userAuthApi + '/' + userId};
    var request = ajax.get(param);
    request.done(function (d) {
        var data = d.result;
        openLay({url: '../../form/_system_auth.html', fun: 'setAuth();', title: '子系统配置'});
        var tmpl = doT.template($('#system-auth-tmpl').text());
        $("#system-auth-list").html(tmpl(data));
        renderIChenk();
    })
}

function setAuth() {
    var auths = [];
    $("input[name='auth']:checkbox").each(function () {
        if (true == $(this).is(':checked')) {
            var tempId = '#role' + $(this).attr('data-id');
            var roleId = $(tempId).val();
            var auth = {
                "userId": userId,
                "systemId": $(this).attr('data-id'),
                "roleId": roleId
            };
            auths.push(auth);
        }
    });
    var param = {url: userModule.userAuthApi + '/' + userId, data: auths};
    var request = ajax.put(param);
    request.done(function (d) {
        tips.ok(d.message);
        closeLay();
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
        tips.ok("密码重置成功请牢记新密码 : " + d.result.password + "");
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
    var param = {url: userModule.userApi + '/' + index + '-' + size + '-' + key+'-'+role_select_Id+"-"+academyCode};
    var request = ajax.get(param);
    request.done(function (d) {
        userData = d.result.data;
        renderList();
        if (d.result.totalPage > 1) {
            initPage(d.result.totalPage, d.result.totalCount);
        }else {
            $('.list-page').empty();
        }
        auth.show();
    })
}

// 创建
function createUser() {
    var data = form.get("#opt-form");
    if (form.verify(data))
        return false;
    var departdata = form.get("#opt2-form");
    if (form.verify(departdata))
        return false;
    data['academyCode'] = departdata['academyCode'];
    data['departCode'] = departdata['departCode'];


    data = {"attr": data, "loginName": data.loginName, "password": data.password, "number": data['number'],"type":data['type'],"roleId":data['roleId']};

    var param = {url: userModule.userApi, data: data};
    var request = ajax.post(param);
    request.done(function (d) {
        tips.done(d);
        getUser();
    })
}

function importUser() {
    var data = form.get("#opt-form");
    if (form.verify(data))
        return false;
    if (data['roleId'] == "" || data['roleId'] == null) {
        tips.warning("请选择角色");
        return false;
    }

    if (data['academyCode'] == "" || data['academyCode'] == null) {
        tips.warning("请选择学院");
        return false;
    }

    if (data['type'] == "" || data['type'] == null) {
        tips.warning("请选择用户类型");
        return false;
    }


    if (!xls_check("file")) {
        return false;
    }
    //console.log(data);debugger;
    $('#opt-dialog-cancel').attr("disabled", true);
    $('#roleId').attr("disabled", true);
    $('#academyCode').attr("disabled", true);
    $('#type').attr("disabled", true);
    $('#file').attr("disabled", true);
    $('#opt-dialog-enter').attr("disabled", true);
    $('#todoBar').empty();
    $('#todoPer').empty();
    $('#mark').empty();
    $('#res').empty();


    var param = {url: baseModule.userManageApi + '/importSetting', data: data, async: false};
    var request = ajax.post(param);
    request.done(function (d) {
        var batch = d.result;
        initImportStatus();


        importFile("file", baseModule.userManageApi + '/import/' + batch,
            function importComplete(evt) {
                $('#opt-dialog-cancel').attr("disabled", false);
                $('#roleId').attr("disabled", false);
                $('#academyCode').attr("disabled", false);
                $('#type').attr("disabled", false);
                $('#file').attr("disabled", false);
                $('#opt-dialog-enter').attr("disabled", false);
                importCompleteShow(evt);
                getUser();
            });

        checkImportStatus(baseModule.userManageApi + "/importStatus/" + batch);



    })


}

// 修改
function updateUser() {
    personalBaseInfo.update();
    closeInfoLay();

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
    server: function (_server) {
        switch (parseInt(_server)) {
            case 0:
                return "后台";
            case 1:
                return "PC/微信";
            default:
                return "";
        }
    }, type: function (_type) {
        switch (_type) {
            case "redis":
                return "会话";
            case "wechat":
                return "微信";
            default:
                return "";
        }
    },
}

//修改用户信息接口


var personalBaseInfo = {
    getSyncByAcademyCode: function () {
        personalBaseInfo.getMajorByAcademyCode();
        personalBaseInfo.getBranchByAcademyCode();

    },
    getMajorByAcademyCode: function () {
        var academyCode = $('#academyCode').val();
        academyCode = academyCode || detailData['academyCode'];
        if (academyCode == undefined || academyCode == "")
            return;
        var param = {url: toolModule.getMajorListApi + "/" + academyCode};
        var request = ajax.get(param);
        request.done(function (d) {
            majorData = d.result;
            renderDetail.majorSelect();
        })
    },
    getBranchByAcademyCode: function () {
        var academyCode = $('#academyCode').val();
        academyCode = academyCode || detailData['academyCode'];
        if (academyCode == undefined || academyCode == "")
            return;
        var param = {url: toolModule.getBranchListApi + "/" + academyCode};
        var request = ajax.get(param);
        request.done(function (d) {
            branchData = d.result;
            renderDetail.branchSelect();
        })
    },
    getNation: function () {
        var param = {url: toolModule.getNationListApi};
        var request = ajax.get(param);
        request.done(function (d) {
            nationData = d.result;
            renderBase.nationSelect();
            $('#nationCode').val(personalData['nationCode']);
        })
    },
    getAcademy: function () {
        var param = {url: toolModule.getAcademyListApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            academyData = d.result;
            renderDetail.academySelect();
            if (detailData['academyCode'] != "")
                $('#academyCode').val(detailData['academyCode']);
        })
    },
    getSelectAcademy: function () {
        var param = {url: toolModule.getAcademyListApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            academyData = d.result;
            var template = doT.template($("#academy-template").text());
            $('#select-academyCode').html(template(academyData));
        })
    },
    update: function () {
        var data = form.get("#opts-form");
        if (form.verify(data))
            return false;
        if ($('#nationCode').val() == "") {
            tips.warning("请选择民族");
            return false;
        }
        if (data['idCard'].length != 15 && data['idCard'].length != 18) {
            tips.warning("身份证长度不正确，请重新输入")
            return false;
        }
        if (!(/^1(3|4|5|7|8)\d{9}$/.test(data['phone']))) {
            tips.warning("手机号码有误，请重填");
            return false;
        }
        if (data['email'].length > 0) {
            if (!(/^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(data['email']))) {
                tips.warning("邮箱格式错误，请重填");
                return false;
            }
        }

        data['id'] = userId;

        data['birthday'] = time.date2timestamp(data['birthday']);


        var detailData = form.get("#opt-form");
        if (form.verify(detailData))
            return false;
        detailData['id'] = userId;

        if ($('#academyCode').val() == "") {
            tips.warning("请选择学院");
            return false;
        }
        detailData['academyCode'] = $('#academyCode').val();

        if ($('#majorCode').val() == "") {
            tips.warning("请选择专业");
            return false;
        }
        detailData['majorCode'] = $('#majorCode').val();

        detailData['departCode'] = $('#departCode').val();
        detailData['myClass'] = $('#myClass').val();
        detailData['studyStatus'] = $('#studyStatus').val();


        var param = {url: userModule.updatePersonalBaseInfoApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);


            var param = {url: userModule.updatePersonalDetailInfoApi + '/', data: detailData};
            var request = ajax.put(param);
            request.done(function (d) {
                tips.ok(d.message);
                closeLay();
                getUser();
            })
        })

    }

};


// 渲染
var renderBase = {
    page: function () {
        var template = doT.template($("#baseInfo-template").text());//获取的模板
        $('#personalBaseInfo').html(template(personalData));//模板装入数据
        // personalBaseInfo.getNation();
        var number = personalData['number'];
        if (number != null && number != '') {
            $("#number").attr("readonly", true);
        }
        var idCard = personalData['idCard'];
        if (idCard != null && idCard != '') {
            $("#idCard").attr("readonly", true);
        }

        var phone = personalData['phone'];
        if (phone != null && phone != '') {
            $("#phone").attr("readonly", true);
        }


        if (personalData['birthday'] == null) {
            $('#birthdayStr').val("");
        } else {
            $('#birthdayStr').val(time.timestamp2shortdate(personalData['birthday']));
        }

        laydate.render({
            elem: "#birthdayStr"
        });
    },
    nationSelect: function () {
        var template = doT.template($("#nation-template").text());
        $('#nationCode').html(template(nationData));
    }
}
var renderDetail = {
    page: function () {
        var template = doT.template($("#detailInfo-template").text());
        $('#personalDetailInfo').html(template(detailData));
        personalBaseInfo.getAcademy();
    },
    academySelect: function () {
        var template = doT.template($("#academy-template").text());
        $('#academyCode').html(template(academyData));
    },

    majorSelect: function () {
        var template = doT.template($("#major-template").text());
        $('#majorCode').html(template(majorData));

        if (detailData['majorCode']) {
            for (var i = 0; i < majorData.length; i++) {
                if (majorData[i].code == detailData['majorCode']) {
                    $('#majorCode').val(detailData['majorCode']);
                }
            }
        }
    },
    branchSelect: function () {
        var template = doT.template($("#depart-template").text());

        $('#departCode').html(template(branchData));

        if (detailData['departCode']) {
            for (var i = 0; i < branchData.length; i++) {
                if (branchData[i].code == detailData['departCode']) {
                    $('#departCode').val(detailData['departCode']);
                }
            }
        }
    },


}
