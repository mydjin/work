var key = '', optId = '';
var baseData = [];
;var videoMd5 = '';
var newFileName = '';
var videoData = {};
var uploadFlag = false;

var config = {
    chapterform: '../../form/_chapter.html',
    chapterview: '../../form/_chapterRead.html',
    section: '../../form/_section.html',
    sectionView: '../../form/_sectionRead.html',
    courseNote: '../../form/_onlineCourseNote.html',
    chapterSectionNote: '../../form/_chapterSectionNote.html',
    courseRead: '../../form/_onlineCourseRead.html',

    title: '章节管理',
}

$(function () {
    //自适应
    if (auth.check(this)) {
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });

        var check = url.getUrlParam("key");
        if (check != null && check != "") {
            key = check;
            base.show();
        } else {
            swal({
                    title: "请选择网络课程",
                    type: "warning",
                    showCancelButton: false,
                    confirmButtonColor: "#DD6B55",
                    confirmButtonText: "确定",
                    closeOnConfirm: false
                },
                function () {
                    window.location.href = server_root + 'my/core/onlinecourse';
                }
            );

        }

    }


})
var base = {
    show: function () {

        var param = {url: baseModule.onlineCourseApi + '/getCatalogue-' + key};
        var request = ajax.get(param);
        request.done(function (d) {
            var nodes = d.result;
            baseData = nodes;
            for (var i = 0, l = nodes.length; i < l; i++) {
                var typeStr = '';
                if (nodes[i].type == null) {
                    typeStr = '&nbsp;';
                } else if (nodes[i].type == 1) {
                    typeStr = '图文章节';
                } else if (nodes[i].type == 2) {
                    typeStr = '视频章节';
                } else if (nodes[i].type == 0) {
                    typeStr = '网络课程';
                }
                nodes[i].pId = (nodes[i].parentId || 0);
                var content = nodes[i].content;
                var arrayObj = new Array();
                arrayObj.push(typeStr);
                arrayObj.push(content);
                arrayObj.push(formatHandle(nodes[i]));
                nodes[i].td = arrayObj;
            }
            var heads = ["名称", "类型", "简介", " "];

            $.TreeTable("treeMenu", heads, nodes);
            $("#treeMenu").treetable("expandAll");
            if (key != null && key != "") {
                var obj = getObjcetById(parseInt(key));
                if (obj != null) {
                    $('#title').empty();
                    $('#title').html(obj.name + "&nbsp;网络课程章节管理");
                }
            }
            auth.show();

        })

    },
    expand: function () {
        $("#treeMenu").treetable("expandAll");
    },
    collapse: function () {
        $("#treeMenu").treetable("collapseAll");
    },
    goback: function () {
        window.location.href = server_root + 'my/core/onlineCourse';
    },


}


var course = {
    note: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个课程");
            return;
        }

        openLay({url: config.courseNote, fun: "", title: obj.name + "&nbsp;课程进度查看"});
        $("#opt-dialog-enter").hide();
        var param = {url: baseModule.onlineCourseUserApi + '/AllNote/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            var notes = d.result;
            var template = doT.template($("#course-note-template").text());//获取的模板
            $('#course-note-list').html(template(notes));//模板装入数据
        })
    },

    viewInfo: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个课程");
            return;
        }
        openLay({url: config.courseRead, fun: "", title: obj.name + '网络课程详细信息'});
        $("#opt-dialog-enter").hide();
        var param = {url: baseModule.onlineCourseApi + '/getById/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            //console.log(d);
            var model = d.result;
            document.getElementById('artworkURL').src = model.artworkURL;
            model['startTime'] = model['startTimeStr'];
            model['endTime'] = model['endTimeStr'];
            tool.coursetranslate(model)

        })


    },

}

var chapter = {
    viewInfo: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }

        openLay({url: config.chapterview, fun: "opt.view();", title: obj.name + config.title + "查看"});
        $("#opt-dialog-enter").hide();
        var param = {url: baseModule.chapterApi + '/getById/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            //console.log(d);
            var model = d.result;
            // editor.set('#content',model['content']);
            $('#chapterContent').html(model['content']);
            form.set(model);
            form.disabled();

        })

    },
    note: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }

        openLay({url: config.chapterSectionNote, fun: "", title: obj.name + "&nbsp;章进度查看"});
        $("#opt-dialog-enter").hide();

        var param = {url: baseModule.chapterUserApi + '/AllNote/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            var notes = d.result;
            var template = doT.template($("#chapterSection-note-template").text());//获取的模板
            $('#chapterSection-note-list').html(template(notes));//模板装入数据
        })

    },
    edit: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }
        title = obj.name;

        openLay({url: config.chapterform, fun: "opt.update();", title: obj.name + config.title + "编辑"});

        var param = {url: baseModule.chapterApi + '/getById/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            var model = d.result;
            editor.set('#content', model['content']);
            form.set(model);


        })
    },
    remove: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }
        tips.confirm({message: '是否要删除章节：“' + obj.name + '”？', fun: "opt.delete();"});

    },
    add: function (event) {

        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个课程");
            return;
        }
        openLay({url: config.chapterform, fun: 'opt.create();', title: obj.name + config.title + "章节新增"});


    },
}


var section = {
    viewInfo: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }

        openLay({url: config.sectionView, fun: "secOpt.view();", title: obj.name + config.title + "查看"});
        $("#opt-dialog-enter").hide();
        var param = {url: baseModule.sectionApi + '/getById/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            var model = d.result;
            $('#secContent').html(model['content']);
            if (model.type == 2 && model.url != null) {
                document.getElementById('VideoShow').src = model['url'];
                document.getElementById('VideoShow').style.display = 'block';
                $('#upload-name').html("更改");
            }
            form.set(model);
            form.disabled();


        })

    },
    note: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }

        openLay({url: config.chapterSectionNote, fun: "", title: obj.name + "&nbsp;节进度查看"});
        $("#opt-dialog-enter").hide();
        var param = {url: baseModule.sectionUserApi + '/AllNote/' + obj.id};
        var request = ajax.get(param);
        request.done(function (d) {
            var notes = d.result;
            var template = doT.template($("#chapterSection-note-template").text());//获取的模板
            $('#chapterSection-note-list').html(template(notes));//模板装入数据
        })

    },
    edit: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }
        openLay({url: config.section, fun: "secOpt.update();", title: obj.name + config.title + "编辑"});
        var param = {url: baseModule.sectionApi + '/getById/' + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            var model = d.result;
            editor.set('#content', model['content']);
            if (model.type == 2 && model.url != null) {
                document.getElementById('VideoShow').src = model['url'];
                document.getElementById('VideoShow').style.display = 'block';
                $('#upload-name').html("更改");
            }
            form.set(model);

        })
    },
    remove: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }
        tips.confirm({message: '是否要删除章节：“' + obj.name + '”？', fun: "secOpt.delete();"});

    },

    add: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值

        var obj = getObjcetById(optId);
        if (obj == null) {
            alert("请先选择一个章节");
            return;
        }
        optId = obj.id;


        var title = obj.name;
        openLay({url: config.section, fun: "secOpt.create();", title: title + config.title + "新增"});


    },

}


// 数据操作

//章管理
var opt = {

    create: function () {
        var data = form.get("#opt-form");
        data['courseId'] = optId;
        if (form.verifyPlus(data))
            return false;
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['content'] = replaceOutUrl(data['content']);


        var param = {url: baseModule.chapterApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d.message);
            base.show();
            //  location.reload();
            editor.set('#content', "");

        })
    },

    delete: function () {
        var request = ajax.delete({url: baseModule.chapterApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            base.show();
            //   location.reload();

        })
    },
    update: function () {
        var data = form.get("#opt-form");
        if (form.verifyPlus(data))
            return false;
        data['id'] = optId;
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['content'] = replaceOutUrl(data['content']);

        var param = {url: baseModule.chapterApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            closeLay();
            base.show();
            //location.reload();
            editor.set('#content', "");

        })
    },
    close: function () {
        closeLay();
    }
}

//节管理

var secOpt = {

    create: function () {
        var data = form.get("#opt-form");
        data.chapterId = optId;
        if (data.type == 2 && !uploadFlag) {
            tips.error("未上传视频");
            return false;
        }
        if (form.verifyPlus(data))
            return false;
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['content'] = replaceOutUrl(data['content']);

        var param = {url: baseModule.sectionApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            videoData['sectionId'] = d.result.id;
            videoData['name'] = d.result.name;
            editor.set('#content', "");

            tips.done(d.message
            );

            if (data.type == 2 && uploadFlag) {
                var param = {url: toolModule.videoApi, data: videoData};
                var request = ajax.post(param);
                request.done(function (d) {
                    //$("#videoId").val(d.result.id);

                })
            }
            base.show();


        })
    },
    delete: function () {

        var request = ajax.delete({url: baseModule.sectionApi + '/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            base.show();

        })
    },
    update: function () {
        var data = form.get("#opt-form");
        var url = document.getElementById('VideoShow').src;

        if (data.type == 2 && (url == "" || url == null)) {
            tips.error("未上传视频");

            return false;
        }
        if (form.verifyPlus(data))
            return false;
        data['id'] = optId;
        tips.info("正在校验数据中，请稍候！");
        //校验内容是否存在外部图片，如有则自动替换
        data['content'] = replaceOutUrl(data['content']);
        var param = {url: baseModule.sectionApi, data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            videoData['sectionId'] = optId;
            videoData['name'] = d.result.name;
            editor.set('#content', "");

            tips.ok(d.message);
            //更新对象
            closeLay();

            if (data.type == 2 && uploadFlag) {
                var param = {url: toolModule.videoApi, data: videoData};
                var request = ajax.post(param);
                request.done(function (d) {
                    //$("#videoId").val(d.result.id);

                })
            }

            base.show();
            //   location.reload();

        })
    },
    upload: function (file) {
        var data = form.get("#opt-form");
        // console.log(data);
        var _file = document.getElementById(file).files[0];

        browserMD5File(_file,

            function (err, md5) {
                videoMd5 = md5;
                // console.log("本地图片md5值："+md5); // 97027eb624f85892c69c4bcec8ab0f11
                var param = {url: toolModule.checkVideoApi + '/' + videoMd5};
                var request = ajax.get(param);
                request.done(function (d) {
                    //console.log(d);
                    videoData = d.result;
                    if (videoData.id) {
                        //console.log("服务器已存在该视频，md5值为："+picData.md5)

                        document.getElementById('VideoShow').src = videoData.url;
                        document.getElementById('VideoShow').style.display = 'block';
                        uploadFlag = true;
                        videoData['url'] = videoData.url;
                        videoData['describe'] = "网课视频";
                        videoData['path'] = videoData.path;
                        videoData['code'] = videoMd5;
                        var uploadMsg = '上传完成.';
                        tips.ok(uploadMsg);
                        $('#upload-name').html("更改");
                        //  $("#content")[0].innerHTML = data.content;


                    } else {
                        // console.log("正常上传，md5值为："+picMd5)
                        newFileName = _file.name;

                        newFileName = getSuffix(newFileName) + new Date().getTime() + "." + getSuffix(newFileName);//修改文件名

                        var path = 'partySystem/course/video/' + newFileName;

                        imageType = file;
                        cos.uploadFile(path, _file, uploadVideoSuccess);
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
    close: function () {
        closeLay();
    }
}


function getObjcetById(id) {
    var nodes = baseData;
    for (var i = 0, l = nodes.length; i < l; i++) {
        if (nodes[i].id == id) {
            return nodes[i];
        }
    }

}


/**
 * 根据权限展示功能按钮
 * @param treeNode
 * @returns {string}
 */
function formatHandle(treeNode) {
    var htmlStr = '';

    htmlStr += '  <div class="btn-group" >' +
        '                <button data-toggle="dropdown" class="btn btn-primary btn-xs dropdown-toggle" >操作 <span class="caret" style="padding:initial;"></span> </button> ' +
        '                <ul class="dropdown-menu" >';

    //  <li><a href="#"  data-id="'+treeNode.id+'" data-id="{{= it[i].id}}" onclick="onlinecourse.delete(this);">删除</a> </li>

    if (treeNode.level == 0 || treeNode.level == "0") {
        htmlStr += '<li ><a href="#"  data-id="' + treeNode.id + '"   onclick="chapter.add(this);">添加章</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '" onclick="course.viewInfo(this);">查看课程</a> </li>';

        htmlStr += '<li ><a href="#"  data-id="' + treeNode.id + '"  onclick="course.note(this);">用户课程进度</a> </li>';


    } else if (treeNode.level == 1 || treeNode.level == "1") {
        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="section.add(this);">添加节</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="chapter.viewInfo(this);">查看章</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="chapter.edit(this);">修改章</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="chapter.remove(this);">删除章</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="chapter.note(this);">用户章进度</a> </li>';


    } else {
        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="section.viewInfo(this);">查看节</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="section.edit(this);">修改节</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="section.remove(this);">删除节</a> </li>';

        htmlStr += '<li><a href="#"  data-id="' + treeNode.id + '"  onclick="section.note(this);">用户节进度</a> </li>';
    }

    htmlStr += ' </ul> </div>';

    return htmlStr;
}

// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}

var uploadVideoSuccess = function (result) {
    loading.hide();
    var data = result.data;
    // console.log(data);
    var access_url = data.source_url.replace("http:", "").replace("https:", "");
    var path = data.resource_path;
    document.getElementById('VideoShow').src = access_url;
    document.getElementById('VideoShow').style.display = 'block';
    $('#upload-name').html("更改");
    videoData['path'] = path;
    videoData['url'] = access_url;
    videoData['describe'] = "网课视频";
    videoData['code'] = videoMd5;
    uploadFlag = true;

};

var coursehelper = {
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
    coursetranslate: function (model) {
        var data = [];
        for (var variable in model) {
            data[variable] = model[variable];
            //判断helper里是否存在该函数，存在则执行转换
            if (typeof eval('coursehelper.' + variable) == 'function')
                model[variable] = eval('coursehelper.' + variable + '(' + model[variable] + ')');
        }
        form.set(model);
        //恢复回转换前数据
        for (var variable in data) {
            model[variable] = data[variable];
        }
    }
}