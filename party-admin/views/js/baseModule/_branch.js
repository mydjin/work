var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var optId = '';

var config = {
    form: '../../form/_branch.html',
    title: '党支部管理',
    import: '../../form/_branchImport.html',
    importTitle: '导入党支部',
};

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        auth.check(this);
        branch.get();
    }
});

var branch = {
    get: function () {
        var param = {url: baseModule.branchApi + '/' + index + '-' + size + '-' + key};
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
    getAcademy: function () {
        var param = {url: baseModule.academyInfoApi, async: false};
        var request = ajax.get(param);
        request.done(function (d) {
            academyData = d.result;
            // console.log(academyData)
            render.academySelect();


        })

    },
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title});
        this.getAcademy();

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
        downFile('../../model/branchImport.xls');

    },

    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除党支部数据？', fun: "opt.delete();"});

    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        this.getAcademy();
        var model = result.get(pageData, optId);

        form.set(model);
    },
    select: function (event) {
        key = $.trim($('#name').val());
        index = 1;
        branch.get();
    },
};

var render = {
    page: function () {
        var template = doT.template($("#branch-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
    academySelect: function () {

        var template = doT.template($("#academy-select-template").text());
        $('#academyCode').html(template(academyData));


    }
};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        //console.log(data);
        var param = {url: baseModule.branchApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            branch.get();

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


        checkImportStatus(baseModule.branchApi + "/importStatus/" + batch);

        importFile("file", baseModule.branchApi + '/import/' + batch,
            function importComplete(evt) {
                $('#opt-dialog-cancel').attr("disabled", false);
                $('#file').attr("disabled", false);
                $('#opt-dialog-enter').attr("disabled", false);
                importCompleteShow(evt);
                branch.get();
            });


    },
    delete: function () {
        var request = ajax.delete({url: baseModule.branchApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            branch.get();

        })
    },
    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;

        var param = {url: baseModule.branchApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            branch.get();
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
            id: 'branch-page',
            callback: function (api) {
                index = api.getCurrent();
                branch.get();
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
    branch.get();
};

var helper = {};

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