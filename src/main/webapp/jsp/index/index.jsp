<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<!--Support IE Text -->
<meta http-equiv="X-UA-Compatible" content="IE=Edge" /> 
</head>

<body>
 spring mvctest<br>
 租户ID：${tenant.tenantId }<br>
 租户名称：${tenant.tenantName }<br>

</body>
</html>