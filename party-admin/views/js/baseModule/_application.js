var index = 1, size = 6, number = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [], picData = [];
var optId = '', stage = '';

var config = {
    approval: '../../form/_applicationApproval.html',//入党申请书审核的弹窗地址
    read: '../../form/_applicationRead.html',
    docPreview: '../../form/_docPreview.html',
    title: '入党申请书管理',
    previewTitle: '入党申请书预览',
}

$(function () {
    if (auth.check(this)) {
        //1自适应（默认加载方法）
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        application.get();

        optId = url.getUrlParam("approvalId");
        var stage = url.getUrlParam("stage");
        if (optId != "" && optId != null && stage != "" && stage != null) {
            if (auth.checkFun('application.approval'))
                return false;
            application.approval(null, optId, parseInt(stage));
        }
        // application.getPictures();
    }

})
//2
var application = {
    get: function () {
        var param = {url: baseModule.applicationApi + '/' + index + '-' + size + '-' + number + '-' + stage};
        var request = ajax.get(param);
        request.done(function (d) {   //
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
    approval: function (event, applicationId, stageId) {
        var stage = 0;
        var model = '';
        if (event != null) {
            if (auth.refuse(event))
                return false;
            optId = getId(event);//获取当前id的值
            stage = getData(event, "data-stage");//获取当前审核阶段
            model = result.find(pageData, "approvalTableId", optId);
        }

        if (event == null) {
            stage = stageId;
            var param = {url: baseModule.applicationApi + '/info/' + applicationId + '-' + stage, async: false};
            var request = ajax.get(param);
            request.done(function (d) {
                model = d.result;
                optId = model.approvalTableId;
            })
        }


        if (stage == 1) {//初审
            openLay({url: config.approval, fun: "opt.approval();", title: config.title/*, enterHidden: true*/});//打开的窗口
            // 文档预览
            //点击按钮打开弹窗
            var url = "https://view.officeapps.live.com/op/view.aspx?src=" + model['fileURL'];
            var previewBtn = "<span class='btn btn-primary' onclick='application.docPreview(\"" + url + "\")'>预览</span>";
            $("#info").html(previewBtn);


        } else if (stage == 2) {//复审，审图片
            openLay({url: config.approval, fun: "opt.recheckApproval();", title: config.title/*, enterHidden: true*/});//打开的窗口
            this.getPic(optId);
        }

        $('#content').html(model['content']);
        // editor.set('#lastContent',model['lastContent']);
        editor.set('#opinion', model['opinion']);
        form.set(model);
        $('#number').attr("readonly", "readonly");//禁止修改学号

    },
    read: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值


        // openLay({url:config.form,fun:"opt.update();",title:config.title});
        openLay({url: config.read, fun: 'opt.read();', title: config.title, enterHidden: true});
        var model = result.find(pageData, "approvalTableId", optId);

        var stage = getData(event, "data-stage");//获取当前审核阶段
        // console.log(stage)
        if (stage == 1) {//初审
            //点击按钮打开弹窗
            var url = "https://view.officeapps.live.com/op/view.aspx?src=" + model['fileURL'];
            var previewBtn = "<span class='btn btn-primary' onclick='application.docPreview(\"" + url + "\")'>预览</span>";
            $("#info").html(previewBtn);

        } else if (stage == 2) {//复审，审图片
            this.getPic(optId);
        }


        // editor.set('#content',model['content']);
        // editor.set('#lastContent',model['lastContent']);
        // editor.set('#opinion',model['opinion']);
        $("#opinion").html(model['opinion']);
        // form.set(model);
        var data = [];
        for (var variable in model) {
            //备份转换前数据
            data[variable] = model[variable];
            //判断helper里是否存在该函数，存在则执行转换
            if (typeof eval('helper.' + variable) == 'function')
                model[variable] = eval('helper.' + variable + '(' + model[variable] + ')');
        }
        // console.log(model);
        form.set(model);
        //恢复回转换前数据
        for (var variable in data)
            model[variable] = data[variable];

    },
    select: function (event) {
        number = $.trim($('#select-number').val());
        stage = $.trim($('#select-stage').val());
        index = 1;
        application.get();
    },

    getPic: function (_tableId) {
        //通过tableId获取审核的图片
        var param = {url: toolModule.pictureApi + '/tableId/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            picData = d.result;
            //渲染图片#info
            render.pic();
        })
    },

    docPreview: function (url) {
        openPreLay({url: config.docPreview, title: config.previewTitle, enterHidden: true});
        var iframeText = "<iframe src='" + url + "' style='height:1000px;width:900px'></iframe>";
        $("#docModal").html(iframeText);
    }
}

// 3数据渲染
var render = {
    // create:function () {
    //     var data = form.get("#opt-form");
    //     if(form.verify(data))
    //         return false;
    //     log.d(data)
    // },
    page: function () {
        var template = doT.template($("#application-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },

    pic: function () {
        var template = doT.template($("#pic-template").text());//获取的模板
        $('#info').html(template(picData));//模板装入数据

        for (var i = 0; i < picData.length; i++) {
            $('#applicationImg' + i).fancybox({
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
            $('#applicationImg' + i).attr("style", "display:block;");
        }
        this.pic();
    }
}

// 4数据操作
var opt = {
    approval: function () {
        var data = form.get("#opt-form");

        if (form.verify(data))
            return false;
        data['id'] = optId;
        if (data['opinion'] == null || data['opinion'] == "") {
            tips.warning("未填写审核意见，请检查");
            return false;
        }
        if (data['result'] == null || data['result'] == "") {
            tips.warning("未填写审核结果，请检查");
            return false;
        }

        data['referTime'] = time.date2timestamp(data['referTime']);

        // var param = {url: baseModule.applicationApi + '/approval', data: data};
        var param = {url: baseModule.applicationApi + '/firstApproval', data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            application.get();
            closeLay();
            render.page(); //更新数据后重新渲染
        })
    },

    recheckApproval: function () {
        var data = form.get("#opt-form");

        //  console.log(data);
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

        // var param = {url: baseModule.applicationApi + '/approval', data: data};
        var param = {url: baseModule.applicationApi + '/recheckApproval', data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            application.get();
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
                application.get();
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
    application.get();
}

var helper = {
    status: function (_status) {
        switch (parseInt(_status)) {
            case 1:
                return "未审核";
            case 2 :
                return "已审核";
            default:
                return "无"
        }
    },
    result: function (_checkType) {
        switch (parseInt(_checkType)) {
            case 0:
                return "待审核";
            case 1 :
                return "通过";
            case 2 :
                return "不通过";
            default:
                return "无";
        }
    },

    stage: function (_stage) {
        switch (parseInt(_stage)) {
            case 1 :
                return "初审";
            case 2 :
                return "复审";
            default:
                return "无";
        }
    }
}

