<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>数控文库</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
</head>

<body>
<div class="container">
    <div class="content clearfix">
        <c:if test="${!empty s}">${s.content}</c:if>
        <c:if test="${empty s}"><img width="100%" src="${assets}/images/instructions.jpg" alt=""/></c:if>
    </div>

    <footer></footer>
</div>
</body>
</html>
