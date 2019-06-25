var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [];
var config = {
    form: '../../form/_articleTopic.html',
    title: '文章主题类型管理',
}

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        articleTopic.get();
    }
})

var articleTopic = {
    get: function () {
        var param = {url: baseModule.articleTopicApi + '/' + index + '-' + size + '-' + key};
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
    // template:function () {
    //     window.location.href = '/excel/course.xlsx';
    // },
    // import:function () {
    //     openLay({url:config.import,fun:'opt.import();',title:config.importTitle});
    // },
    create: function (event) {
        if (auth.refuse(event))
            return false;
        openLay({url: config.form, fun: 'opt.create();', title: config.title});
    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除文章主题类型数据？', fun: "opt.delete();"});
    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.form, fun: "opt.update();", title: config.title});
        var model = result.get(pageData, optId);
        form.set(model);
    },
    //查询
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        articleTopic.get();
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
    // import:function(_type, _message){
    //     var iHtml = '';
    //     if(_type == 0){
    //         iHtml = '<i data-toggle="tooltip" data-placement="bottom" title="导入失败\n '+_message+'" class="fa fa-question-circle" style="color: red;"></i>';
    //     }else{
    //         iHtml = '<i data-toggle="tooltip" data-placement="bottom" title="导入成功" class="fa fa-check-circle" style="color: green;"></i>';
    //     }
    //     return iHtml;
    // },
}

// 数据操作
var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;

        var param = {url: baseModule.articleTopicApi, data: data};
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
        var request = ajax.delete({url: baseModule.articleTopicApi + '/' + optId});
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
        var param = {url: baseModule.articleTopicApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            closeLay();
            render.page();
        })
    },
    // import:function () {
    //     $('#opt-dialog-enter').attr('disabled','disabled');
    //     $('#opt-dialog-enter').html('导入中请勿关闭当前窗口');
    //     var len = $(".import-index").length;
    //     var tem = 0;
    //     var ok = 0;
    //
    //     $(".import-index").queue(function() {
    //         var index = $(this).attr('data-index');
    //         $.ajax({
    //             type: 'POST',
    //             url:baseModule.courseApi + '/import',
    //             data: JSON.stringify(itemData[index]),
    //             contentType: "application/json;charset=utf-8",
    //             headers: {
    //                 "Access-Control-Allow-Origin": "*",
    //                 "token": (typeof store_user_info) == 'undefined' ? '' : store_user_info.token
    //             },
    //             error: function (d) {
    //                 d = JSON.parse(d.responseText);
    //                 $('.import-index').eq(index).html(render.import(0, d.message));
    //                 renderTip();
    //                 tem += 1;
    //                 if (len == tem) {
    //                     renderTip();
    //                     index = 1;
    //                     course.get();
    //                     tips.ok("成功导入[" + ok + "]条数据");
    //                     $('#opt-dialog-enter').removeAttr('disabled');
    //                     $('#opt-dialog-enter').html('操作');
    //                 }
    //             },
    //             success: function (d) {
    //                 $(".import-index").dequeue();
    //                 $('.import-index').eq(index).html(render.import());
    //                 $(".import-index").dequeue();
    //                 if ($(".import-index").queue().length == 0) {
    //                     tem += 1;
    //                     ok += 1;
    //                     if (len == tem) {
    //                         renderTip();
    //                         index = 1;
    //                         course.get();
    //                         tips.ok("成功导入[" + ok + "]条数据")
    //                         $('#opt-dialog-enter').removeAttr('disabled');
    //                         $('#opt-dialog-enter').html('操作');
    //                     }
    //                 }
    //             }
    //         });
    //     })
    // },
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
    course.get();
}

// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}