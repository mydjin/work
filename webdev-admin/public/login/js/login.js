	var times=0;	//记录密码输错几次
	var userFlag = true; //判断用户名是否存在
	var ipFlag = true;	//判断当前ip地址和上一次的ip地址是否相同
	var flag=""; //判断用户名和密码是否输入正确
	var apq="0";  //判断短信重新发送a标签是否已经发送过了，0表示可以发送，1表示60未过，不可以发送
	var dx=""; 	//判断短信是否发送成功，1表示发送成功，0表示发送失败
	var randCodeFlag="";//1表示验证码正确，0表示验证码错误
	var LOGIN = {
			//检查用户名是否存在
			checkLogin:function() {
				//0表示用户名不存在，1表示用户存在
				$.post("myLogin.do?checkuser", {name:$("#username").val(),type:$("#type").val()}, function(data){
					if(data == "0"){
						userFlag = false;								
					}
					else {
						userFlag = true;
					}
				});
			},
			//检查当前ip地址和上一次的ip地址是否相同
			checkIp:function(){
				$.post("myLogin.do?checkip", {ipAdress:returnCitySN["cip"]} ,function(data){
					//0表示ip地址和上一次的ip地址不相同,1表示ip地址和上一次的ip地址相同
					if(data == "0"){
						ipFlag = false;				
					}
					else {
						ipFlag = true;
					}
				});
			},
			//检查密码是否正确
			doLogin:function() {
//				var ipAdress=returnCitySN["cip"];
//				alert(ipAdress);
//				$("#ip").val(ipAdress);
				$.post("myLogin.do?checklogin", {pwd:$("#password").val(),ipAdress:remote_ip_info["city"]} ,function(data){
					//假设0表示密码输错,1表示输错3次,2表示正确,3表示ip地址变化,密码输错次数应该保存到数据库中，不然用户关掉浏览器重新打开就要重新计数。
					flag=data;
				});
			},		
			//检查短信验证码是否正确
			checkRand:function() {
				$.post("myLogin.do?checkRand", {randCode:$("#message").val(),ipAdress:remote_ip_info["city"]} ,function(data){
					//1表示验证码正确，0表示验证码错误
					randCodeFlag=data;
				});
			},		
			//发送短信验证码
			sendMessage:function() {
				$.post("myLogin.do?sendMessage",function(data){
					 dx=data;
				 });
			},
			//清除session中的验证码
			cleanRand:function() {
				$.post("myLogin.do?cleanRand");
			}
	};
  $(function(){
//	  alert(returnCitySN["cip"]);//ip地址
	  var yzm = "";
	  var type=0;//用来判断输入的用户名是身份证3，工号2，手机号1,0表示不是手机号工号和身份证
	  $("#login-img1").click(function(){
		  type=0;
		  //测试邮箱正则表达式
		  var userNameTest=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
		  //身份证正则表达式(15位)
		  var idCard1Test=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
		//身份证正则表达式(18位)
		  var idCard2Test=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
		  //手机号正则表达式
		  var phoneTest=/^1[3|4|5|7|8][0-9]{9}$/;
		  //工号正则表达式
		  var jobNumTest=/^[0-9]{8}$/;
		  var userName=$("#username").val();
		  //验证用户名是否正确
		  if(phoneTest.test(userName)){//如果是手机号type=1
			  type=1;			  
		  }
		  else if(jobNumTest.test(userName)||userName=="admin"){//如果是工号type=2
			  type=2;
		  }
		  else if(idCard1Test.test(userName)||idCard2Test.test(userName)){//如果是身份证type=3
			  type=3;
		  }
		  $("#type").val(type);
		  if(type==0){
			  $("#info").html("请输入正确的用户名");
			  return;			  
		  }
		//	  验证密码是否正确
		  if($.trim($("#password").val()).length<6){
			  $("#info").html("密码少于6位！");
			  return;
		  }
		  
		  //将输入的验证码和系统的验证码均变成小写再进行验证
		  if($("#yz").val().toLowerCase()!=yzm.toLowerCase()){
			  $("#info").html("验证码错误！");
			  yzm="";
			  drawPic();
			  $("#yz").val("");
			  return;
		 }
		  //已经输错3次或ip地址变化，验证短信验证码
		  if(flag=="1"||flag=="3"){
			  if($("#message").val().length==0){
				 $("#info").html("请输入短信验证码！");
				 return;
			  }else{
				  LOGIN.checkRand();
				  if(randCodeFlag=="0"){
					  $("#info").html("短信验证码错误！");
					 return;
				  }
			  }
		  }
		 $.ajaxSetup({async: false});
		 LOGIN.checkLogin();
		 //数据库中没有该用户
		 if(!userFlag){
			 $("#info").html("用户不存在，请重新输入");		
			 return;
		 }
//		 LOGIN.checkIp();
//		 if(!ipFlag){
//			 $("#info").html("ip地址变化，请输入短信验证码");
//			 flag="1";//下次要进行短信的验证，空的话直接return；
//			 showdx();//显示短信验证框
//			 $("#messSend").click();//显示time秒后重新发送字符串	
//			 LOGIN.sendMessage();
//			 if(dx=="0"){
//				 $("#info").html("短信发送失败");
//			 }
//			 return;
//		 }
		 LOGIN.doLogin();
		 //假设0表示密码输错,1表示输错3次,2表示正确,密码输错次数应该保存到数据库中，不然用户关掉浏览器重新打开就要重新计数。
		if (flag == "0") {
			$("#info").html("用户名或密码不正确，请重新输入");
			return;
		} else if(flag == "1"||flag=="3") {
			 showdx();//显示短信验证框
			 $("#info").html("请输入短信验证码");
			 if(apq=="0")
			 $("#messSend").click();//显示time秒后重新发送字符串	
			 return;
		} else {
			location.href="myLogin.do?login";
		//	alert("登录成功！");
			
		}		  
	  });
//	  //没用，只是用来测试短信验证码框显示和消失
//	  $("#dxcsxs").click(function(){
//		  flag="1";
//		  document.getElementById("dxyz").style.display="table-row";
//		  document.getElementById("login").style.paddingBottom="30px";
//		});
//	  $("#xs").click(function(){
//		  flag="0";
//		  document.getElementById("dxyz").style.display="none";
//		  document.getElementById("login").style.paddingBottom="10px";
//	  });
	  //忘记密码操作
	  $("#forget").click(function(){
		  alert("忘记密码操作");
		});
 	 /**生成一个随机数**/
	  function randomNum(min,max){
		return Math.floor( Math.random()*(max-min)+min);
	  }
	  /**生成一个随机色**/
	  function randomColor(min,max){
		var r = randomNum(min,max);
		var g = randomNum(min,max);
		var b = randomNum(min,max);
		return "rgb("+r+","+g+","+b+")";
	  }
	  drawPic();
	  if(document.getElementById("changeImg")) {
          onclick = function (e) {
              yzm = "";
              e.preventDefault();
              drawPic();
          }
      }

	  /**绘制验证码图片**/
	  function drawPic(){
		var canvas=document.getElementById("canvas");
		var width=canvas.width;
		var height=canvas.height;
		var ctx = canvas.getContext('2d');
		ctx.textBaseline = 'bottom';
	
		/**绘制背景色**/
		ctx.fillStyle = randomColor(180,240); //颜色若太深可能导致看不清
		ctx.fillRect(0,0,width,height);
		/**绘制文字**/
		var str = 'ABCEFGHJKLMNPQRSTWXY123456789';
		for(var i=0; i<4; i++){
		  var txt = str[randomNum(0,str.length)];
		  yzm+=txt;
		  ctx.fillStyle = randomColor(50,160);  //随机生成字体颜色
		  ctx.font = randomNum(15,40)+'px SimHei'; //随机生成字体大小
		  var x = 10+i*25;
		  var y = randomNum(25,45);
		  var deg = randomNum(-45, 45);
		  //修改坐标原点和旋转角度
		  ctx.translate(x,y);
		  ctx.rotate(deg*Math.PI/180);
		  ctx.fillText(txt, 0,0);
		  //恢复坐标原点和旋转角度
		  ctx.rotate(-deg*Math.PI/180);
		  ctx.translate(-x,-y);
		}
		/**绘制干扰线**/
/*	    for(var i=0; i<8; i++){
		  ctx.strokeStyle = randomColor(40,180);
		  ctx.beginPath();
		  ctx.moveTo( randomNum(0,width), randomNum(0,height) );
		  ctx.lineTo( randomNum(0,width), randomNum(0,height) );
		  ctx.stroke();
		}*/
		/**绘制干扰点**/
/*	    for(var i=0; i<100; i++){
		  ctx.fillStyle = randomColor(0,255);
		  ctx.beginPath();
		  ctx.arc(randomNum(0,width),randomNum(0,height), 1, 0, 2*Math.PI);
		  ctx.fill();
		}*/
	  }
	  //短信验证码重新发送
	  $("#messSend").click(function(){
		  document.getElementById("messSend").style.opacity= "0.2";	  
		  if(apq=="0"){
			  LOGIN.sendMessage();
			  time=59;
			  show();  
		  }
		 else{
			 return;
		 }
	  });
	  
	  var time=59;
	  function show(){
		  setReadOnly();
		  var timer = window.setInterval(getTimes,1000);
	  }
	  function getTimes(){
	    if(time==0){
	    	setReadWrite();
			document.getElementById("messSend").style.opacity= "1.0";
			$("#time").html("");
			apq="0";
			 LOGIN.cleanRand();
			 window.clearInterval(timer);			 
	    }
	   else{
		 apq="1";
		 $("#time").html(time+"秒后");
		 time--;
	   }
	 }
	//回车登录
	  $(document).keydown(function(e){
	  	if(e.keyCode == 13) {
	  		$("#login-img1").click();
	  	}
	  });
	  //账号换了之后短信验证码框消失
	  $("#username").change(function(){
		  hiddendx();
	  });
	  function showdx(){
		  document.getElementById("dxyz").style.display="table-row";
		  document.getElementById("login").style.paddingBottom="30px";
	  }
	  function hiddendx(){
		  document.getElementById("dxyz").style.display="none";
		  document.getElementById("login").style.paddingBottom="10px";
	  }
	  function setReadOnly(){
		  document.getElementById("username").readOnly=true;
//		  document.getElementById("password").readOnly=true;
	  }
	  function setReadWrite(){
		  document.getElementById("username").readOnly=false;
//		  document.getElementById("password").readOnly=false;
	  }
  });