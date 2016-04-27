/*$(document).ready(function(){
$(".logo").click(function () {
   $(".subnav").animate({height:'toggle'},100);
   $(".wrapper").toggleClass("wrappera");
});
});*/


$(function(){
$(".nav li").click(function () {
                $(".nav li").each(function () {
                    $(this).removeClass("current");
                });
                $(this).addClass("current");
            });
$('.nav li').click(function(){
 $(this).children('.current-cnt').show().end().siblings(this).children(".current-cnt").hide();
});
});


$(function(){ 
$(document).bind("click",function(e){ 
var target = $(e.target); 
if(target.closest(".nav li").length == 0){ 
$(".current-cnt").hide(); } 
});
});


$(document).ready(function(){
$('.user-cnt p').click(function(){
   $(this).css("background","#f1f1f1"); 
   $(".user-cnt ul").animate({height:'toggle'},80);
});
});


$(function(){ 
$(document).bind("click",function(e){ 
var target = $(e.target); 
if(target.closest(".user-cnt p").length == 0){ 
$(".user-cnt ul").hide(); } 
});
});


$(function(){ 
$(document).bind("click",function(e){ 
var target = $(e.target); 
if(target.closest(".user-cnt p").length == 0){ 
$(".user-cnt p").css("background","none"); } 
});
});


$(function(){
$(".msg").click(function () {
   $(".msg-cnt").animate({right:'0'},200);
});
});


$(function(){
$(".p a").click(function () {
   $(".msg-cnt").animate({right:'-280px'},200);
});
});

