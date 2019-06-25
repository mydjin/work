var index = 1, size = 6, key = '', type = '1', number = ' ', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var optId = '';

var config = {
    form: '../../form/_surveyRecordChat.html',
    update: '../../form/_surveyRecordChatUpdate.html',
    approval: '../../form/_surveyRecordChatApproval.html',
    read: '../../form/_surveyRecordChatRead.html',
    title: '考察记录管理',
};

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        surveyRecordChat.get();


    }
});

var surveyRecordChat = {
    get: function () {
        var param = {url: baseModule.surveyRecordChatApi + '/' + index + '-' + size + '-' + type + '-' + number};
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
            // console.log(pageData)
        })
    },

    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title});
    },


    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.update, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);
        editor.set('#surveyCondition', model['surveyCondition']);
        editor.set('#approvalOpinion', model['approvalOpinion']);
        form.set(model);

    },
    approval: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.approval, fun: "opt.approval();", title: config.title});//打开的窗口
        var model = result.get(pageData, optId);
        editor.set('#surveyCondition', model['surveyCondition']);
        editor.set('#approvalOpinion', model['approvalOpinion']);
        tool.translate(model);
        form.set(model);
        $('#number').attr("readonly", "readonly");//禁止修改学号


    },
    read: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.read, fun: "opt.read();", title: config.title, enter: "确定"});//打开的窗口
        $("#opt-dialog-enter").hide();
        var model = result.get(pageData, optId);
        tool.translate(model);//自带form.set(model)
        $("#surveyCondition").html(model['surveyCondition']);

    },
    //查询
    select: function (event) {
        number = $.trim($('#number').val());
        index = 1;
        surveyRecordChat.get();
    }


};

var render = {
    page: function () {
        var template = doT.template($("#surveyRecordChat-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;


        data['surveyTime'] = time.date2timestamp(data['surveyTime']);
        data['type'] = 1;//1-考察记录
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['surveyCondition'] = replaceOutUrl(data['surveyCondition']);

        var param = {url: baseModule.surveyRecordChatApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            surveyRecordChat.get();
            editor.set('#surveyCondition', "");
        })
    },

    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;
        data['type'] = 1;//1-考察记录
        data['surveyTime'] = time.date2timestamp(data['surveyTime']);
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['surveyCondition'] = replaceOutUrl(data['surveyCondition']);

        var param = {url: baseModule.surveyRecordChatApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            surveyRecordChat.get();
            editor.set('#surveyCondition', "");
            closeLay();

        })
    },
    approval: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;

        // data['surveyTime'] = time.date2timestamp(data['surveyTime']);
        var param = {url: baseModule.surveyRecordChatApi + '/approval/', data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            surveyRecordChat.get();
            editor.set('#surveyCondition', "");
            closeLay();

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
            id: 'surveyRecordChat-page',
            callback: function (api) {
                index = api.getCurrent();
                surveyRecordChat.get();
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
    surveyRecordChat.get();
};

var helper = {
    type: function (_type) {
        if (_type == 1) {
            return "考察";
        }
        if (_type == 2) {
            return "谈话";
        }
    },
    approvalStatus: function (_approvalStatus) {
        if (_approvalStatus == 2) {
            return "未审";
        }
        if (_approvalStatus == 3) {
            return "已审";
        }
        if (_approvalStatus == 1) {
            return "待审";
        }
        if (_approvalStatus == undefined) {
            return "无";
        }
    },
    approvalResult: function (_approvalResult) {
        if (_approvalResult == 1) {
            return "通过";
        }
        if (_approvalResult == 2) {
            return "不通过";
        }
        if (_approvalResult == undefined) {
            return "无";
        }
    },
    stage: function (_stage) {
        switch (parseInt(_stage)) {
            case 1:
                return "积极分子";
            case 2:
                return "发展对象";
            case 3:
                return "预备党员";
        }
    },
}
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
}