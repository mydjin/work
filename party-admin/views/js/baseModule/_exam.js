var index = 1, size = 6, key = '', totalPage = 0, totalCount = 0;
var baseData = [], pageData = [], examSelectData = [], questionNumber = [];
var optId = '', batch = '';
var note = 2, push = 2, isRelease = 2;

var config = {
    form: '../../form/_exam.html',
    title: '考试管理',
    preview: '../../form/_examPreview.html',
    previewTitle: '试题预览',
    importUser: '../../form/_examImportUser.html',
    importUserTitle: '导入考生',
    examRandom: '../../form/_examRandom2.html',
};

$(function () {
    //自适应
    if (auth.check(this)) {
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        exam.get();
        exam.getNoteValue();
        exam.getPushValue();
    }
})
;

var exam = {
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
        var param = {url: baseModule.examApi + '/' + index + '-' + size + '-' + key};
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
        openLay({url: config.form, fun: 'opt.create();', title: config.title, enter: "操作"});
        createTree(toolModule.getBranchCatalogueApi, "#rangeTree", true, true); //创建
        render.showNoteSelect();
        render.showPushSelect();
    },
    getAcademySelect: function () {
        var param = {url: toolModule.getAcademyListApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            academyData = d.result;
            var template = doT.template($("#academy-template").text());
            $('#academyCode').html(template(academyData));
        })

    },
    importUser: function (event) {
        if (auth.refuse(event))
            return false;
        optId= getId(event);
        openLay({url: config.importUser, fun: 'opt.importUser();', title: config.importUserTitle, enter: "导入"});
        exam.getImportExamSelect();
        exam.getAcademySelect();

    },
    exportUserError: function () {
        $("td.td-number").attr("data-tableexport-msonumberformat", "\\@");
        $("td.td-phone").attr("data-tableexport-msonumberformat", "\\@");
        $("#error-table").tableExport({type: 'excel', fileName: 'exportUserError'});
    },
    importUserModel: function (event) {
        if (auth.refuse(event))
            return false;
        downFile('../../model/examImportUser.xls');

    },
    delete: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要删除考试数据？', fun: "opt.delete();"});

    },

    update: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        openLay({url: config.form, fun: "opt.update();", title: config.title, enter: "操作"});
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
        optId = getId(event);
        tips.confirm({message: '确定要发布试卷吗？如果发布试卷，将不能在修改试卷信息！', fun: "opt.release();", enter: "发布"});
    },
    sendNote: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '确定要短信推送吗？如果点击确定，有手机号码的本场考生将会收到短信！', fun: "opt.sendNote();", enter: "发布"});
    },

    read: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        openLay({url: config.form, title: config.title, enterHidden: true, enter: "操作"});
        form.disabled();
        $('#opt-dialog-enter').hide();
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
        tool.translate(model);
    },

    preview: function (event) {
        // if (auth.refuse(event))
        //     return false;
        optId = getId(event);//获取当前id的值 = examId

        openLay({url: config.preview, title: config.previewTitle, enter: "操作"});
        $('#opt-dialog-enter').hide();

        var model = result.get(pageData, optId);
        var type = model['type'];
        var questionId = model['questionId'];

        //通过问题获取选项
        var param = {url: baseModule.examQuestionApi + "/exam/" + optId};
        var request = ajax.get(param);
        request.done(function (d) {
            //按类型渲染选项
            //正确答案用颜色标出
            var singleListData = d.result.singleChoice;
            var multipleListData = d.result.multipleChoice;
            var tFListData = d.result.trueOrFalse;
            var gapListData = d.result.gapFilling;

            // console.log(gapListData);

            render.singleChoiceList(singleListData);
            render.multipleChoiceList(multipleListData);
            render.trueOrFalseList(tFListData);
            render.gapFillingList(gapListData);
        });

        model['type'] = helper.type(model['type']);
        form.set(model);
        model['type'] = type;
        $('#answerKeys').html(model['answerKeys'])
    },
    sync: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '是否要重置该考试缓存？', fun: "opt.sync();", enter: "重置"});
    },
    finish: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        tips.confirm({message: '您确定要结束本次的所有考试吗？如果点击确定，所有考生都不能继续参加考试，未提交试卷的考生成绩将为0。', fun: "opt.finish();", enter: "结束"});
    },


    getExamSelect: function () {
        //获取所有考试列表
        var param = {url: baseModule.examApi + "/all/" + 1};
        var request = ajax.get(param);
        request.done(function (d) {
            examSelectData = d.result;
            render.examSelect();
            if(optId!=null&&optId!=""){
                $('#examId').val(optId);
                exam.examInfo();
            }
        });
    },
    getImportExamSelect: function () {
        //获取所有考试列表
        var param = {url: baseModule.examApi + "/import/selectList"};
        var request = ajax.get(param);
        request.done(function (d) {
            examSelectData = d.result;
            render.examSelect();
            if(optId!=null&&optId!=""){
                $('#examId').val(optId);
            }
        });
    },
    randomExam: function (event) {
        if (auth.refuse(event))
            return false;
        optId= getId(event);
        openLay({url: config.examRandom, fun: "opt.randomExam();", title: config.title, enter: "操作"});
        exam.getExamSelect();
        // render.examSelect();

        //获取渲染题库的题目数量
        var param = {url: baseModule.examQuestionApi + "/type"};
        var request = ajax.get(param);
        request.done(function (d) {
            questionNumber = d.result;
        });
    },

    examInfo: function () {
        var examId = $('select#examId').val();

        if (examId == " ") {
            $('#examInfo').html("");
            return false;
        }

        var request = $.ajax({
            type: "GET",
            url: baseModule.examApi + '/info/' + examId,
            // contentType: "json",
            headers: header,
            // dataType: 'json',
        });
        request.done(function (d) {
            var examList = d.result;
            var examData =examList[0];
            var isMakeStr =examData.isMake;
            if(isMakeStr!=null&&isMakeStr!=""){
                var isMake =parseInt(isMakeStr);
                if(isMake==2){
                    tips.info("当前试卷已经组卷，重新组卷将会清空原来组卷信息！若确定要重新组卷请继续操作，误点可以关闭界面！");
                }
            }
            render.examInfo(examList);
        })
    },
    //重定向到手动组卷
    redirectExamPaperGenerate: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        window.location.href = server_root + 'my/core/examPaperGenerate?key=' + optId;
    },
    //重定向到手动组卷
    redirectExamRoom: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        window.location.href = server_root + 'my/core/examRoom?key=' + optId;
    },
    //重定向到考生管理
    redirectExamStudent: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);//获取当前id的值
        window.location.href = server_root + 'my/core/examStudent?key=' + optId;
    },


}


var render = {
    page: function () {
        var template = doT.template($("#exam-template").text());//获取的模板
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

    singleChoiceList: function (listData) {
        var template = doT.template($("#choice-list-template").text());
        $('#single-list').html(template(listData));
    },

    multipleChoiceList: function (listData) {
        var template = doT.template($("#choice-list-template").text());
        $('#multiple-list').html(template(listData));
    },

    trueOrFalseList: function (listData) {
        var template = doT.template($("#choice-list-template").text());
        $('#true-or-false-list').html(template(listData));
    },

    gapFillingList: function (listData) {
        var template = doT.template($("#gap-list-template").text());
        $('#gap-list').html(template(listData));
    },

    examSelect: function () {
        var template = doT.template($("#exam-select-template").text());
        $('#examId').html(template(examSelectData));

        $('#select-paper').html(template(examSelectData));
        $('#select-paper option[value=" "]').html("全部考试")
    },

    examInfo: function (examInfoData) {
        var template = doT.template($("#exam-info-template").text());
        $('#examInfo').html(template(examInfoData));
        $("#trueOrFalseNumberQuestion").html(questionNumber['trueOrFalse']);
        $("#singleNumberQuestion").html(questionNumber['single']);
        $("#multipleNumberQuestion").html(questionNumber['multiple']);
        $("#gapNumberQuestion").html(questionNumber['gap']);
        // var allQuestion = questionNumber['trueOrFalse']+questionNumber['single']+questionNumber['multiple']+questionNumber['gap'];
        // $("#allQuestion").html(allQuestion);
    },
};

var opt = {
    create: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        var range = getCheckedNodesToString("rangeTree");
        data['range'] = range;
        /*if (range == "" || range == null) {
            return false;
        }*/
        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);
        var score = parseInt(data['score']);
        var trueOrFalse = parseInt(data['trueOrFalse']);
        var single = parseInt(data['single']);
        var multiple = parseInt(data['multiple']);
        var gap = parseInt(data['gap']);


        if (score == 0 || single + gap + multiple + trueOrFalse == 0) {
            tips.error("总分不能为0！");
        }

        var param = {url: baseModule.examApi, data: data};
        var request = ajax.post(param);
        request.done(function (d) {
            tips.done(d);
            exam.get();
        })
    },
    importUser: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        if (data['examId'] == "" || data['examId'] == null) {
            tips.warning("请选择考试");
            return false;
        }

        if (data['academyCode'] == "" || data['academyCode'] == null) {
            tips.warning("请选择学院");
            return false;
        }

        if (data['type'] == "" || data['type'] == null) {
            tips.warning("请选择用户类型");
            return false;
        }


        if (!xls_check("file")) {
            return false;
        }
        //console.log(data);debugger;
        $('#opt-dialog-cancel').attr("disabled", true);
        $('#examId').attr("disabled", true);
        $('#academyCode').attr("disabled", true);
        $('#type').attr("disabled", true);
        $('#file').attr("disabled", true);
        $('#opt-dialog-enter').attr("disabled", true);
        $('#todoBar').empty();
        $('#todoPer').empty();
        $('#mark').empty();
        $('#res').empty();


        var param = {url: baseModule.examApi + '/ImportUserSetting', data: data, async: false};
        var request = ajax.post(param);
        request.done(function (d) {
            var batch = d.result;
            initImportStatus();

            checkImportStatus(baseModule.examApi + "/ImportUserStatus/" + batch);

            importFile("file", baseModule.examApi + '/ImportUser/' + batch,
                function importComplete(evt) {
                    $('#opt-dialog-cancel').attr("disabled", false);
                    $('#examId').attr("disabled", false);
                    $('#academyCode').attr("disabled", false);
                    $('#type').attr("disabled", false);
                    $('#file').attr("disabled", false);
                    $('#opt-dialog-enter').attr("disabled", false);
                    importCompleteShow(evt);
                });


        })


    },
    delete: function () {
        var request = ajax.delete({url: baseModule.examApi + '/' + optId});
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
            /*if (range == "" || range == null) {
                return false;
            }*/
            data['range'] = range;
        }
        data['startTime'] = time.date2timestamp(data['startTime']);
        data['endTime'] = time.date2timestamp(data['endTime']);
        var score = parseInt(data['score']);
        var trueOrFalse = parseInt(data['trueOrFalse']);
        var single = parseInt(data['single']);
        var multiple = parseInt(data['multiple']);
        var gap = parseInt(data['gap']);


        if (score == 0 || single + gap + multiple + trueOrFalse == 0) {
            tips.error("总分不能为0！");
        }
        var param = {url: baseModule.examApi, data: data};
        var request = ajax.put(param); //加一条记录
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            closeLay();
            exam.get();

        })
    },

    release: function () {
        //closeLay();
        //tips.info("后台发布中，请稍候！");
        var request = ajax.put({url: baseModule.examApi + '/release/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            pageData = result.update(pageData, optId);
            exam.get();
            closeLay();
            // 发布推送
            var request = ajax.put({url: baseModule.examApi + '/releaseSend/' + optId});
            request.done(function (d) {
                tips.ok(d.message);
            })

        })
    },
    sendNote: function () {
        var request = ajax.put({url: baseModule.examApi + '/sendNote/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            exam.get();
            closeLay();
        })
    },


    randomExam: function () {
        var data = form.get("#opt-form");
        if (form.verify(data))
            return false;
        var examId = data['examId'];
        if (examId == "") {
            tips.warning("请选择考试");
            return false;

        }

        var param = {url: baseModule.examQuestionApi + '/random', data: data};
        var request = ajax.put(param);
        request.done(function (d) {
            tips.ok(d.message);
            //更新对象
            exam.get();
            closeLay();
        })

    },
    sync: function () {
        var request = ajax.put({url: baseModule.examApi + '/syncCache/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            closeLay();

        })
    },
    finish: function () {
        var request = ajax.put({url: baseModule.examApi + '/finish/' + optId});
        request.done(function (d) {
            tips.ok(d.message);
            exam.get();
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
            id: 'exam-page',
            callback: function (api) {
                index = api.getCurrent();
                exam.get();
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
    exam.get();
};


var helper = {
    status: function (_status) {
        switch (parseInt(_status)) {
            case 0:
                return "未开始";
            case 1:
                return "进行中";
            case 2:
                return "已结束";
        }
    },
    releaseStatus: function (_releaseStatus) {
        switch (parseInt(_releaseStatus)) {
            case 1:
                return "未发布";
            case 2:
                return "已发布";
        }
    },
    type: function (_type) {
        switch (parseInt(_type)) {
            case 1:
                return "每月一测";
            case 2:
                return "结业考试";
        }
    }, length: function (_length) {
        switch (parseInt(_length)) {
            case 0:
                return "不限";
            default:
                return _length;
        }
    },
    way: function (_way) {
        switch (parseInt(_way)) {
            case 1:
                return "开放考试";
            case 2:
                return "限定考试";
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

