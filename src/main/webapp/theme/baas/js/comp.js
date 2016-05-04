$(function(){
$(".login-wrapper-cnt-section .user").click(function () {
	$(".login-wrapper-cnt-section .user i").css('border-color', '#0697ef')
	$(".login-wrapper-cnt-section .user i").css('color', '#0697ef')
	$(".login-wrapper-cnt-section .user").css('border-color', '#0697ef')
	$(".login-wrapper-cnt-section .password i").css('border-color', '#666')
	$(".login-wrapper-cnt-section .password i").css('color', '#666')
	$(".login-wrapper-cnt-section .password").css('border-color', '#e4e4e4')
});});

$(function(){
$(".login-wrapper-cnt-section .password").hover(function () {
	$(".login-wrapper-cnt-section .password").css('border-color', '#e4e4e4')
});});




$(function(){
$(".login-wrapper-cnt-section .password").mouseout(function () {
	$(".login-wrapper-cnt-section .password i").css('border-color', '#666')
	$(".login-wrapper-cnt-section .password i").css('color', '#666')
	$(".login-wrapper-cnt-section .password").css('border-color', '#e4e4e4')
});});

$(function(){
$(".login-wrapper-cnt-section .user").mouseout(function () {
	$(".login-wrapper-cnt-section .user i").css('border-color', '#666')
	$(".login-wrapper-cnt-section .user i").css('color', '#666')
	$(".login-wrapper-cnt-section .user").css('border-color', '#e4e4e4')
});});


$(function(){
$(".login-wrapper-cnt-section .password").click(function () {
	$(".login-wrapper-cnt-section .password i").css('border-color', '#0697ef')
	$(".login-wrapper-cnt-section .password i").css('color', '#0697ef')
	$(".login-wrapper-cnt-section .password").css('border-color', '#0697ef')
	$(".login-wrapper-cnt-section .user i").css('border-color', '#666')
	$(".login-wrapper-cnt-section .user i").css('color', '#666')
	$(".login-wrapper-cnt-section .user").css('border-color', '#e4e4e4')
});
});

$(document).ready(function(){
$(".int-xlarge").click(function () {
   $(".login-wrapper-cnt-section ul .user i").animate({width:'26px',fontSize:'12px',marginTop:'18px'},100);
   $(".login-wrapper-cnt-section ul .password i").animate({width:'43px',fontSize:'20px',marginTop:'14px'},100);
});
});
$(document).ready(function(){
$(".int-xlarge-password").click(function () {
   $(".login-wrapper-cnt-section ul .password i").animate({width:'26px',fontSize:'12px',marginTop:'18px'},100);
   $(".login-wrapper-cnt-section ul .user i").animate({width:'43px',fontSize:'20px',marginTop:'14px'},100);
});
});

$(document).ready(function(){
$(".int-xlarge-password").mouseout(function () {
   $(".login-wrapper-cnt-section ul .password i").animate({width:'43px',fontSize:'20px',marginTop:'14px'},100);
});
});

$(document).ready(function(){
$(".int-xlarge").mouseout(function () {
   $(".login-wrapper-cnt-section ul .user i").animate({width:'43px',fontSize:'20px',marginTop:'14px'},100);
});
});

$(function(){
$(".regsiter-wrapper-cnt .user").click(function () {
	$(".regsiter-wrapper-cnt .user").css('border-color', '#0697ef')
	$(".regsiter-wrapper-cnt .password .icon-eye-open").css('color', '#666')
	$(".regsiter-wrapper-cnt .password").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .SMSidentifying").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .int-xlarge-identifying").css('border-color', '#e4e4e4')
	
});});

$(function(){
$(".regsiter-wrapper-cnt .password").click(function () {
	$(".regsiter-wrapper-cnt .password").css('border-color', '#0697ef')
	$(".regsiter-wrapper-cnt .password .icon-eye-open").css('color', '#0697ef')
	$(".regsiter-wrapper-cnt .user").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .SMSidentifying").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .int-xlarge-identifying").css('border-color', '#e4e4e4')
});});


$(function(){
$(".regsiter-wrapper-cnt .SMSidentifying").click(function () {
	$(".regsiter-wrapper-cnt .SMSidentifying").css('border-color', '#0697ef')
	$(".regsiter-wrapper-cnt .user").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .password .icon-eye-open").css('color', '#666')
	$(".regsiter-wrapper-cnt .password").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .int-xlarge-identifying").css('border-color', '#e4e4e4')
});});

$(function(){
$(".regsiter-wrapper-cnt .int-xlarge-identifying").click(function () {
	$(".regsiter-wrapper-cnt .int-xlarge-identifying").css('border-color', '#0697ef')
	$(".regsiter-wrapper-cnt .user").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .password .icon-eye-open").css('color', '#666')
	$(".regsiter-wrapper-cnt .password").css('border-color', '#e4e4e4')
	$(".regsiter-wrapper-cnt .SMSidentifying").css('border-color', '#e4e4e4')
});});


$(document).ready(function(){
$(".ctn-a").click(function(){
  $(".ctn-b").show();
  $(this).hide();
});
});



	$(function(){
//			$("img").lazyload({
//				placeholder : "images/loading.gif",
//				effect: "fadeIn"
//			});
			$('#dowebok').fullpage({
//             sectionsColor: ['#0581B2', '#4BBFC3', '#7BAABE','#daa520'],
//                //设置每一屏幕的背景颜色
				scrollingSpeed:1000,
				// 设置滚动的花费时间
				easingcss3:'ease-in-out',
				//运动曲线
				continuousVertical: true,
				//到达最后一屏后，是否回到首屏
				'navigation': true,
				// 右侧显示小圆点

				/*顶部导航栏*/
				anchors:['page1','page2','page3','page4'],
				// 值为绑定菜单的类名或ID名，需先设置achors才能生效
				menu:'#menu',
				/*控制首屏*/
				afterLoad: function(anchorLink, index){
					if(index==1){
						$('.section1').removeClass('comeout');
					}
				},

				//来判断当前的屏幕
				onLeave: function(index, nextIndex, direction){
					if(index==1){
						$('.section1 .twotp').animate({marginRight:'0'},700,function(){$('.section1 .two-nr').animate({marginLeft:'0'},500);});
					}
					else if(index==2){
						$('.section2 .three-nr').animate({marginRight:'0'},1000);
						$('.section2 .threetp').animate({marginLeft:'0'},1000);
					}
					else if(index==3){
						$('.section3 .two-nr').animate({marginLeft:'0'},700,function(){$('.section3 .twotp').animate({marginRight:'0'},500);});
					}
					else if(index==4){
						$('.section4');
					}
					else if(index==5){
						$('.section5');
					}
					if(nextIndex==0){ 
						$('.section5');
					}
					else if(nextIndex==2){
						$('.section1 .twotp').animate({marginRight:'0'},700,function(){$('.section1 .two-nr').animate({marginLeft:'0'},500);});
					}
					else if(nextIndex==3){
						$('.section2 .three-nr').animate({marginRight:'0'},1000);
						$('.section2 .threetp').animate({marginLeft:'0'},1000);
					}
					else if(nextIndex==4){
						$('.section3 .two-nr').animate({marginLeft:'0'},700,function(){$('.section3 .twotp').animate({marginRight:'0'},500);});
					}
					else if(nextIndex==5){
						$('.section4');
					}
				},



			
			});
		});