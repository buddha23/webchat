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
        	<div class="page business-home">
            	<div class="home-top">
                </div>
                <div class="home-sub">
                	<div class="home-meta">
                		<div class="home-icon"><img src="../images/demo/avatar.png" alt=""/></div>
                        <div class="home-title"><span>${info.name}</span></div>
                        <div class="home-contact">联系电话：${info.telephone}</div>
                    </div>
                </div>
                <div class="home-section">
                    <div class="section-header">企业主营产品（服务）</div>
                    <div class="section-content">
                    	${info.service}
                    </div>
                </div>
                <div class="home-section">
                    <div class="section-header">企业介绍</div>
                    <div class="section-content">
                    	<img class="section-qr" src="${ctx}/files/${info.qrCode}" alt=""/>
                        <div class="section-text">${info.introduce}</div>
                    </div>
                </div>
            </div>
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
