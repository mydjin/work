$(function () {
    window.onload = function () {
        /* if(cookie.get("oSwitch")==1){
           return false;
           setTimeout(function(){
           location.reload();
           cookie.get("oSwitch",0);},300);
           }*/


        var logZ1 = "本页面由广西民族大学“启航训练营”负责开发。" + "\n\n"
            + "“启航训练营”面向于广西民族大学全体学生，" + "\n"
            + "组建完整的项目开发团队，采用“培训+实践”的工作方式，" + "\n"
            + "旨在全面提高学生的专业知识技能，培养学生的沟通协作能力,增强项目实践经验。" + "\n\n"
            + "如果您像我们一样深深地热爱软件开发，" + "\n"
            + "那么还犹豫什么，赶紧加入我们" + "\n"
            + "启航训练营能为你实现梦想提供最广阔的平台" + "\n\n\n";
        var logZ2 = "请将简历发送至：" + "%c hr@qihsoft.cn(注明来自console)";
        //var logZ3 = "招聘详情："+"%c http://t.cn/EPOcaSI";
        var logZ2_style = "color:#00a5c7;" + "\n\n";
        //var logZ3_style = "color:#e45;"+"\n\n";
        console.log(logZ1);
        console.log(logZ2, logZ2_style);
        //console.log(logZ3 , logZ3_style);
        //cookie.set("oSwitch",1);
    };
});
    