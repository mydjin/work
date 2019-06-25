var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var imageType = '';
var newFileName = '';
var optId = '';
var config = {
    form: '../../form/_news.html',
    title: '新闻管理',
}

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        news.get();
        cos.init();
    }
})

var news = {
    get: function () {
        var param = {url: baseModule.articleApi + '/' + index + '-' + size + '-' + key};
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
        tips.confirm({message: '是否要删除文章数据？', fun: "opt.delete();"});
    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        //  var model = result.get(pageData,optId);
        var param = {url: baseModule.articleApi + '/info/' + optId, async: false};
        var request = ajax.get(param);
        request.done(function (d) {
            var result = d.result;
            var model = result;//.get(pageData,optId);
            //向富文本控件插入初始值，第一个参数对应html页面的id值，第二个参数是初始值
            editor.set('#content', model['content']);

            form.set(model);

        })
    },


    release: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        // openLay({url: config.form, fun: "opt.release();", title: config.title});
        tips.confirm({message: '是否要发布该条新闻？', fun: "opt.release();", color: "#23b7e5", enter: "发布"});

    },
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        news.get();
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
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['content'] = replaceOutUrl(data['content']);

        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的releaseTime，而不是id值的releaseTimeStr
        data['releaseTime'] = time.date2timestamp(data['releaseTime']);
        //新增时给审核结果的初始值为未审
        data ['auditingStatus'] = 1;
        //新增时给发布状态的初始值为未发布
        data['releaseStatus'] = 1;
        //以下为将数据提交到接口的操作
        var param = {url: baseModule.articleApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            news.get();


        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.articleApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            news.get();

        })
    },
    getURL: function () {
        var param = {url: baseModule.getPictureURLApi + '/' + id};
        var request = ajax.get(param);
        request.done(function (d) {
            urlData = d.result.artworkURL;
            render.page();   //图片界面渲染
        })
    },
    //查询
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        news.get();
    },

    update: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;
        tips.ok("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['content'] = replaceOutUrl(data['content']);

        //因为接口只接收整型的日期格式，所以在提交到接口前将日历控件的日期格式转为整型的
        //注意这时转换的是name值的releaseTime，而不是id值的releaseTimeStr
        data['releaseTime'] = time.date2timestamp(data['releaseTime']);

        //以下为将数据提交到接口的操作
        var param = {url: baseModule.articleApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            news.get();


        })
    },
    release: function () {
        var param = {url: baseModule.articleApi + "/release/" + optId};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            news.get();     //刷新修改后的数据
            closeLay();
        })
    },
    upload: function (file) {
        var _file = document.getElementById(file).files[0];
        var type = _file.type;
        var accept = ["image/gif", "image/jpeg", "image/jpg", "image/png", "image/svg"];
        var flag = false;
        for (var i = 0; i < accept.length; i++) {
            if (accept[i] == type) {
                flag = true;
            }
        }
        if (!flag) {
            tips.error("请上传图片文件！");
            return false;
        }
        newFileName = _file.name;
        newFileName = getSuffix(newFileName) + new Date().getTime() + "." + getSuffix(newFileName);//修改文件名

        var path = 'partySystem/picture/' + newFileName;

        imageType = file;
        cos.uploadFile(path, _file, uploadImageSuccess);
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
            id: 'article-page',
            callback: function (api) {
                index = api.getCurrent();
                news.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
}

function pageChange(event) {
    size = $(event).val();
    index = 1;
    news.get();
}

// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}


//下拉框
var helper = {
    //文章类型
    type: function (_type) {
        switch (parseInt(_type)) {
            case 1 :
                return "通知公告";
            case 2 :
                return "新闻咨讯";

        }
    },

    //发布状态
    releaseStatus: function (_releaseStatus) {
        switch (parseInt(_releaseStatus)) {
            case 1 :
                return "未发布";
            case 2 :
                return "已发布";

        }
    },


    //审核状态

    auditingStatus: function (_auditingStatus) {
        switch (parseInt(_auditingStatus)) {
            case 1 :
                return "未审核";
            case 2 :
                return "已通过";

            case 3 :
                return "未通过";
        }


    },

//是否需要审核
    isNeedAuditing: function (_isNeedAuditing) {
        switch (parseInt(_isNeedAuditing)) {
            case 1 :
                return "是";
            case 2 :
                return "否";


        }
    },

    //是否置顶
    setTopStatus: function (_setTopStatus) {
        switch (parseInt(_setTopStatus)) {
            case 1 :
                return "是";
            case 2 :
                return "否";


        }
    },


}

var uploadImageSuccess = function (result) {
    loading.hide();
    var data = result.data;

    var access_url = data.source_url.replace("http:", "").replace("https:", "");
    var path = data.resource_path;
    $('#artworkURL').attr('src', access_url);
    $('#artworkURL').attr('data-path', cos.fileName(access_url));

    var pictureData = {};
    pictureData['name'] = newFileName;
    pictureData['artworkURL'] = access_url;
    pictureData['path'] = path;
    pictureData['describe'] = "公告图片";
    pictureData['tableId'] = '0';
    var param = {url: toolModule.pictureApi, data: pictureData};
    var request = ajax.post(param);
    request.done(function (d) {
        $("#pictureId").val(d.result.id);
    })
};

