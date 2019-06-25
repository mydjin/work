var optId = '';

$(function () {
    //60秒钟刷新一次数据
    getMessage();
    //getTodo();
    setInterval(getMessage, 60000);
    //  setInterval(getTodo,60000);
    optId = getUrlParms("message");
    if (optId != "" && optId != null) {
        readMessage(null, optId);
    }

})

function getMessage() {
    var param = {url: baseModule.userMessageApi + '/1-5-'};//调用页面接口，取第一页前五条记录，完整参数为index-size-key
    var request = ajax.get(param);
    request.done(function (d) {
        var messageData = d.result.data;
        var template = doT.template($("#message-list-template").text());//获取的模板
        $('#message-list').html(template(messageData));//模板装入数据
        if (messageData.length == 5) {
            $("#messageMore").attr("style", "display:block;");
        }
    })
}

/*
function getTodo() {
    var param = {url:baseModule.approvalListApi+'/todo/1-5-/' };//调用页面接口，取第一页前五条记录，完整参数为index-size-approvalType
    var request = ajax.get(param);
    request.done(function (d) {
        var todoData= d.result.data;
            var template = doT.template($("#todo-list-template").text());//获取的模板
            $('#todo-list').html(template(todoData));//模板装入数据
        if(todoData.length==5){
            $("#todoMore").attr("style","display:block;");
        }
    })
}

function readMessage(event,id) {
    if(event!=null) {
       optId = getId(event);
    }
    if(event==null){
        optId = id;
    }
    var param = {url:baseModule.userMessageApi+'/info/'+optId };
    var request = ajax.get(param);
    request.done(function (d) {
        var messageData= d.result;
        openLay({url: '/form/_userMessage.html', fun: "deleteMessage();", title: '', enter: "删除"});
        var template = doT.template($("#message-template").text());
      //  $("#opt-dialog-enter").hide();
        $('#message').html(template(messageData));
        if ( messageData.readStatus!=1) {
            var param = {url: baseModule.userMessageApi+ '/read/' + optId};
            var request = ajax.put(param);
            request.done(function (d) {
                getMessage();
            })

        }
    })
}

function deleteMessage() {

    swal({
            title: "是否要删除这条消息？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: ((typeof color) == "undefined") ? "#DD6B55" : color,
            confirmButtonText: ((typeof enter) == 'undefined') ? '删除' : enter,
            cancelButtonText: ((typeof cancel) == 'undefined') ? "容我再想想" : cancel,
            closeOnConfirm: false
        },
        function () {
            var request = ajax.delete({url: baseModule.messageApi  +'/'+ optId});
            request.done(function (d) {
                closeLay();
                tips.ok(d.message);
                getMessage();
            })
        }
    );

}*/
var helper = {
    type: function (_type) {
        switch (parseInt(_type)) {
            case 0 :
                return "系统通知";
            case 1 :
                return "课程安排";
            case 2 :
                return "会议安排";
            case 3 :
                return "每月一测";
            case 4 :
                return "入党申请书";
            case 5 :
                return "思想汇报";
            default:
                return "系统通知";
        }
    },
    url: function (_type) {
        switch (parseInt(_type)) {
            case 0:
                return "#";
            case 1 :
                return "/my/core/schedule";
            case 2 :
                return "/my/core/meetingSchedule";
            case 3 :
                return "/my/core/exam";
            case 4 :
                return "/my/core/application";
            case 5 :
                return "/my/core/report";
            default:
                return "#";
        }
    },
};

//1-入党申请书,2-思想汇报,3-考察表,4-谈话记录,5-个人详细信息审核
/*
var todohelper = {
    stage: function (_stage){
        switch (parseInt(_stage)){
            case 0 : return "审核";
            case 1 : return "初审";
            case 2 : return "复审";
            default:
                return "审核";
        }
    },
    approvalType: function (_approvalType){
        switch (parseInt(_approvalType)){
            case 1 : return "入党申请书";
            case 2 : return "思想汇报";
            case 3 : return "考察记录";
            case 4 : return "谈话记录";
            case 5 : return "个人信息";
            case 6 : return "课程请假";
            case 7 : return "会议请假";

            default:
                return "其他";
        }
    },
    approvalUrl: function (_approvalType){
        switch (parseInt(_approvalType)){
            case 1 : return "/my/core/application";
            case 2 : return "/my/core/report";
            case 3 : return "/my/core/surveyRecordChat";
            case 4 : return "/my/core/chatRecord";
            case 5 : return "/my/core/userManage";
            case 6 : return "/my/core/vacate";
            case 7 : return "/my/core/meetingVacate";

            default:
                return "#";
        }
    },
};*/

function getUrlParms(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return unescape(r[2]);
    return null;
}



