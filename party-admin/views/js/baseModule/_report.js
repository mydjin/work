var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var optId = '';

var config = {
    form: '../../form/_report_manage.html',
    form_read: '../../form/_report_read.html',
    approval: '../../form/_report_approval.html',
    docPreview: '../../form/_docPreview.html',
    previewTitle: '思想汇报预览',
    title: '思想汇报管理',
    importTitle: "导入"
};

$(function () {

    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        report.get();

        optId = url.getUrlParam("approvalId");
        var stage = url.getUrlParam("stage");
        if (optId != "" && optId != null && stage != "" && stage != null) {
            if (auth.checkFun('report.approval'))
                return false;
            report.approval(null, optId, parseInt(stage));
        }
    }

});

var report = {
    get: function () {
        var param = {url: baseModule.reportManageApi + '/' + index + '-' + size + '-' + key};
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
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title});
    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除模板？', fun: "opt.delete();"});
    },
    read: function (event) {

        optId = getId(event);//获取当前id的值
        // openLay({url: config.approval, fun: "opt.approval();", title: config.title});//打开的窗口
        var approvalTableId = getData(event, "data-approvalTableId");
        var model = result.find(pageData, "approvalTableId", approvalTableId);
        var stage = getData(event, "data-stage");//获取当前审核阶段
        if (stage == 1) {//初审
            openLay({url: config.form_read, fun: "opt.approval();", title: config.title, enterHidden: true});//打开的窗口
            // 文档预览

            //点击按钮打开弹窗
            var url = "https://view.officeapps.live.com/op/view.aspx?src=" + model['fileURL'];
            var previewBtn = "<span class='btn btn-primary' onclick='report.docPreview(\"" + url + "\")'>预览</span>";
            $("#info").html(previewBtn);

        } else if (stage == 2) {//复审，审图片
            openLay({url: config.form_read, fun: "opt.recheckApproval();", title: config.title, enterHidden: true});//打开的窗口
            this.getPic(approvalTableId);
        }
        model['referTime'] = time.timestamp2shortdate(model['referTime']);//时间
        form.set(model);

        $("#status").html(helper.status(model['status']));//调用helper
        $("#result").html(helper.result(model['result']));//调用helper方法传值
        $("#stage").html(helper.stage(model['stage']));//调用helper方法传值
        $("#type").html(helper.type(model['type']));//调用helper方法传值
        $("#opinion").html(model['opinion']);//文本

        $("#referTime").html(model['referTime']);
        report.get();    //刷新修改后的数据

    },
    approval: function (event, approvalId, stageId) {

        var stage = 0;
        var model = '';
        var approvalTableId = '';


        if (event != null) {
            if (auth.refuse(event))
                return false;
            optId = getId(event);//获取当前id的值
            approvalTableId = getData(event, "data-approvalTableId");
            model = result.find(pageData, "approvalTableId", approvalTableId);
            stage = getData(event, "data-stage");//获取当前审核阶段
        }


        if (event == null) {
            stage = stageId;
            var param = {url: baseModule.reportManageApi + '/info/' + optId + '-' + stage, async: false};
            var request = ajax.get(param);
            request.done(function (d) {
                model = d.result;
                optId = model.id;
                approvalTableId = model.approvalTableId;
            })
        }


        if (stage == 1) {//初审
            openLay({url: config.approval, fun: "opt.approval();", title: config.title});//打开的窗口
            // 文档预览

            //点击按钮打开弹窗
            var url = "https://view.officeapps.live.com/op/view.aspx?src=" + model['fileURL'];
            var previewBtn = "<span class='btn btn-primary' onclick='report.docPreview(\"" + url + "\")'>预览</span>";
            $("#info").html(previewBtn);


        } else if (stage == 2) {//复审，审图片
            openLay({url: config.approval, fun: "opt.recheckApproval();", title: config.title});//打开的窗口
            this.getPic(approvalTableId);
        }


        // var model = result.get(pageData, optId);
        // $("#content").html(model['content']);
        // $("#lastContent").html(model['lastContent']);//文本
        model['referTime'] = time.timestamp2shortdate(model['referTime']);//时间

        form.set(model);

        $("#stage").html(helper.stage(model['stage']));//调用helper方法传值
        $("#type").html(helper.type(model['type']));//调用helper方法传值

        $("#referTime").html(model['referTime']);
        editor.set('#opinion', model['opinion']);

        $('#number').attr("readonly", "readonly");//禁止修改学号
    },

    //查询
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        report.get();
    },

    docPreview: function (url) {
        openPreLay({url: config.docPreview, title: config.previewTitle, enterHidden: true});
        var iframeText = "<iframe src='" + url + "' style='height:1000px;width:900px'></iframe>";
        $("#docModal").html(iframeText);
    },

    getPic: function (_tableId) {
        //通过tableId获取审核的图片
        var param = {url: toolModule.pictureApi + '/tableId/' + _tableId};
        var request = ajax.get(param);
        request.done(function (d) {
            picData = d.result;
            //渲染图片#info
            render.pic();
        })
    },

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
        var template = doT.template($("#course-template").text());
        $('#item-list').html(template(pageData));
    },

    pic: function () {
        var template = doT.template($("#pic-template").text());//获取的模板
        $('#info').html(template(picData));//模板装入数据

        for (var i = 0; i < picData.length; i++) {
            $('#reportImg' + i).fancybox({
                helpers: {
                    title: {
                        type: 'over'
                    }
                }
            });
        }
    },
    alwaysShowImg: function () {
        for (var i = 0; i < picData.length; i++) {
            $('#reportImg' + i).attr("style", "display:block;");
        }
        this.pic();
    }

}

// 数据操作
var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;


        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的referTime，而不是id值的referTimeStr
        data['referTime'] = time.date2timestamp(data['referTime']);

        //以下为将数据提交到接口的操作
        var param = {url: baseModule.reportManageApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            render.page();
            report.get();    //刷新修改后的数据

        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.reportManageApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            pageData = result.delete(pageData, optId);
            render.page();
            totalCount = totalCount - 1;
            page.init(totalPage, totalCount);
        })
    },

    approval: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        if (data['opinion'] == null || data['opinion'] == "") {
            tips.warning("未填写审核意见，请检查");
            return false;
        }
        if (data['result'] == null || data['result'] == "") {
            tips.warning("未填写审核结果，请检查");
            return false;
        }


        data['id'] = optId;

        data['referTime'] = time.date2timestamp(data['referTime']);
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['opinion'] = replaceOutUrl(data['opinion']);

        var param = {url: baseModule.reportManageApi + '/approval', data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            report.get();
            editor.set('#opinion', "");

            closeLay();
            render.page(); //更新数据后重新渲染
        })
    },

    recheckApproval: function () {
        var data = form.get("#opt-form");

        // console.log(data);
        if (form.verify(data))
            return false;
        if (data['opinion'] == null || data['opinion'] == "") {
            tips.warning("未填写审核意见，请检查");
            return false;
        }
        if (data['result'] == null || data['result'] == "") {
            tips.warning("未填写审核结果，请检查");
            return false;
        }

        data['id'] = optId;

        data['referTime'] = time.date2timestamp(data['referTime']);
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['opinion'] = replaceOutUrl(data['opinion']);

        var param = {url: baseModule.reportManageApi + '/recheckApproval', data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            report.get();
            editor.set('#opinion', "");

            closeLay();
            render.page(); //更新数据后重新渲染
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
    approvalStage: function (_approvalStage) {
        switch (parseInt(_approvalStage)) {
            case 1 :
                return "初审";
            case 2 :
                return "复审";
        }
    },
    stage: function (_stage) {
        switch (parseInt(_stage)) {
            case 1 :
                return "积极分子";
            case 2 :
                return "发展对象";
            case 3 :
                return "预备党员";
        }
        if (_stage == undefined) {
            return ""
        }
    },

    type: function (_type) {
        switch (parseInt(_type)) {
            case 1 :
                return "个人心得";
            case 2 :
                return "集中学习考核";
            case 3 :
                return "个人自传";
        }
    },

    status: function (_status) {
        switch (parseInt(_status)) {
            case 1 :
                return "未审";
            case 2 :
                return "已审";
        }
        if (_status == undefined) {
            return "无"
        }
    },

    result: function (_result) {
        switch (parseInt(_result)) {
            case 0:
                return "待审核";
            case 1 :
                return "通过";
            case 2 :
                return "未通过";
            default:
                return "";
        }
        if (_result == undefined) {
            return " "
        }
    }
};