<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
<link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
<script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="${assets}/js/app.js"></script>
<title>${category.name}</title>
</head>

<body>
<div class="container">
	<header>
    	<a id="prevPage" class="prev" href="javascript:history.back();">
        	<i class="fa fa-chevron-left"></i>
        </a>
        
    	<div class="menu">
        </div>
    	
    	<div class="title">${category.name}</div>
    </header>
    
    <div class="content bg-white clearfix">
        <div class="page">
	        <div class="section">
	        	<div class="section-content">
	        		<div class="doc-list">
						<c:forEach items="${docs.content}" var="doc">
							<a class="${doc.fileType} clearfix" href="${ctx}/m/doc/${doc.id}?categoryId=${doc.docCategory.fid}">
								<div class="doc-icon"></div>
								<div class="doc-content">
									<div class="doc-title">${doc.title}</div>
									<div class="doc-meta">
									<span>
										<bt:fileSize size="${doc.fileSize}"/>
									</span>
										<span>上传：<c:if test="${doc.user.nickname==null}">系统管理员</c:if>${doc.user.nickname}</span>
									</div>
								</div>
							</a>
							</c:forEach>
	        		</div>
					<bt:pagination data="${docs}"/>
	        	</div>
	        </div>
        </div>
    </div>
    
    <footer>
    </footer>
</div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
	// 微信分享
	var title = '${fCategory.name}';
	if (title == '') title = '大牛数控文库';
	var imgUrl = 'http://www.d6sk.com/files/${fCategory.icon}';
	if (imgUrl == 'http://www.d6sk.com/files/') imgUrl = 'http://www.d6sk.com/assets/images/logo(500).jpg';
	var desc = '${fn:escapeXml(fCategory.description)}';
	if (desc == '') desc = '大牛数控-国内最大的数控技术交流社区';
	var link = location.href;

	$.post('${ctx}/weixin/signature', {url: location.href}, function (data) {
		wx.config({
			debug: false,
			appId: data.appId,
			timestamp: data.timestamp,
			nonceStr: data.noncestr,
			signature: data.signature,
			jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone']
		});
	});
	wx.ready(function () {
		wx.onMenuShareTimeline({    // 朋友圈
			title: title,
			link: link,
			imgUrl: imgUrl,
			success: function () {
				console.log('朋友圈分享成功！');
			},
			cancel: function () {
				console.log('朋友圈分享取消！');
			}
		});
		wx.onMenuShareAppMessage({ // 朋友
			title: title,
			link: link,
			imgUrl: imgUrl,
			desc: desc,
			success: function () {
				console.log('朋友分享成功！');
			},
			cancel: function () {
				console.log('朋友分享取消！');
			}
		});
		wx.onMenuShareQQ({ // QQ
			title: title,
			link: link,
			imgUrl: imgUrl,
			desc: desc,
			success: function () {
				console.log('QQ分享成功！');
			},
			cancel: function () {
				console.log('QQ分享取消！');
			}
		});
		wx.onMenuShareWeibo({ // 微博
			title: title,
			link: link,
			imgUrl: imgUrl,
			desc: desc,
			success: function () {
				console.log('微博分享成功！');
			},
			cancel: function () {
				console.log('微博分享取消！');
			}
		});
		wx.onMenuShareQZone({ // QZone
			title: title,
			link: link,
			imgUrl: imgUrl,
			desc: desc,
			success: function () {
				console.log('QZone分享成功！');
			},
			cancel: function () {
				console.log('QZone分享取消！');
			}
		});
	});
	wx.error(function (res) {
		console.log(res);
	});
</script>
</body>
</html>
