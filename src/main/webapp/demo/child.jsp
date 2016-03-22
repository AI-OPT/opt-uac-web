<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/inc/inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>子页面</title>
</head>
<body>
	<input type="text" id="sendText"/><input type="button" id="sendButton" value="发给父页面"/><br/>
	来自父页面的消息：<div id="messageDivId"/>
</body>
<script type="text/javascript">
(function () {
	seajs.use('app/demo/child', function (ChildPager) {
		var childPager = new ChildPager({element: document.body});
// 		pager.initialize();
		childPager.render();
	});
})();
</script>
</html>