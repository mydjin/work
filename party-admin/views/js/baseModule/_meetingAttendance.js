var index = 1, size = 6, type = '2', number = ' ', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [], topicData = [];
var attendanceData = [], attendanceId = '';
var optId = '';

var config = {
    form: '../../form/_meetingAttendance.html',
    update: '../../form/_meetingAttendanceUpdate.html',
    form_read: '../../form/_attendanceRead.html',
    title: '会议考勤管理',

}

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        meetingAttendance.get();
    }
})

var meetingAttendance = {
    get: function () {
        var param = {url: baseModule.attendanceApi + '/type/' + index + '-' + size + '-' + type + '-' + number};
        var request = ajax.get(param);
        request.done(function (d) {
            pageData = d.result.data;
            // //console.log(pageData);
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
    //获取会议主题
    getTopic: function () {
        var param = {url: baseModule.scheduleApi + '/byType/' + type, async: false};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            topicData = d.result;
            // console.log(topicData);
            render.topicSelect();
        })
    },
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title, enter: "确认"});

        meetingAttendance.getTopic();
    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除记录？', fun: "opt.delete();"});
    },
    read: function (event) {
        if (auth.refuse(event))
            return false;
        attendanceId = getId(event);
        openLay({url: config.form_read, fun: "", title: '考勤详细信息'});
        $("#opt-dialog-enter").hide();
        var model = result.get(pageData, attendanceId);

        $("#status").html(helper.status(model['status']));//调用helper方法传值
        $("#way").html(helper.type(model['way']));//调用helper方法传值
        $("#type").html(helper.type(model['type']));//调用helper方法传值
        if (model['attendanceTime'] != undefined) {
            model['attendanceTime'] = time.timestamp2shortdate(model['attendanceTime']);//时间
        } else {
            model['attendanceTime'] = " ";
        }

        $("#attendanceTime").html(model['attendanceTime']);
        if (model["status"] == 2) {
            $("#wayTr").attr("style", "display:");
        } else {
            $("#way").val(0);
        }
        meetingAttendance.get();     //刷新修改后的数据
        // form.set(model);
        tool.translate(model);
    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.update, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);
        meetingAttendance.get();     //刷新修改后的数据
        meetingAttendance.getTopic();
        if (model["status"] == 2) {
            $("#wayTr").attr("style", "display:");
        } else {
            $("#way").val(0);
        }
        form.set(model);


    },
    //查询
    select: function (event) {
        number = $.trim($('#number').val());
        index = 1;
        meetingAttendance.get();
    },

    selectStatue: function (event) {
        var indexVal = $(event).val();
        if (indexVal == 2) {
            //已签到的显示签到方式
            $("#wayTr").attr("style", "display:");
            $("#attendanceTimeTr").attr("style", "display:");
        } else {
            //2-未签到和3-已请假不显示签到时间和签到方式
            $("#attendanceTimeTr").attr("style", "display:none");
            $("#wayTr").attr("style", "display:none");
        }
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
        // //console.log(pageData);
        var meetingAttendance = doT.template($("#course-template").text());
        $('#item-list').html(meetingAttendance(pageData));
    },

    //渲染新增时，主题下拉框
    topicSelect: function () {

        var template = doT.template($("#topic-select-template").text());
        $('#scheduleId').html(template(topicData));
    }

}


// 数据操作
var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['type'] = 2;//2-会议考勤
        // console.log(data);

        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的releaseTime，而不是id值的releaseTimeStr
        data['attendanceTime'] = time.date2timestamp(data['attendanceTime']);
        // console.log(data);

        //以下为将数据提交到接口的操作
        var param = {url: baseModule.meetingAttendanceApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            meetingAttendance.get();
            render.page();
        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.meetingAttendanceApi + '/' + optId});
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
        data['type'] = 2;//2-会议考勤
        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的releaseTime，而不是id值的releaseTimeStr
        data['attendanceTime'] = time.date2timestamp(data['attendanceTime']);

        if (data['status'] != 2) {
            //不需要选择签到方式
            data['way'] = 0;
        }
        // console.log(data);
        //以下为将数据提交到接口的操作
        var param = {url: baseModule.meetingAttendanceApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            meetingAttendance.get();     //刷新修改后的数据
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
            id: 'template-page',
            callback: function (api) {
                index = api.getCurrent();
                meetingAttendance.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
}

function pageChange(event) {
    size = $(event).val();
    index = 1;
    meetingAttendance.get();
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
    status: function (_status) {
        switch (parseInt(_status)) {
            case 1 :
                return "未签到";
            case 2 :
                return "已签到";
            case 3 :
                return "已请假";
        }
    },
    way: function (_way) {
        switch (parseInt(_way)) {
            case 0 :
                return "无";
            case 1 :
                return "二维码";
            case 2 :
                return "位置定位";
            default:
                return "";
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
};
