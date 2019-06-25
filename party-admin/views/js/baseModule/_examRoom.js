var index = 1, size = 6, totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var optId = '';
var examId= '',monitor='', select_examRoom ='',roomStatus = '';

var config = {
    form: '../../form/_examRoom.html',
    updateForm: '../../form/_examRoomUpdate.html',
    title: '考场管理',
};

$(function () {
    //自适应
    view.initHeight();
    $(window).resize(function () {
        view.initHeight();
    });
    examId = url.getUrlParam("key");
    examRoom.get();
    examRoom.getAllExam();
});

var examRoom = {
    get: function () {
        var param = {url: baseModule.examRoomApi + '/' + index + '-' + size + '-' +examId+"-"+monitor+"-"+select_examRoom+"-"+roomStatus};
        var request = ajax.get(param);
        request.done(function (d) {
            pageData = d.result.data;
            render.page();
            totalPage = d.result.totalPage;
            totalCount = d.result.totalCount;
            if (d.result.totalPage>1) {
                  page.init(d.result.totalPage, d.result.totalCount);
            }else{
                 $('.list-page').empty();
            }
           // console.log(pageData);
        })
    },
    select: function () {
        //查询

        examId= $.trim($('#select-examId').val());
        select_examRoom = $.trim($('#input-examRoom').val());
        monitor = $.trim($('#input-Name').val());
        roomStatus = $.trim($('#select-roomStatus').val());
        index = 1;
        examRoom.get();



    },

    getAllExam: function () {
        //获取所有考试列表
        var param = {url: baseModule.examApi + "/all/selectList",async:false};
        var request = ajax.get(param);
        request.done(function (d) {
            examSelectData = d.result;
            render.examSelect();
        });
    },
    getExamSelect: function () {
        //获取所有未结束考试列表
        var param = {url: baseModule.examApi + "/ready/selectList",async:false};
        var request = ajax.get(param);
        request.done(function (d) {
            examSelectData = d.result;
            render.examAdd();
        });
    },
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title});
        examRoom.getExamSelect();

    },

    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '确定要删除该考场信息吗？如果删除，该考场的考生信息也将被删除。', fun: "opt.delete();"});

    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.updateForm, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);
        form.set(model);
        var isUnseal =parseInt(model.isUnseal);
        if(isUnseal==0){
            $("#unsealCode-body").hide();
        }else if(isUnseal==1){
            $("#unsealCode-body").show();
        }

    },
    random: function () {
        var unsealCode = string.random(8);
        $("input[name='unsealCode']").val(unsealCode);
    },
    sync: function () {
        var isUnseal = $("#isUnseal option:selected").val();
        if(isUnseal!=null&&isUnseal!="") {
            if (parseInt(isUnseal) == 0) {
                $("#unsealCode-body").hide();
            } else if (parseInt(isUnseal) == 1) {
                $("#unsealCode-body").show();
            }
        }
    },
    sendNote: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '确定要短信推送吗？如果点击确定，有手机号码的本场考生将会收到短信！', fun: "opt.sendNote();", enter: "发布"});
    },
    finish: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '您确定要结束本次的所有考试吗？如果点击确定，所有考生都不能继续参加考试，未提交试卷的考生成绩将为0。', fun: "opt.finish();", enter: "结束"});
    },
};

var render = {
    page: function () {
        var template = doT.template($("#examRoom-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
    examSelect: function () {
        var template = doT.template($("#exam-select-template").text());
        $('#select-examId').html(template(examSelectData));
    },
    examAdd: function () {
        var template = doT.template($("#exam-select-template").text());
        $('#examId').html(template(examSelectData));
    },
};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        var isUnseal=parseInt(data.isUnseal);
        if(isUnseal==1){
            var unsealCode=data.unsealCode;
            if(unsealCode==null||unsealCode==""){
                tips.info("请输入启封码！");
                return  false;
            }
        }

        var param = {url: baseModule.examRoomApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            examRoom.get();
        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.examRoomApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            examRoom.get();

        })
    },
    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;
        var isUnseal=parseInt(data.isUnseal);
        if(isUnseal==1){
            var unsealCode=data.unsealCode;
            if(unsealCode==null||unsealCode==""){
                tips.info("请输入启封码！");
                return  false;
            }
        }
        var param = {url: baseModule.examRoomApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            examRoom.get();
            closeLay();
        })
    },
    close: function () {   //关闭按钮
        closeLay();
    },
    sendNote: function () {
        var request = ajax.put({url: baseModule.examRoomApi + '/sendNote/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            examRoom.get();
            closeLay();
        })
    },
    finish: function () {
        var request = ajax.put({url: baseModule.examRoomApi + '/finish/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            examRoom.get();
            closeLay();

        })
    },
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
            id: 'examRoom-page',
            callback: function (api) {
                index = api.getCurrent();
                examRoom.get();
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
    examRoom.get();
};

var helper = {
    isUnseal: function (_isUnseal) {
        switch (parseInt(_isUnseal)) {
             case 0:
                 return "否";
             case 1:
                 return "是";
         }
    },
    examStatus: function (_status) {
        switch (parseInt(_status)) {
            case 0:
                return "未开始";
            case 1:
                return "进行中";
            case 2:
                return "已结束";
        }
    },
    status: function (_status) {
        switch (parseInt(_status)) {
            case 0:
                return "开启";
            case 1:
                return "关闭";

        }
    },
};

var tool = {
    translate:function (model) {
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
    ,
};