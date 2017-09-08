<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0" />
<title></title>
<link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
<link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
<script type="text/javascript" src="${assets}/js/min/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.ext.js"></script>
</head>

<body>
<div class="container">
	<div class="zoomable">
        <header>
            <div class="top">
                <div class="menu-left">
                    <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
                </div>
                
                <div class="menu-right">
                    <a class="menu-item menu-user" href="index.html#usercenter" onclick="location.reload()"></a>
                </div>
                
                <div class="title">CNC数控技术</div>
            </div>
        </header>
        
        <div class="content clearfix">
        	<c:if test="${list.content.size()>0}">
        	<div class="page business">
        
            	<ul class="business-list">
            	      <c:forEach items="${list.content}" var="info">
                        <li>
                        <a href= "${ctx}/m/enterprise/${info.id}">
                    	<div class="business-logo"><img src="${ctx}/files/${info.logoImg}" alt=""/></div>
                        <div class="business-info">
                        	<div class="business-title">${info.name}</div>
                            <div class="business-contact"><i class="fa fa-phone-square"></i> ${info.telephone}</div>
                            <div class="business-intro">${info.introduce}</div>
                        </div>
                        </a>
                    </li>
                    </c:forEach>
                </ul>
            </div>
             <bt:pagination data="${list}"/>
             </c:if>
        </div>
        
        <footer>
        </footer>
    </div>
</div>
</body>

<script>
$(function () {
})
</script>
</html>
