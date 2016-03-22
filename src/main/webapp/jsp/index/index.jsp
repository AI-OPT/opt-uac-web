<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>首页</title>
<%
    String _base = request.getContextPath();
			request.setAttribute("_base", _base);
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Pragma", "No-cache");
%>
<script>
	var _base = "${_base}";
</script>

</head>

<body>
 spring mvctest<br>
 租户ID：${tenant.tenantId }<br>
 租户名称：${tenant.tenantName }<br>

</body>
</html>