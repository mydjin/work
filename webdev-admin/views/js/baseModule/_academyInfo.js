var index = 1, size = 6, key = '', code = ' ', name = ' ', campus = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var config = {
    form: '/form/_academyInfo.html',
    title: '部门信息管理'

}

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        academyInfo.get();
    }
})

var academyInfo = {
    get: function () {
        var param = {url: baseModule.academyInfoApi + '/' + index + '-' + size + '-' + name + '-' + code + '-' + campus};
        var request = ajax.get(param);
        request.done(function (d) {
            pageData = d.result.data;
            render.page();
            totalPage = d.result.totalPage;
            totalCount = d.result.totalCount;
            page.init(d.result.totalPage, d.result.totalCount);
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
        tips.confirm({message: '是否要删除部门数据？', fun: "opt.delete();"});
    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);
        form.set(model);
    },
    select: function (event) {
        code = $.trim($('#select-code').val()).replace('-', '');
        name = $.trim($('#select-name').val()).replace('-', '');
        campus = $.trim($('#select-campus').val()).replace('-', '');
        index = 1;
        academyInfo.get();
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
        var template = doT.template($("#course-template").text());
        $('#item-list').html(template(pageData));
    },

}

// 数据操作
var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['status'] = 0;
        var param = {url: baseModule.academyInfoApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            render.page();
        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.academyInfoApi + '/' + optId});
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
        var param = {url: baseModule.academyInfoApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            closeLay();
            render.page();
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
            pageArray: [size,6, 12, 24, 48],
            totalCount: _total,
            id: 'course-page',
            callback: function (api) {
                index = api.getCurrent();
                course.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
}

function pageChange(event) {
    size = $(event).val();
    index = 1;
    academyInfo.get();
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
            //部门状态
            case 0 :
                return '有效';
            case 1 :
                return '无效';

        }
    },
    campus: function (_campus) {
        switch (parseInt(_campus)) {
            case 1 :
                return '东校区';
            case 2 :
                return '西校区';
            case 3 :
                return '武鸣校区';


        }
    },
}