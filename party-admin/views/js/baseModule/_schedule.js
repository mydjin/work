var index = 1, size = 6, key = '1', totalPage = 0, totalCount = 0, topic = " ", type = "1";
var baseData = [], pageData = [];
var note = 2, push = 2, isRelease = 2;
var config = {
    form: '../../form/_schedule.html',//表单地址
    read: '../../form/_scheduleRead.html',
    cancel: '../../form/_scheduleCancel.html',
    modify: '../../form/_scheduleModify.html',
    title: '课程安排',
    map: '../../form/map/choose.html',
}

$(function () {
    if (auth.check(this)) {
        //1自适应（默认加载方法）
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        schedule.getNoteValue();
        schedule.getPushValue();
        schedule.get();
    }
})
//2
var schedule = {
    getNoteValue: function () {
        //获取是否允许发送短信，若是1-允许，则渲染发送短信的tr
        var requestNote = ajax.get({url: toolModule.getDicFieldNameApi + '/' + 'note'});
        requestNote.done(function (d) {
            var data = [];
            data = d.result;
            if (data.length != 0) {
                note = data[0].value;
            }
        });
    },

    getPushValue: function () {
        //获取是否允许微信推送，若是1-允许，则渲染微信推送的tr
        var requestNote = ajax.get({url: toolModule.getDicFieldNameApi + '/' + 'push'});
        requestNote.done(function (d) {
            var data = [];
            data = d.result;
            if (data.length != 0) {
                push = data[0].value;
            }
        });
    },

    get: function () {
        var param = {url: baseModule.scheduleApi + '/' + index + '-' + size + '-' + type + '-' + topic};
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
            //console.log(d);
        })
    },

    create: function (event) {
        if (auth.refuse(event))
            return false;
        // openLay({url:config.form,fun:'opt.create();',title:config.title});
        openLay({url: config.form, fun: 'opt.create();', title: config.title, enter: "保存"});
        render.showNoteSelect();
        render.showPushSelect();
        createTree(toolModule.getBranchCatalogueApi, "#rangeTree", true, true); //创建

    },
    chooseMap: function (event) {
        var val = $("#position").val();
        var postion = [];
        if (val == null || val == "") {
            val = "108.233510,22.840763";
        }
        postion = val.split(",");

        openMapLay({url: config.map, fun: 'opt.saveMap();', title: '选择会议地点位置', enter: "保存"});
        AMapUI.loadUI(['misc/PositionPicker'], function (PositionPicker) {
            var map = new AMap.Map('map-container', {
                center: postion,
                zoom: 17,
                scrollWheel: false
            })
            var positionPicker = new PositionPicker({
                mode: 'dragMap',
                map: map
            });

            positionPicker.on('success', function (positionResult) {
                document.getElementById('map-lnglat').innerHTML = positionResult.position;
                document.getElementById('map-address').innerHTML = positionResult.address;
                document.getElementById('map-nearestJunction').innerHTML = positionResult.nearestJunction;
                document.getElementById('map-nearestRoad').innerHTML = positionResult.nearestRoad;
                document.getElementById('map-nearestPOI').innerHTML = positionResult.nearestPOI;
            });
            positionPicker.on('fail', function (positionResult) {
                document.getElementById('map-lnglat').innerHTML = ' ';
                document.getElementById('map-address').innerHTML = ' ';
                document.getElementById('map-nearestJunction').innerHTML = ' ';
                document.getElementById('map-nearestRoad').innerHTML = ' ';
                document.getElementById('map-nearestPOI').innerHTML = ' ';
            });
            var onModeChange = function (e) {
                positionPicker.setMode(e.target.value)
            }

            var dragMapMode = document.getElementsByName('mode')[0];
            var dragMarkerMode = document.getElementsByName('mode')[1];


            AMap.event.addDomListener(dragMapMode, 'change', onModeChange)
            AMap.event.addDomListener(dragMarkerMode, 'change', onModeChange);
            positionPicker.start();
            map.panBy(0, 1);

            map.addControl(new AMap.ToolBar({
                liteStyle: true
            }))
        });

    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        // console.log("id:" + optId);
        tips.confirm({message: '是否要删除课程安排数据？', fun: "opt.delete();"});
    },
    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        // openLay({url:config.form,fun:"opt.update();",title:config.title});//打开的窗口
        openLay({url: config.form, fun: 'opt.update();', title: config.title, enter: "确认"});
        var model = result.get(pageData, optId);

        isRelease = model.isRelease;
        if (model.isRelease == 1) {
            render.showNoteInfo();
            render.showPushInfo();
            if (note == 1) {
                document.getElementById('isNote').value = helper.isNote(model['isNote']);
            }
            if (push == 1) {
                document.getElementById('isPush').value = helper.isNote(model['isPush']);
            }
            createTree(toolModule.getBranchCatalogueApi, "#rangeTree", false, true, model['range']); //创建
        } else {
            render.showNoteSelect();
            render.showPushSelect();
            createTree(toolModule.getBranchCatalogueApi, "#rangeTree", true, true, model['range']); //创建
        }

        form.set(model);


    },
    release: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        tips.confirm({message: '是否要发布该课程？', fun: "opt.release();", enter: "发布"});


    },
    read: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        // openLay({url:config.form,fun:"opt.update();",title:config.title});
        openLay({url: config.read, fun: "", title: config.title, enter: "确定"});
        $('#opt-dialog-enter').hide();

        var model = result.get(pageData, optId);
        //console.log(model);
        $("#content").html(model['content']);
        $("#lastContent").html(model['lastContent']);//文本
        createTree(toolModule.getBranchCatalogueApi, "#rangeTree", false, true, model['range']); //创建

        render.showNoteInfo();
        render.showPushInfo();
        if (note == 1) {
            document.getElementById('isNote').value = helper.isNote(model['isNote']);
        }
        if (push == 1) {
            document.getElementById('isPush').value = helper.isNote(model['isPush']);
        }

        tool.translate(model);
    },
    cancel: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.cancel, fun: 'opt.cancel();', title: config.title, enter: "确认取消"});
        var model = result.get(pageData, optId);

        tool.translate(model);
    },
    modify: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        // openLay({url:config.form,fun:"opt.update();",title:config.title});//打开的窗口
        openLay({url: config.modify, fun: 'opt.modify();', title: config.title, enter: "确认修改"});
        var model = result.get(pageData, optId);

        createTree(toolModule.getBranchCatalogueApi, "#rangeTree", false, true, model['range']); //创建
        render.showNoteInfo();
        render.showPushInfo();

        if (note == 1) {
            document.getElementById('isNote').value = helper.isNote(model['isNote']);
        }
        if (push == 1) {
            document.getElementById('isPush').value = helper.isNote(model['isPush']);
        }

        form.set(model);


    },
    //查询
    select: function (event) {
        topic = $.trim($('#topic').val());
        index = 1;
        schedule.get();
    }
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

// 3数据渲染
var render = {
    page: function () {
        var template = doT.template($("#schedule-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
    showNoteSelect: function () {
        //如果启用短信提醒功能，则显示短信提醒的选项，否则不显示
        if (note == 1) {
            var template = doT.template($("#note-select-template").text());
            $('#note').html(template());
        }
    },
    showNoteInfo: function () {
        if (note == 1) {
            var template = doT.template($("#note-info-template").text());
            $('#note').html(template());
        }
    },
    showPushSelect: function () {
        //如果启用微信推送功能，则渲染微信推送的选项
        if (push == 1) {
            var template = doT.template($("#push-select-template").text());
            $('#push').html(template());
        }
    },
    showPushInfo: function () {
        if (push == 1) {
            var template = doT.template($("#push-info-template").text());
            $('#push').html(template());
        }
    },

}

// 4数据操作
var opt = {
    saveMap: function () {   //关闭按钮
        var position = document.getElementById('map-lnglat').innerHTML;
        var address = document.getElementById('map-address').innerHTML;
        $("#position").val(position);
        $("#remark").val(address);
        closeMapLay();
    },
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        var range = getCheckedNodesToString("rangeTree");
        if (range == "" || range == null) {
            return false;
        }
        data['range'] = range;
        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);


        data['type'] = 1;//1-课程安排（集中学习）
        //  console.log(data);

        var param = {url: baseModule.scheduleApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            schedule.get();
            // render.page();
        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.scheduleApi + '/' + optId});
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
        if (isRelease != 1) {
            var range = getCheckedNodesToString("rangeTree");
            if (range == "" || range == null) {
                return false;
            }
            data['range'] = range;
        }
        data['range'] = range;
        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);
        data['type'] = 1;//1-课程安排（集中学习）
        // console.log(data)

        var param = {url: baseModule.scheduleApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            closeLay();
            schedule.get();
            // render.page(); //更新数据后重新渲染
        })
    },
    release: function () {
        // var data = form.get("#opt-form");
        // if (form.verify(data))
        //     return false;
        // data['id'] = optId;

        //tips.info("后台发布中，请稍候！");

        var param = {url: baseModule.scheduleApi + "/release/" + optId};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            schedule.get();
            closeLay();
            // 发布推送
            var request = ajax.put({url: baseModule.scheduleApi + '/releaseSend/' + optId}); //短信推送发布
            request.done(function (d) {
                tips.ok(d.message);
            })

            // render.page(); //更新数据后重新渲染
        })
    },
    cancel: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;
        var param = {url: baseModule.scheduleApi + "/cancel/", data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            schedule.get();
            closeLay();
            // 发布推送
            var request = ajax.put({url: baseModule.scheduleApi + '/cancelSend/' + optId}); //短信推送发布
            request.done(function (d) {
                tips.ok(d.message);
            })

        })
    },
    modify: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        data['id'] = optId;
        //tips.info("后台修改中，请稍候！");
        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);

        var param = {url: baseModule.scheduleApi + '/modify', data: data};
        var request = ajax.post(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            schedule.get();
            closeLay();
            // 发布推送
            var request = ajax.post({url: baseModule.scheduleApi + '/modifySend/' + optId}); //短信推送发布
            request.done(function (d) {
                tips.ok(d.message);
            })

            // render.page(); //更新数据后重新渲染
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
                return "未发布";
            case 2 :
                return "未开始";
            case 3 :
                return "已开始";
            case 4 :
                return "已取消";
            case 5 :
                return "已结束";
        }
    },
    checkType: function (_checkType) {
        switch (parseInt(_checkType)) {
            case 1 :
                return "无";
            case 2 :
                return "心得体会";
        }
    },
    type: function (_type) {
        switch (parseInt(_type)) {
            case 1 :
                return "集中学习";
            case 2 :
                return "会议管理";
        }
    },

    isNote: function (_isNote) {
        switch (parseInt(_isNote)) {
            case 1 :
                return '否';
            case 2 :
                return '是';
        }
    },

    isPush: function (_isPush) {
        switch (parseInt(_isPush)) {
            case 1 :
                return "否";
            case 2 :
                return "是";
        }
    }
}