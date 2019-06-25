var index = 1, size = 6, type = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var reportData = [], reportId = '';
var optId = '';

var config = {
    form: '../../form/_attendance.html',
    title: '课程考勤管理',
    importTitle: "导入"
}

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        attendance.get();


    }
})

var attendance = {
    get: function () {
        var param = {url: baseModule.attendanceApi + '/' + index + '-' + size + '-' + type};
        var request = ajax.get(param);
        request.done(function (d) {
            pageData = d.result.data;
            //console.log(pageData);
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

    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title});

    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除此课程考勤记录？', fun: "opt.delete();"});
    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);

        //向富文本控件插入初始值，第一个参数对应html页面的id值，第二个参数是初始值
        editor.set('#content', model['content']);
        editor.set('#lastContent', model['lastContent']);

        form.set(model);
    }

}

// 数据渲染
var render = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        log.d(data)
    },
    page: function () {
        //console.log(pageData);
        var template = doT.template($("#courseAttendance-template").text());
        $('#item-list').html(template(pageData));
    },

}

// 数据操作
var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        //  console.log(data);
        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的referTime，而不是id值的referTimeStr
        data['attendanceTime'] = time.date2timestamp(data['attendanceTime']);
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['surveyCondition'] = replaceOutUrl(data['surveyCondition']);

        //以下为将数据提交到接口的操作
        var param = {url: baseModule.attendanceApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {//由数字转换成文字
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            render.page();
            report.get();    //刷新修改后的数据
            editor.set('#surveyCondition', "");

        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.attendanceApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            pageData = result.delete(pageData, optId);
            render.page();
            totalCount = totalCount - 1;
            page.init(totalPage, totalCount);
        })
    },
    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;

        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的referTime，而不是id值的referTimeStr
        data['attendanceTime'] = time.date2timestamp(data['attendanceTime']);
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['surveyCondition'] = replaceOutUrl(data['surveyCondition']);


        //以下为将数据提交到接口的操作
        var param = {url: baseModule.attendanceApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            report.get();     //刷新修改后的数据
            editor.set('#surveyCondition', "");

            closeLay();
        })
    },

    close: function () {
        closeLay();
    }
}

// 分页
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
            id: 'report-page',
            callback: function (api) {
                index = api.getCurrent();
                report.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
}

function pageChange(event) {
    size = $(event).val();
    index = 1;
    report.get();
}

// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}


var helper = {
    status: function (_stage) {
        switch (parseInt(_stage)) {
            case 1 :
                return "未签到";
            case 2 :
                return "已签到";
            case 3 :
                return "已请假";
        }
    },

    type: function (_type) {
        switch (parseInt(_type)) {
            case 1 :
                return "集中学习";
            case 2 :
                return "会议";

        }
    },

    way: function (_status) {
        switch (parseInt(_status)) {
            case 1 :
                return "二维码";
            case 2 :
                return "位置定位";

        }
    }
}