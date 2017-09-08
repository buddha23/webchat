<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>500 | <spring:message code="site.name"/></title>
    <link rel="stylesheet" href="${assets}/css/public.css">
    <link rel="stylesheet" href="${assets}/css/style.css" type="text/css">
</head>
<body class="bg-white">
<div class="page-error">
    <div class="wrapper clearfix">
        <div class="error-number">500</div>
        <div class="error-title">服务器暂时无法相应</div>
        <ul class="error-tips">
            <li>如果您持续遇到这个错误，请通过<a href="#">email</a>通知我们此Bug。</li>
        </ul>
    </div>
</div>
</body>
</html>
