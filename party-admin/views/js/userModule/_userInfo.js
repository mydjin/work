var index = 1, size = 12, totalPage = 0, totalCount = 0;
var pageData = [];
var optId = '';
var number="",academyCode="";
var personalData = {};
var detailData = {};
var nationData = [], academyData = [], majorData = [], branchData = [];

var config = {
    form: '_userInfo.html',
    title: '用户信息',
};

$(function () {
    //自适应
    view.initHeight();
    $(window).resize(function () {
        view.initHeight();
    });
    userInfo.get();
    personalBaseInfo.getSelectAcademy();
});

var userInfo = {
    get: function () {
        var param = {url: baseModule.userInfoApi + '/' + index + '-' + size + '-' + number + '-' + academyCode};
        var request = ajax.get(param);
        request.done(function (d) {
            pageData = d.result.data;
            render.page();
            totalPage = d.result.totalPage;
            totalCount = d.result.totalCount;
            if (d.result.totalPage > 1) {
                page.init(d.result.totalPage, d.result.totalCount);
            } else {
                $('.list-page').empty();
            }
            auth.show();
        })
    },
    select: function (event) {
        number = $.trim($('#select-number').val());
        academyCode= $.trim($('#select-academyCode').val());
        index = 1;
       userInfo.get();
    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        var model = result.get(pageData, optId);
        // console.log(model);
        openInfoLay({url: '../../form/_userInfo.html', fun: 'opt.update();', title: '用户信息'});
        personalData = model;
        detailData = model;
        renderBase.page();
        personalBaseInfo.getNation();
        renderDetail.page();
        personalBaseInfo.getAcademy();
        personalBaseInfo.getMajorByAcademyCode();
        personalBaseInfo.getBranchByAcademyCode();
        // form.set(model);
    },
    info: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var param = {url: userModule.userApi + '/getUserInfoById/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            openLay({url: '../../form/_userInfoRead.html', title: '用户信息', enterHidden: true});
            var model = d.result;
            form.set(model);
            form.disabled();
        })

    },
};

var render = {
    page: function () {
        var template = doT.template($("#userInfo-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
};

var opt = {
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

        data['id'] = optId;

        data['birthday'] = time.date2timestamp(data['birthday']);
        //console.log(data);

        var param = {url: userModule.updatePersonalBaseInfoApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            var data = form.get("#opt-form");
            if (form.verify(data))
                return false;
            data['id'] = optId;

            if ($('#academyCode').val() == "") {
                tips.warning("请选择学院");
                return false;
            }

            if ($('#majorCode').val() == "") {
                tips.warning("请选择专业");
                return false;
            }
            //  console.log(data);

            var param = {url: userModule.updatePersonalDetailInfoApi + '/', data: data};
            var request = ajax.put(param);
            request.done(function (d) {
                tips.ok(d.message);
                closeLay();
                closeInfoLay();

                userInfo.get();
            })
        })
    },
    close: function () {   //关闭按钮
        closeLay();
    }
};

var page = {
    init: function (_pageSize, _total) {
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
            pageArray: [6, 12, 24, 48],
            totalCount: _total,
            id: 'userInfo-page',
            callback: function (api) {
                index = api.getCurrent();
                userInfo.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
};

var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
};


function pageChange(event) {
    size = $(event).val();
    index = 1;
    userInfo.get();
};

var helper = {
    sex: function (_sex) {
        switch (parseInt(_sex)) {
            case 1:
                return "女";
            case 2:
                return "男";
        }
    },
    politicalOutlook: function (_politicalOutlook) {
        switch (parseInt(_politicalOutlook)) {
            case 1:
                return "群众";
            case 2:
                return "共青团员";
            case 3:
                return "共产党员";
        }
    },
    studyStatus: function (_studyStatus) {
        switch (parseInt(_studyStatus)) {
            case 0:
                return "在读";
            case 1:
                return "毕业";
        }
    },
};

var tool = {
    translate: function (model) {
        var data = [];
        for (var variable in model) {
            data[variable] = model[variable];
            //判断helper里是否存在该函数，存在则执行转换
            if (typeof eval('helper.' + variable) == 'function')
                model[variable] = eval('helper.' + variable + '(' + model[variable] + ')');
        }
        form.set(model);
        //恢复回转换前数据
        for (var variable in data) {
            model[variable] = data[variable];
        }
    }
};

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
