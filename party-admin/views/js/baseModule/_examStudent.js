var academyData = [], examData = [], pageData = [];
var totalPage = 0, totalCount = 0;
var optId = '',optNumber='',optExamId='';
var index = 1, size = 10, academyCode = '', examId = '',number='';


$(function () {
    //自适应
    if (auth.check(this)) {
        view.initHeight();
        $(window).resize(function () {
            view.initHeight();
        });
        var key = url.getUrlParam("key");
        if (key != null && key != "") {
            loading.show();
            examId = key;
        }
        examStudent.get();
        // examStudent.getAcademy();
        examStudent.getAllExamSelect();

    }
});

var examStudent = {
    get: function () {
        var param = {url: baseModule.examApi + '/grade/' + index + '-' + size + '/examid-' + examId + '/academyCode-' + academyCode + '/number-'+number};
        // var param = {url: baseModule.examApi + '/grade/' + index + '-' + size + '/examid-' + examId  + '/number-'+number};
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
            loading.hide();

        })
    },
    select: function () {
        //查询

        examId = $.trim($('#examId').val());
        // academyCode = $.trim($('#academyCode').val());
        number = $.trim($('#number').val());

        index = 1;
        examStudent.get();

    },
    export: function () {
        //fixme:暂用前端导出
        examId = $.trim($('#examId').val());
        academyCode = $.trim($('#academyCode').val());
        number = $.trim($('#number').val());

        var param = {url: baseModule.examApi + '/grade/all/examid-' + examId + '/academyCode-' + academyCode  + '/number-'+number};
        // var param = {url: baseModule.examApi + '/grade/all/examid-' + examId   + '/number-'+number};
        var request = ajax.get(param);
        request.done(function (d) {
            var gradeData = d.result;
            var template = doT.template($("#examStudent-export-template").text());//获取的模板
            $('#examStudent-list-hide').html(template(gradeData));//模板装入数据

            $("td.td-number").attr("data-tableexport-msonumberformat", "\\@");
            $("td.td-score").attr("data-tableexport-msonumberformat", "\\@");
            $("td.td-time").attr("data-tableexport-msonumberformat", "\\@");

            $("#examStudent-table-hide").show();
            $("#examStudent-table-hide").tableExport({type: 'excel', fileName: 'grade', tableName: '成绩'});
            $("#examStudent-list-hide").empty();
            $("#examStudent-table-hide").hide();


        })

        // examStudent.get();

    },
    getAllExamSelect: function () {
        //获取所有试卷列表
        var param = {url: baseModule.examApi + "/all/selectList" };
        var request = ajax.get(param);
        request.done(function (d) {
            examData = d.result;
            render.examSelect();
            if(examId!=null&&examId!=""){
                $('#examId').val(examId);
            }
        });
    },
    getAcademy: function () {
        var param = {url: toolModule.getAcademyListApi};
        var reqest = ajax.get(param);
        reqest.done(function (d) {
            academyData = d.result;
            render.academySelect();
        })

    },
    sync: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        var model = result.get(pageData, optId);
        optNumber=model.number;
        optExamId=model.examId;
        tips.confirm({message: '是否要重置该考生答题状态？', fun: "opt.sync();", enter: "重置"});
    },
    unlock: function (event) {
        if (auth.refuse(event))
            return false;
        optId = getId(event);
        var model = result.get(pageData, optId);
        optNumber=model.number;
        tips.confirm({message: '是否要解除该考生登陆锁？', fun: "opt.unlock();", enter: "解除"});
    },


}


// 渲染
var render = {
    page: function () {
        var template = doT.template($("#examStudent-template").text());//获取的模板
        $('#examStudent-list').html(template(pageData));//模板装入数据

    },
    academySelect: function () {

        var template = doT.template($("#academy-select-template").text());
        $('#academyCode').html(template(academyData));


    },
    examSelect: function () {
        var template = doT.template($("#exam-select-template").text());
        $('#examId').html(template(examData));
    },

}


// 视图界面
var view = {
    initHeight: function () {
        $('.data-view').css('height', (parent.adaptable().h) - 80);
        $('.date-table').css('height', (parent.adaptable().h) - 180);
        size = Math.floor(((parent.adaptable().h) - 180) / 40);
    }
}

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
            pageArray: [size, 12, 24, 48],
            totalCount: _total,
            id: 'greade-page',
            callback: function (api) {
                index = api.getCurrent();
                examStudent.get();
            }
        });
        if (_pageSize > 0)
            $('.pages').show();
    }
};

function pageChange(event) {
    size = $(event).val();
    index = 1;
    examStudent.get();
};

var opt = {
    sync: function () {
        var request = ajax.put({url: baseModule.examApi + '/syncStudentStatus/' + optExamId+'-'+optNumber});
        request.done(function (d) {
            tips.ok(d.message);
            closeLay();
            examStudent.get();

        })
    },
    unlock: function () {
        var request = ajax.put({url: baseModule.examApi + '/unlockStudent/' + optNumber});
        request.done(function (d) {
            tips.ok(d.message);
            closeLay();
            examStudent.get();

        })
    },
}
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
}