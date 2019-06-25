var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var imageType = '';
var newFileName = '';
var baseData = [], pageData = [], urlData = [];
var onlineCourseData = [], onlineCourseId = '';
var optId = '';
var picMd5 = '';
//var note = 2 , push = 2,isRelease=2;

var config = {
    form: '../../form/_onlineCourse.html',
    // approval:'../../form/_onlineCourseApproval.html',
    //  release:'../../form/_onlineCourseRelease.html',
    form_read: '../../form/_onlineCourseRead.html',
    title: '网络课程管理',
};

$(function () {
    if (auth.check(this)) {
        //自适应
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        onlineCourse.get();
        // onlineCourse.getNoteValue();
        //  onlineCourse.getPushValue();

    }
});

var onlineCourse = {
    /* getNoteValue: function () {
         //获取是否允许发送短信，若是1-允许，则渲染发送短信的tr
         var requestNote = ajax.get({url:toolModule.getDicFieldNameApi+'/'+  'note'});
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
         var requestNote = ajax.get({url:toolModule.getDicFieldNameApi+'/'+  'push'});
         requestNote.done(function (d) {
             var data = [];
             data = d.result;
             if (data.length != 0) {
                 push = data[0].value;
             }
         });
     },*/
    get: function () {
        var param = {url: baseModule.onlineCourseApi + '/' + index + '-' + size + '-' + key};
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
        tips.confirm({message: '是否要删除网络课程数据？', fun: "opt.delete();"});

    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.form, fun: "opt.update();", title: config.title, enter: "保存"});
        var model = result.get(pageData, optId);

        document.getElementById('faceImageShow').src = model['artworkURL'];

        form.set(model);


    },
    read: function (event) {
        if (auth.refuse(event))
            return false;
        onlineCourseId = getId(event);
        openLay({url: config.form_read, fun: "", title: '网络课程详细信息'});
        $("#opt-dialog-enter").hide();
        var model = result.get(pageData, onlineCourseId);

        document.getElementById('artworkURL').src = model['artworkURL'];
        tool.translate(model)
    },
    //查询
    select: function (event) {
        key = $.trim($('#select-key').val());
        index = 1;
        onlineCourse.get();
    },

    //发布
    release: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        tips.confirm({message: '是否要发布该课程？', fun: "opt.release();", enter: "发布"});


    },
    //重定向到章节管理
    redirectChapterSection: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        window.location.href = server_root + 'my/core/chapterSection?key=' + optId;
    },
};

var render = {
    page: function () {
        var template = doT.template($("#onlineCourse-template").text());//获取的模板
        $('#item-list').html(template(pageData));//模板装入数据
    },
    /*showNoteSelect: function () {
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
    },*/

};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;

        /*  var range =getCheckedNodesToString("rangeTree");
          if(range==""||range==null){
              return false;
          }*/
        data['range'] = range;
        data['result'] = '1';
        data['status'] = '2';
        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);

        var param = {url: baseModule.onlineCourseApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            pageData.push(d.result);
            totalCount = totalCount + 1;
            page.init(totalPage, totalCount);
            render.page();
            onlineCourse.get();
        })
    },
    delete: function () {
        var request = ajax.delete({url: baseModule.onlineCourseApi + '/' + optId});
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

        /*if(isRelease!=1) {
            var range =getCheckedNodesToString("rangeTree");
            if(range==""||range==null){
                return false;
            }
            data['range']=range;

        }*/

        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);

        var param = {url: baseModule.onlineCourseApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            pageData = result.update(pageData, d.result, 'id');
            onlineCourse.get();
            closeLay();
        })
    },

    release: function () {
        //  closeLay();
        //tips.info("后台发布中，请稍候！");
        var param = {url: baseModule.onlineCourseApi + '/publish/' + optId};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            //pageData = result.update(pageData, d.result, 'id');
            onlineCourse.get();
            closeLay();
            //render.page(); //更新数据后重新渲染
            /* if(note==1||push==1) {
                 var request =     ajax.put({url: baseModule.onlineCourseApi + '/publishSend/' + optId}); //短信推送发布
                 request.done(function (d) {
                     tips.ok(d.message);
                 })
             }*/
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

        browserMD5File(_file, function (err, md5) {
                picMd5 = md5;
                // console.log("本地图片md5值："+md5); // 97027eb624f85892c69c4bcec8ab0f11
                var param = {url: toolModule.checkPicApi + '/' + picMd5};
                var request = ajax.get(param);
                request.done(function (d) {
                    //console.log(d);
                    picData = d.result;
                    if (picData.id) {
                        //console.log("服务器已存在该图片，md5值为："+picData.md5)
                        $("#pictureId").val(picData.id);
                        document.getElementById('faceImageShow').src = picData.artworkURL;
                    } else {
                        // console.log("正常上传，md5值为："+picMd5)
                        newFileName = _file.name;
                        newFileName = getSuffix(newFileName) + new Date().getTime() + "." + getSuffix(newFileName);//修改文件名

                        var path = 'partySystem/picture/' + newFileName;

                        imageType = file;
                        cos.uploadFile(path, _file, uploadImageSuccess);
                    }
                })
            }, function (progess) {
                var checkStr = "文件加载：" + progess + '%';
                if (progess == 100) {
                    checkStr = "文件加载：完成";
                }
                tips.ok(checkStr);

            }
        );
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
            id: 'onlineCourse-page',
            callback: function (api) {
                index = api.getCurrent();
                onlineCourse.get();
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
    onlineCourse.get();
};

var helper = {
    isRelease: function (_isRelease) {
        switch (parseInt(_isRelease)) {
            case 1:
                return "发布";
            case 2:
                return "未发布";
        }
    },
    result: function (_result) {
        switch (parseInt(_result)) {
            case 1:
                return "通过";
            case 2:
                return "未通过";
        }
    },
    status: function (_status) {
        switch (parseInt(_status)) {
            case 1:
                return "未审";
            case 2:
                return "已审";
        }
    },
    isReview: function (_isReview) {
        switch (parseInt(_isReview)) {
            case 1:
                return "允许";
            case 2:
                return "不允许";
        }
    },
    isEssence: function (_isEssence) {
        switch (parseInt(_isEssence)) {
            case 1:
                return "精华";
            case 2:
                return "非精华";
        }
    },
    property: function (_property) {
        switch (parseInt(_property)) {
            case 1:
                return "必修";
            case 2:
                return "选修";
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


var uploadImageSuccess = function (result) {
    loading.hide();
    var data = result.data;
    // console.log(result);

    var access_url = data.source_url.replace("http:", "").replace("https:", "");
    var path = data.resource_path;
    $('#' + imageType + 'Show').attr('src', access_url);
    $('#' + imageType + 'Show').attr('data-path', cos.fileName(access_url));

    var pictureData = {};
    pictureData['path'] = path;
    pictureData['name'] = newFileName;
    pictureData['artworkURL'] = access_url;
    pictureData['describe'] = "网络课程封面";
    pictureData['code'] = picMd5;
    var param = {url: toolModule.pictureApi, data: pictureData};
    var request = ajax.post(param);
    request.done(function (d) {
        $("#pictureId").val(d.result.id);
    })
};