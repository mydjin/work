var detailData = [], nationData = [], academyData = [], majorData = [], branchData = [], personalData = [];
var optId = '';
var index = 1, size = 12, key = '';
var config = {
    title: '修改个人信息',
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
            personalData = d.result.userBaseInfoList;
            detailData = d.result.userDetailInfoList;
            // console.log(personalData)
            render.page();
            personalBaseInfo.getNation();

            renderDetail.page();
            personalDetailInfo.getAcademy();
            personalBaseInfo.getMajorByAcademyCode();
            personalBaseInfo.getBranchByAcademyCode();

            // personalDetailInfo.getMajor();

        })
    },
    getSyncByAcademyCode: function () {
        personalBaseInfo.getMajorByAcademyCode();
        personalBaseInfo.getBranchByAcademyCode();

    },

    getMajorByAcademyCode: function () {
        var academyCode = $('#academyCode').val();
        /* if(academyCode.length == 0){
             $("#majorCode").html("<option value='' selected='selected'>请选择专业</option>");
         }*/
        academyCode = academyCode || detailData[0]['academyCode'];
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
        /* if(academyCode.length == 0){
             $("#majorCode").html("<option value='' selected='selected'>请选择专业</option>");
         }*/
        academyCode = academyCode || detailData[0]['academyCode'];
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
            render.nationSelect();
            $('#nationCode').val(personalData[0]['nationCode']);
        })
    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        opt.update();
    },

};

// resetData: function () {
//     $('select').val('0');
//     $('input').val('');
// },


var personalDetailInfo = {


    getAcademy: function () {
        var param = {url: toolModule.getAcademyListApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            academyData = d.result;
            renderDetail.academySelect();
            if (detailData[0]['academyCode'] != "")
                $('#academyCode').val(detailData[0]['academyCode']);
        })

    },

    getMajor: function () {

        var param = {url: baseModule.majorInfoApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            majorData = d.result;
            renderDetail.majorSelect();


            // if (detailData[0]['majorCode']) {
            //     for (var i = 0; i < majorData.length; i++) {
            //         if (majorData[i].code == detailData[0]['majorCode']) {
            //             $('#majorCode').val(detailData[0]['majorCode']);
            //             // $('#majorCode').html("<option value='"+majorData[i].code +"'>"+majorData[i].name +"</option>");
            //         }
            //     }
            // }
            // console.log(detailData[0]['majorCode'])
            // if(detailData[0]['majorCode'])
            //     $('#majorCode').val(detailData[0]['majorCode']);
        })
    },
    update: function (event) {

        if (auth.refuse(event))
            return false;
        optDetail.update();
    },

}

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

        var param = {url: userModule.updatePersonalBaseInfoApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            //   tips.ok(d.message);
            //更新对象
            optDetail.update();


        })
    },


}
var optDetail = {
    update: function () {
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

        /*  if ($('#departCode').val() == "") {
              tips.warning("请选择党支部");
              return false;
          }*/

        // data['surveyTime'] = time.date2timestamp(data['surveyTime']);
        var param = {url: userModule.updatePersonalDetailInfoApi + '/', data: data};
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
        // personalBaseInfo.getNation();
        var number = personalData[0]['number'];
        if (number != null && number != '') {
            $("#number").attr("readonly", true);
        }
        var idCard = personalData[0]['idCard'];
        if (idCard != null && idCard != '') {
            $("#idCard").attr("readonly", true);
        }

        var phone = personalData[0]['phone'];
        if (phone != null && phone != '') {
            $("#phone").attr("readonly", true);
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
    nationSelect: function () {
        var template = doT.template($("#nation-template").text());
        $('#nationCode').html(template(nationData));
    }
}
var renderDetail = {
    page: function () {
        var template = doT.template($("#detailInfo-template").text());
        $('#personalDetailInfo').html(template(detailData));
        personalDetailInfo.getAcademy();
        // personalDetailInfo.getAcademy();
    },
    academySelect: function () {
        var template = doT.template($("#academy-template").text());
        $('#academyCode').html(template(academyData));
    },

    majorSelect: function () {
        var template = doT.template($("#major-template").text());
        $('#majorCode').html(template(majorData));

        if (detailData[0]['majorCode']) {
            for (var i = 0; i < majorData.length; i++) {
                if (majorData[i].code == detailData[0]['majorCode']) {
                    $('#majorCode').val(detailData[0]['majorCode']);
                }
            }
        }
    },
    branchSelect: function () {
        var template = doT.template($("#depart-template").text());

        $('#departCode').html(template(branchData));

        if (detailData[0]['departCode']) {
            for (var i = 0; i < branchData.length; i++) {
                if (branchData[i].code == detailData[0]['departCode']) {
                    $('#departCode').val(detailData[0]['departCode']);
                }
            }
        }
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


