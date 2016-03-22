<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/inc/inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>父页面</title>
</head>
<body>
<table>
<tr>
父页面<br/>
	<input type="text" id="sendTextParent"/><input type="button" id="sendButtonParent" value="发给子页面"/><br/>
	来自子页面的消息：<div id="messageDivIdParent"/>
	
</tr>
<tr>
子页面<br/>
			<iframe src="child.jsp" allowtransparency="true" frameborder="0" scrolling="0" ></iframe>
</tr>
</table>
</body>


<script type="text/javascript">
(function () {
	seajs.use('app/demo/parent', function (ParentPager) {
		var parentPager = new ParentPager({element: document.body});
// 		pager.initialize();
		parentPager.render();
	});
})();
</script>
</html>