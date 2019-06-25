var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var growthRecordId = '', growthRecordData = [];
var optId = '';

var config = {
    form: '../../form/_growthRecord.html',
    form_read: '../../form/_growthRead.html',
    title: '成长记录管理',
};

$(function () {
    //自适应
    if (auth.check(this)) {
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        growthRecord.get();


    }
});

var growthRecord = {
    get: function () {
        var param = {url: baseModule.growthRecordApi + '/' + index + '-' + size + '-' + key};
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
            ////console.log(pageData);
        })
    },

    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title, enter: "保存"});
    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);
        //向富文本控件插入初始值，第一个参数对应html页面的id值，第二个参数是初始值
        editor.set('#record', model['record']);
        model['recordTime'] = time.timestamp2shortdate(model['recordTime']);//时间
        form.set(model);
        $('#number').attr("readonly", "readonly");//禁止修改学号

    },

    read: function (event) {
        if (auth.refuse(event))
            return false;
        growthRecordId = getId(event);
        openLay({url: config.form_read, fun: "", title: config.title, enter: "确认"});
        $("#opt-dialog-enter").hide();
        var model = result.get(pageData, growthRecordId);

        $("#record").html(model['record']);//文本

        $("#stage").html(helper.stage(model['stage']));//调用helper

        model['recordTime'] = time.timestamp2shortdate(model['recordTime']);//时间
        $("#recordTime").html(model['recordTime']);
        growthRecord.get();    //刷新修改后的数据
        form.set(model);
    },
    //查询
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        growthRecord.get();
    },
};

var render = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        log.d(data)
    },
    page: function () {
        var template = doT.template($("#growthRecord-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },

};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;

        data['recordTime'] = time.date2timestamp(data['recordTime']);

        var param = {url: baseModule.growthRecordApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            render.page();
            growthRecord.get();
        })
    },
    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;

        data['recordTime'] = time.date2timestamp(data['recordTime']);

        var param = {url: baseModule.growthRecordApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            growthRecord.get();
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
            id: 'growthRecord-page',
            callback: function (api) {
                index = api.getCurrent();
                growthRecord.get();
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
    growthRecord.get();
};

var helper = {
    stage: function (_stage) {
        switch (parseInt(_stage)) {
            case 1 :
                return "积极分子";
            case 2 :
                return "发展对象";
            case 3 :
                return "预备党员";
        }
    },
    //1-入党申请书，2-思想汇报，3-学习记录，4-在线学习
    type: function (_type) {
        switch (parseInt(_type)) {
            case 1 :
                return "入党申请书";
            case 2 :
                return "思想汇报";
            case 3 :
                return "学习记录";
            case 4 :
                return "在线学习";
        }
    },
}
