var index = 1, size = 6, type = '2', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [], number = ' ', optId = '';
var config = {
    read: '../../form/_vacateRead.html',
    permit: '../../form/_vacatePermit.html',
    title: '会议请假管理',
}

$(function () {
    if (auth.check(this)) {
        //1自适应（默认加载方法）
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        vacate.get();
        optId = url.getUrlParam("approvalId");
        if (optId != "" && optId != null) {
            if (auth.checkFun('vacate.permit'))
                return false;
            vacate.permit(null, optId);
        }
    }
})
//2
var vacate = {
    get: function () {
        var param = {url: baseModule.vacateApi + '/' + index + '-' + size + '-' + type + '-' + number};
        var request = ajax.get(param);
        request.done(function (d) {
            pageData = d.result.data;//测试数据
            render.page();
            totalPage = d.result.totalPage;//总页数
            totalCount = d.result.totalCount;//总记录数
            if (d.result.totalPage > 1) {
                page.init(d.result.totalPage, d.result.totalCount);
            } else {
                $('.list-page').empty();
            }
            auth.show();
        })
    },
    permit: function (event, approvalId) {

        var model = '';
        if (event != null) {
            if (auth.refuse(event))
                return false;
            optId = getId(event);//获取当前id的值
            model = result.get(pageData, optId);
        }
        if (event == null) {
            optId = approvalId;
            var param = {url: baseModule.vacateApi + '/info/' + optId, async: false};
            var request = ajax.get(param);
            request.done(function (d) {
                model = d.result;
            })
        }

        openLay({url: config.permit, fun: "opt.permit();", title: config.title});//打开的窗口

        tool.translate(model);
        $('#number').attr("readonly", "readonly");//禁止修改学号

    },
    read: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        // openLay({url:config.form,fun:"opt.update();",title:config.title});
        openLay({url: config.read, fun: 'opt.release();', title: config.title, enter: "确定"});
        $("#opt-dialog-enter").hide();
        var model = result.get(pageData, optId);
        // form.set(model);
        $("#opinion").html(model['opinion']);
        tool.translate(model);

    },
    //查询
    select: function (event) {
        number = $.trim($('#select-key').val());
        index = 1;
        vacate.get();
    },

}

// 3数据渲染
var render = {
    page: function () {
        var template = doT.template($("#vacate-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    }
}

// 4数据操作
var opt = {
    permit: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;

        var param = {url: baseModule.vacateApi + '/permit', data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            vacate.get();
            closeLay();
            render.page(); //更新数据后重新渲染
        })
    },

    close: function () {   //关闭按钮
        closeLay();
    }
}

// 5分页
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
            id: 'course-page',
            callback: function (api) {
                index = api.getCurrent();
                schedule.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
}


// 6视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}

function pageChange(event) {
    size = $(event).val();
    index = 1;
    schedule.get();
}

var helper = {
    status: function (_status) {
        switch (parseInt(_status)) {
            case 1:
                return "未审";
            case 2 :
                return "已审";
        }
    },
    result: function (_result) {
        switch (parseInt(_result)) {
            case 1 :
                return "同意";
            case 2 :
                return "不同意";
        }
        if (_result == undefined) {
            return " 无";
        }
    },
    type: function (_type) {
        if (_type == 1) {
            return "集中学习请假";
        }
        if (_type == 2) {
            return "会议请假";
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