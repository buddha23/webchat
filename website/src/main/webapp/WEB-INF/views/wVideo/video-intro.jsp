<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${fn:escapeXml(vodVolumes.name)}"/>
    <jsp:param name="keywords" value="${fn:escapeXml(vodVolumes.name)}"/>
    <jsp:param name="description" value="${fn:escapeXml(vodVolumes.introduction)}"/>
    <jsp:param name="css" value="css/jquery.Jcrop.css"/>
</jsp:include>

<div class="content bg-white pdb40">
    <div class="container">
        <div class="video-intro clearfix">
            <div class="intro-top">
                <a class="preview-img"
                        <c:if test="${firstSection != null}"> href="javascript:;" onclick="goDetail(${firstSection.id},${firstSection.free})"</c:if>
                        <c:if test="${firstSection == null}"> href="javascript:;" onclick="layer.alert('该视频集暂时无法播放')"</c:if>
                   target="_blank">
                    <img class="" src="${bt:url(vodVolumes.cover,'files/','assets/images/demo/510.jpg')}">
                    <div class="preview-play"></div>
                </a>
                <div class="intro-header">
                    <div class="intro-info">
                        <h1 class="intro-title">${fn:replace(vodVolumes.name,".mp4","")}</h1>
                        <div class="intro-score">所需牛人币： <span class="score-num">${vodVolumes.costScore} 牛人币</span></div>
                        <ul class="intro-meta clearfix">
                            <li>总课时： ${vodVolumes.totleNum} 课</li>
                            <%--<li>播放： 1314 次</li>--%>
                            <li>讲师： ${fn:escapeXml(vodVolumes.lecturer)}</li>
                        </ul>
                    </div>
                    <div class="intro-sign">
                        <a class="sign-btn"
                                <c:if test="${firstSection != null}"> href="javascript:;" onclick="goDetail(${firstSection.id},${firstSection.free})"</c:if>
                                <c:if test="${firstSection == null}"> href="javascript:;" onclick="layer.alert('该视频暂时无法播放')"</c:if>
                           target="_blank">立即参加
                        </a>
                    </div>
                    <div class="operate-bar">
                        <%--<a class="bar-btn" href="javascript:;"><i class="fa fa-heart"></i> 收藏</a>--%>
                        <%--<a class="bar-btn" href="javascript:;"><i class="fa fa-share-alt"></i> 分享</a>--%>
                    </div>
                </div>
            </div>

            <div class="intro-content">
                <div class="intro-box tabs">
                    <ul class="subnav tabs-nav clearfix">
                        <li class="tabs-nav-item current"><a href="javascript:;">课程简介</a></li>
                        <li class="tabs-nav-item"><a href="javascript:;">课程目录</a></li>
                        <%--<li class="tabs-nav-item"><a href="javascript:;">评论（<strong>4</strong>）</a></li>--%>
                    </ul>

                    <div class="intro-panel tabs-panel">
                        <div class="tab">
                            <div class="tutorial-intro">
                                <%--<p><strong>摘要</strong></p>--%>
                                <%--<p>${fn:escapeXml(vodVolumes.summary)}</p>--%>
                                <p><strong>介绍</strong></p>
                                <p>${vodVolumes.introduction}</p>
                                <p><strong>内容</strong></p>
                                <p>${fn:escapeXml(vodVolumes.content)}</p>
                            </div>
                        </div>

                        <div class="tab">
                            <div class="tutorial-chapter">
                                <c:forEach items="${chapters}" var="chapter">
                                    <div class="chapter">
                                        <div class="chapter-header clearfix">
                                            <div class="chapter-num">第 ${chapter.sorting} 章</div>
                                            <div class="chapter-title">${fn:escapeXml(chapter.name)}</div>
                                        </div>
                                        <ul class="chapter-list">
                                            <c:forEach items="${chapter.vodSections}" var="section">
                                                <a class="list-item linkcolor" href="javascript:;"
                                                   onclick="goDetail(${section.id},${section.free})">
                                                        ${fn:escapeXml(section.name)}
                                                    <span class="tips">
                                                        （时长: <bt:durationToString duration="${section.duration}"/>）
                                                    </span>
                                                    <c:if test="${section.free == true}">
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
        </div>
    </div>
</div>

<!-- JiaThis -->
<script type="text/javascript" src="http://v1.jiathis.com/code/jia.js" charset="utf-8"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>

<script type="text/javascript">
    var costScore = ${vodVolumes.costScore};
    var memberScore = 0;
    if (costScore && costScore > 0) {
        memberScore = Math.round(costScore * 0.5);
    }
    function videoBuy(sectionId) {
        $.ajax({
            url: ctx + '/video/buy',
            type: 'post',
            data: {volumeId:${vodVolumes.id}},
            success: function () {
                layer.alert("购买成功!", {icon: 1, time: 1500}, function () {
                    window.open("${ctx}/video/detail/${vodVolumes.id}/" + sectionId);
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
                        })
            } else {
                location.href = "${ctx}/video/detail/${vodVolumes.id}/" + sectionId;
            }
        });
    }

    function goDetail(sectionId, fr) {
        if (fr == true || costScore == 0) {
            location.href = "${ctx}/video/detail/${vodVolumes.id}/" + sectionId;
        } else {
            OBJ_loginBox.checkLogin(function () {
                playvideo(sectionId)
            })
        }
    }
    $(function () {
        //选项卡切换
        if ($('.intro-box.tabs').length > 0) {
            //初始化
            $('.intro-box.tabs').each(function (index, element) {
                if ($(element).find('.current').length > 0) {
                    $(element).find('.tabs-panel .tab').eq($(element).find('.tabs-nav .tabs-nav-item.current').index()).show().siblings().hide();
                }
            });
            $('.intro-box.tabs .tabs-nav').children('.tabs-nav-item').click(function () {
                $(this).addClass('current').siblings().removeClass('current');
                $(this).parent().parent('.tabs').find('.tab').eq($(this).index()).show().siblings().hide();
            });
        }
        $('.nav .menu li a[data-nav="vdo"]').addClass('current');
    })
</script>

<jsp:include page="../footer.jsp"/>