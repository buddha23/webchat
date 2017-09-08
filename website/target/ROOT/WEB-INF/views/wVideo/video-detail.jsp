<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(section.name)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(section.name)}"/>
    <jsp:param name="description" value="${fn:escapeXml(section.name)}"/>
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white pd20">
    <div class="container">
        <div class="page-video-play">
            <div class="player is-splash" data-embed="false"
                 style="height: 600px; background-color:#777; background-image: url(${files}/${vodVolumes.cover});"></div>
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
                                    <a class="list-item linkcolor" href="javascript:;"
                                       onclick="goDetail(${s.id},${s.free})"
                                       <c:if test="${section.id == s.id}">style="background-color: #BDDCFF"</c:if>>
                                            ${fn:escapeXml(s.name)}
                                        <span class="tips">（时长: <bt:durationToString duration="${s.duration}"/>）</span>
                                        <c:if test="${s.free == true}">
                                            <span class="label label-red marginleft5">免费试听</span>
                                        </c:if>
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

<link rel="stylesheet" href="${assets}/plugins/flowplayer/skin/functional.css">
<script type="text/javascript" src="${assets}/plugins/flowplayer/flowplayer.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>

<script type="text/javascript">
    var costScore = ${vodVolumes.costScore};
    var memberScore = 0;
    if (costScore && costScore > 0) {
        memberScore = Math.round(costScore * 0.5);
    }
    $(function () {
        $.get(ctx + 'video/getDealURL', {sectionId: "${section.id}", volumeId: "${vodVolumes.id}"}, function (data) {
            if (data) {
                var $player = $(".player").flowplayer({
                    clip: {
                        sources: [{
                            type: "video/mp4",
                            src: data
                        }]
                    }
                });
                var api = $player.eq(0).data("flowplayer");
                api.play(0);
                $('.nav .menu li a[data-nav="vdo"]').addClass('current');
            }
        });
    });

    function goDetail(sectionId, fr) {
        if (fr == true || costScore == 0) {
            location.href = "${ctx}/video/detail/${vodVolumes.id}/" + sectionId;
        } else {
            OBJ_loginBox.checkLogin(function () {
                playvideo(sectionId)
            });
        }
    }

    function videoBuy(sectionId) {
        $.ajax({
            url: ctx + '/video/buy',
            type: 'post',
            data: {volumeId:${vodVolumes.id}},
            success: function () {
                layer.alert("购买成功!", {icon: 1, time: 1500}, function () {
                    location.href = "${ctx}/video/detail/${vodVolumes.id}/" + sectionId;
                });
            },
            error: function (r) {
                layer.alert(JSON.parse(r.responseText).message, {icon: 2, time: 1500});
            }
        })
    }

    function playvideo(sectionId) {
        $.get(ctx + 'video/checkIsBuy', {sectionId: sectionId, volumeId: "${vodVolumes.id}"}, function (data) {
            if (data == false) {
                var down = layer.confirm(
                        '一次购买，无限次学习，普通会员 ' + costScore + ' 牛人币，<br><a href="${ctx}/userAccount/vip" target="blank" style="color:red;">VIP尊享价</a> ' + memberScore + ' 牛人币，立即支付？',
                        {btn: ['确定', '取消'], icon: 3},
                        function () {
                            videoBuy(sectionId);
                            layer.close(down);
                        });
            } else {
                location.href = "${ctx}/video/detail/${vodVolumes.id}/" + sectionId;
            }
        });
    }
</script>

<jsp:include page="../footer.jsp"/>