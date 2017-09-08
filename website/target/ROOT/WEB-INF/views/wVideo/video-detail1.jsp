<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(section.name)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(section.name)}"/>
    <jsp:param name="description" value="${fn:escapeXml(section.name)}"/>
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white pd20">
    <div class="container">
        <div class="page-video-play">
            <div class="flowplayer" data-swf="${assets}/plugins/flowplayer/flowplayer.swf" data-ratio="0.4167">
                <video>
                    <source type="video/mp4" src="${section.dealURL}">
                </video>
            </div>

            <div class="play-list margintop20">
                <div class="tutorial-chapter">
                    <c:forEach items="${chapters}" var="chapter">
                        <div class="chapter">
                            <div class="chapter-header clearfix">
                                <div class="chapter-num">第 ${chapter.sorting} 章</div>
                                <div class="chapter-title">${fn:escapeXml(chapter.name)}</div>
                            </div>
                            <ul class="chapter-list">
                                <c:forEach items="${chapter.vodSections}" var="s">
                                    <a class="list-item linkcolor"
                                       <c:if test="${section.id == s.id}">style="background-color: #BDDCFF"</c:if>
                                       href="${ctx}/video/detail/${chapter.volumeId}/${s.id}">${fn:escapeXml(s.name)}
                                        <span class="tips">（时长: <bt:durationToString duration="${s.duration}"/>）</span>
                                    </a>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<link rel="stylesheet" href="http://g.alicdn.com/de/prismplayer/1.5.4/skins/default/index-min.css" type="text/css">
<link rel="stylesheet" href="${assets}/plugins/flowplayer/skin/functional.css">
<script type="text/javascript" src="http://g.alicdn.com/de/prismplayer/1.5.4/prism-min.js"></script>
<script type="text/javascript" src="${assets}/plugins/flowplayer/flowplayer.min.js"></script>
<%--<script type="text/javascript" src="http://g.alicdn.com/de/prismplayer/1.5.4/prism-flash-min.js"></script>--%>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>

<script type="text/javascript">

    $(function () {

        // 计时
        var isBuy = false;
        var costScore = ${vodVolumes.costScore};


        <c:if test="${!empty sessionScope.user.id}">checkIsBuy();
        </c:if>

        function checkIsBuy() {
            $.get(ctx + '/video/checkIsBuy?volumeId=' + ${vodVolumes.id}, function (data) {
                isBuy = data;
            })
        }

        var api = flowplayer();
        api.video.buffer = api.video.duration * ${vodScale};
        api.video.buffered = false;
        api.on("ready", function (e, api) {
            if (costScore == 0 || isBuy) {
                return true;
            } else {
                var ongoing = setInterval(function () {
                    if (api.playing == true) {
                        if (api.video.time < api.video.duration * ${vodScale}) {
                            return true;
                        } else {
                            api.pause();
                            OBJ_loginBox.checkLogin(function () {
                                checkIsBuy();
                                if (isBuy)
                                    return true;
                                var down = layer.confirm(
                                        '继续观看当前视频集需要消耗【' + costScore + '】牛人币，确认购买吗？',
                                        {btn: ['确定', '取消'], icon: 3},
                                        function () {
                                            layer.close(down);
                                            $.ajax({
                                                url: ctx + '/video/buy',
                                                type: 'post',
                                                data: {volumeId:${vodVolumes.id}},
                                                success: function () {
                                                    isBuy = true;
                                                    layer.alert("购买成功!");
                                                },
                                                error: function (r) {
                                                    layer.alert(JSON.parse(r.responseText).message, {
                                                        icon: 2,
                                                        time: 1500
                                                    });
                                                }
                                            })
                                        })
                            });
                        }
                    }
                }, 1000);
            }

            $('.nav .menu li a[data-nav="vdo"]').addClass('current');
        });
    })
</script>

<jsp:include page="../footer.jsp"/>