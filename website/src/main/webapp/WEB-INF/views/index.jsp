<jsp:include page="header.jsp"/>

<script>
    $(function () {
        setInterval(function () {
            $('.member .marquee dl:first').animate({
                marginTop: -$('.member .marquee dl:first').outerHeight()
            }, 500, 'swing', function () {
                $(this).removeAttr('style').insertAfter('.member .marquee dl:last');
            });
        }, 5000);

        $('.category .more').click(function(){
            if(!$('.category .category-list').hasClass('open')){
                $('.category .category-list').addClass('open');
                $(this).text('- 收起');
            }
            else{
                $('.category .category-list').removeClass('open');
                $(this).text('- 展开');
            }
        });
    })
</script>
<%--广告位出租O(∩_∩)O~--%>
<c:if test="${imgFileList.size() != 0}">
    <div class="banner">
        <div class="banner-pic">
            <c:forEach items="${imgFileList}" var="img" varStatus="imgStatus">
                <a href="${bt:out(img.link,'javascript:void(0);')}" target="_blank" class="banner_p${imgStatus.count} bgchange" style="background:url(${ctx}/files/user/${img.path}) center no-repeat"></a>
            </c:forEach>
                <%--<div class="banner-mask"></div>--%>
            <div class="banner-leftmask">
                <div class="banner-prev"></div>
            </div>
            <div class="banner-rightmask">
                <div class="banner-next"></div>
            </div>
        </div>

        <div class="banner-box">
            <div class="mini-slides clearfix">
                <div class="mini-left-arrow">上一页</div>
                <div class="mini-slide-container">
                    <div class="slide-viewport"></div>
                    <div class="slides clearfix">
                        <c:forEach items="${imgFileList}" var="img">
                            <li><img src="${ctx}/files/user/${img.path}"/></li>
                        </c:forEach>
                    </div>
                </div>
                <div class="mini-right-arrow">下一页</div>
            </div>
        </div>
    </div>
</c:if>

<div class="content clearfix pd">
    <div class="container clearfix">
        <div class="index-main">
            <div class="category">
                <h4>推荐版块<a class="more" href="javascript:;">- 展开</a></h4>
                <div class="category-list clearfix">
                    <c:forEach items="${docCategories}" var="docCategory">
                        <dl>
                            <a href="${ctx}/doc/?c1=${docCategory.id}" class="linkcolor linkover">
                                <dt><img src="${bt:url(docCategory.icon,"/files/" ,"assets/images/logo.jpg" )}" alt="${fn:escapeXml(docCategory.name)}"/></dt>
                                <dd>${fn:escapeXml(docCategory.name)}</dd>
                            </a>
                        </dl>
                    </c:forEach>
                    <dl>
                        <a href="${ctx}/doc" class="linkcolor linkover">
                            <dt><img src="${assets}/images/logo.jpg" alt="大牛数控"/></dt>
                            <dd>更多</dd>
                        </a>
                    </dl>
                </div>
            </div>

            <div class="recommend margintop40">
                <h4>每日推荐</h4>
                <div class="recommend-content clearfix">
                    <div class="recommend-list recommend-left">
                        <c:forEach items="${leftRecommendDocs}" var="video">
                            <dl class="${video.fileType} clearfix">
                                <dt><a class="linkline linkcolor" href="${ctx}/doc/${video.id}">${fn:escapeXml(video.title)}</a>
                                </dt>
                                <dd>阅读：${video.views}</dd>
                            </dl>
                        </c:forEach>
                    </div>
                    <div class="recommend-list recommend-right">
                        <c:forEach items="${rightRecommendDocs}" var="video">
                            <dl class="${video.fileType} clearfix">
                                <dt><a class="linkline linkcolor" href="${ctx}/doc/${video.id}">${fn:escapeXml(video.title)}</a>
                                </dt>
                                <dd>阅读：${video.views}</dd>
                            </dl>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="doc-type margintop40">
                <h4>分类筛选</h4>
                <ul class="type-tab clearfix">
                    <li data-type="pdf" class="current">pdf文档</li>
                    <li data-type="doc">word文档</li>
                    <li data-type="excel">excel文档</li>
                    <li data-type="ppt">ppt文档</li>
                    <li data-type="txt">txt文档</li>
                    <li data-type="jpg">图片</li>
                </ul>
                <div>
                    <ul class="doc-type-list clearfix" id="pdf">
                        <c:forEach items="${pdfDocs}" var="video">
                            <li>
                                <a href="${ctx}/doc/${video.id}" class="linkcolor linkover">
                                    <div class="cover vertical">
                                        <div class="cover-preview">
                                            <img src="${files}/${video.imagePath}" alt="${fn:escapeXml(video.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list clearfix" id="doc" hidden="hidden">
                        <c:forEach items="${docDocs}" var="video">
                            <li>
                                <a href="${ctx}/doc/${video.id}"
                                   class="linkcolor linkover">
                                    <div class="cover vertical">
                                        <div class="cover-preview">
                                            <img src="${files}/${video.imagePath}" alt="${fn:escapeXml(video.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list clearfix" id="excel" hidden="hidden">
                        <c:forEach items="${xlsDocs}" var="video">
                            <li>
                                <a href="${ctx}/doc/${video.id}"
                                   class="linkcolor linkover">
                                    <div class="cover">
                                        <div class="cover-preview">
                                            <img src="${files}/${video.imagePath}" alt="${fn:escapeXml(video.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list clearfix" id="ppt" hidden="hidden">
                        <c:forEach items="${pptDocs}" var="video">
                            <li>
                                <a href="${ctx}/doc/${video.id}"
                                   class="linkcolor linkover">
                                    <div class="cover horizontal">
                                        <div class="cover-preview">
                                            <img src="${files}/${video.imagePath}" alt="${fn:escapeXml(video.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list clearfix" id="txt" hidden="hidden">
                        <c:forEach items="${txtDocs}" var="video">
                            <li>
                                <a href="${ctx}/doc/${video.id}"
                                   class="linkcolor linkover">
                                    <div class="cover">
                                        <div class="cover-preview">
                                            <img src="${assets}/images/cover_txt.png" alt="${fn:escapeXml(video.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                    <ul class="doc-type-list clearfix" id="jpg" hidden="hidden">
                        <c:forEach items="${jpgDocs}" var="video">
                            <li>
                                <a href="${ctx}/doc/${video.id}"
                                   class="linkcolor linkover">
                                    <div class="cover">
                                        <div class="cover-preview">
                                            <img src="${files}/${video.filePath}" alt="${fn:escapeXml(video.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="doc-type margintop20">
                <h4>精品教程</h4>
                <ul class="doc-tab clearfix"></ul>
                <div>
                    <ul class="doc-type-list">
                        <c:forEach items="${courses.content}" var="course">
                            <li>
                                <a href="${ctx}/course/detail/${course.id}" class="linkcolor linkover">
                                    <div class="cover vertical">
                                        <div class="cover-preview">
                                            <img src="${files}/${course.imgUrl}" alt="${fn:escapeXml(course.title)}"/>
                                        </div>
                                    </div>
                                    <div class="doc-title">${fn:escapeXml(course.title)}</div>
                                </a>
                                <div class="count">${course.visits}人阅读</div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

        <div class="index-side">
            <div class="member">
                <div class="member-header">
                    <h4>注册会员</h4>
                    <div class="count"><em>1${userCount}</em>人</div>
                </div>
                <div class="member-content">
                    <div id="marquee" class="marquee">
                        <c:forEach items="${leftRecommendDocs}" var="video">
                            <dl>
                                <dt>${fn:escapeXml(video.diffTime)}前</dt>
                                <dd class="clearfix">
                                    <img src="${bt:url(video.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt="${bt:out(video.user.nickname,video.user.name)}"/>
                                    <div class="member-info">
                                        <div class="member-name">
                                            <c:if test="${video.uploaderType==1}">系统管理员</c:if>
                                            <c:if test="${video.uploaderType!=1}">${bt:out(video.user.nickname,video.user.name)}</c:if>
                                        </div>
                                            <%--member-act--%>
                                        <a href="${ctx}/doc/${video.id}"
                                           class="linkcolor linkover">上传文档《${fn:escapeXml(video.title)}》</a>
                                    </div>
                                </dd>
                            </dl>
                        </c:forEach>
                        <c:forEach items="${rightRecommendDocs}" var="video">
                            <dl>
                                <dt>${fn:escapeXml(video.diffTime)}前</dt>
                                <dd class="clearfix">
                                    <img src="${bt:url(video.user.avatar,'files/','assets/images/demo/p1.jpg')}" alt="${bt:out(video.user.nickname,video.user.name)}"/>
                                    <div class="member-info">
                                        <div class="member-name">
                                            <c:if test="${video.uploaderType==1}">系统管理员</c:if>
                                            <c:if test="${video.uploaderType!=1}">${bt:out(video.user.nickname,video.user.name)}</c:if>
                                        </div>
                                        <a href="${ctx}/doc/${video.id}" class="linkcolor linkover">上传文档《${fn:escapeXml(video.title)}》</a>
                                    </div>
                                </dd>
                            </dl>
                        </c:forEach>
                    </div>
                </div>
            </div>

            <div class="hots margintop30">
                <h4>热门文档</h4>
                <div class="hots-list">
                    <c:forEach items="${hotDocs}" var="video" varStatus="docStatus">
                        <dl class="clearfix">
                            <dt>${docStatus.count}</dt>
                            <dd>
                                <a href="${ctx}/doc/${video.id}" class="linkcolor linkover">
                                    <div class="hots-title">${fn:escapeXml(video.title)}</div>
                                </a>
                                <div class="count">${video.views}人阅读</div>
                            </dd>
                        </dl>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.easing.min.js"></script>
<script type="text/javascript" src="${assets}/js/photowall.js"></script>
<jsp:include page="index-footer.jsp"/>
<%--<%@include file="index-footer.jsp" %>--%>
<script>
    $(function () {
        $(".doc-type").find("li").click(function () {
            $(this).addClass("current");
            $(this).siblings().removeClass("current");
            var type = $(this).attr("data-type");
            $("#" + type).show();
            $("#" + type).siblings().hide();
        });

        $('.nav .menu li a[data-nav="index"]').addClass('current');
    })
</script>
<style>
    .banner_p1, .banner_p2, .banner_p3, .banner_p4, .banner_p5 {
        height: 300px;
        width: 100%;
        position: absolute;
        overflow: hidden;
        display: none;
    }

    .banner_p1 {
        display: block;
    }

    .banner_p1 img, .banner_p2 img, .banner_p3 img, .banner_p4 img, .banner_p5 img {
        min-height: 300px;
    }

</style>
