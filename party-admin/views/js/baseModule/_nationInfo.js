var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var nationInfoData = [], nationInfoId = '';
var optId = '';

var config = {
    form: '../../form/_nationInfo.html',
    form_read: '../../form/_nationRead.html',
    title: '民族信息管理',
    import: '../../form/_nationImport.html',
    importTitle: '导入民族',
};

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        nationInfo.get();


    }
});

var nationInfo = {
    get: function () {
        var param = {url: baseModule.nationInfoApi + '/' + index + '-' + size + '-' + key};
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
    import: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.import, fun: 'opt.import();', title: config.importTitle, enter: "导入"});

    },
    exportError: function () {
        $("#error-table").tableExport({type: 'excel', fileName: 'exportError'});
    },
    importModel: function (event) {
        if (auth.refuse(event))
            return false;
        downFile('../../model/nationImport.xls');

    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除民族信息数据？', fun: "opt.delete();"});

    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);

        // //向富文本控件插入初始值，第一个参数对应html页面的id值，第二个参数是初始值
        // editor.set('#detail',model['detail']);

        form.set(model);
    },
    //查询
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        nationInfo.get();
    },
    // read:function (event) {
    //     if (auth.refuse(event))
    //         return false;
    //     nationInfoId = getId(event);
    //     openLay({url:config.form_read, fun:"", title: '民族详细信息'});
    //     $("#opt-dialog-enter").hide();
    //     var model = result.get(pageData, nationInfoId);
    //
    //     form.set(model);
    // },
};

var render = {
    page: function () {
        var template = doT.template($("#nationInfo-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;

        var param = {url: baseModule.nationInfoApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            nationInfo.get();

        })
    },
    import: function () {

        if (!xls_check("file")) {
            return false;
        }
        //console.log(data);debugger;
        $('#opt-dialog-cancel').attr("disabled", true);
        $('#file').attr("disabled", true);
        $('#opt-dialog-enter').attr("disabled", true);
        $('#todoBar').empty();
        $('#todoPer').empty();
        $('#mark').empty();
        $('#res').empty();


        var batch = getToken();

        initImportStatus();

        checkImportStatus(baseModule.nationInfoApi + "/importStatus/" + batch);

        importFile("file", baseModule.nationInfoApi + '/import/' + batch,
            function importComplete(evt) {
                $('#opt-dialog-cancel').attr("disabled", false);
                $('#file').attr("disabled", false);
                $('#opt-dialog-enter').attr("disabled", false);
                importCompleteShow(evt);
                nationInfo.get();
            });


    },
    delete: function () {
        var request = ajax.delete({url: baseModule.nationInfoApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            nationInfo.get();

        })
    },
    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;

        var param = {url: baseModule.nationInfoApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            nationInfo.get();

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
            id: 'nationInfo-page',
            callback: function (api) {
                index = api.getCurrent();
                nationInfo.get();
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
    nationInfo.get();
};

var helper = {
    isDel: function (_isDel) {
        switch (parseInt(_isDel)) {
            case 0:
                return "有效";
            case 1:
                return "已删除";
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