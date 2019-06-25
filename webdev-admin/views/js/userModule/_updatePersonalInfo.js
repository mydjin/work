var detailData = [], classData = [], academyData = [], majorData = [], personalData = [];
var optId = '';
var index = 1, size = 12, key = '';
var config = {
    title: '修改信息',
}


$(function () {
    //自适应
    if (auth.check(this)) {
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        personalBaseInfo.get();
    }
});

var personalBaseInfo = {
    get: function () {
        var param = {url: userModule.getPersonBaseAndDetailApi};
        var request = ajax.get(param);
        request.done(function (d) {
            personalData = d.result.userInfoList;
            optId = personalData.id;
            render.page();
            personalBaseInfo.getAcademy();


        })
    },
    getAcademy: function () {
        var param = {url: toolModule.getAcademyInfoListApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            academyData = d.result;
            render.academySelect();
            if (personalData[0]['academyCode'] != "")
                $('#academyCode').val(personalData[0]['academyCode']);
        })

    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        opt.update();
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


        if ($('#academyCode').val() == "") {
            tips.warning("请选择部门");
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


        data['id'] = optId;

        data['birthday'] = time.date2timestamp(data['birthday']);

        console.log(data);

        var param = {url: userModule.updatePersonalBaseInfoApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            personalData = result.update(personalData, d.result, 'id');
            personalBaseInfo.get();
            closeLay();

        })
    },


}

// 渲染
var render = {
    page: function () {
        var template = doT.template($("#baseInfo-template").text());//获取的模板
        $('#personalBaseInfo').html(template(personalData));//模板装入数据
        var number = personalData[0]['number'];
        if (number != null && number != '') {
            $("#number").attr("readonly", true);
        }
        var idCard = personalData[0]['idCard'];
        if (idCard != null && idCard != '') {
            $("#idCard").attr("readonly", true);
        }

        if (personalData[0]['birthday'] == null) {
            $('#birthdayStr').val("");
        } else {
            $('#birthdayStr').val(time.timestamp2shortdate(personalData[0]['birthday']));
        }

        laydate.render({
            elem: "#birthdayStr"
        });
    },
    academySelect: function () {
        var template = doT.template($("#academy-template").text());
        $('#academyCode').html(template(academyData));
    },

}

// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}


