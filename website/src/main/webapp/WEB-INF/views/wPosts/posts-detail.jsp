<jsp:include page="../header.jsp">
    <jsp:param name="title" value="${postsInfos.title}"/>
    <jsp:param name="keywords" value="${postsInfos.title},学数控有前途吗,数控编程,数控技术工资"/>
    <jsp:param name="description" value="${postsInfos.title}-学数控有前途吗,数控工资多少,数控技术探讨,尽在大牛数控网"/>
    <jsp:param name="css" value="plugins/froalaEditor/css/froala_editor.pkgd.min.css"/>
</jsp:include>

<div class="content bg-grey pd20">
    <div class="container">
        <div class="page-qa clearfix">
            <div class="qa-aside">
                <div class="userbar">
                    <a class="question-btn" onclick="OBJ_loginBox.checkLogin(operate.publishPosts)">
                        <i class="fa fa-question-circle"></i> 我要提问</a>
                </div>
            </div>

            <div class="qa-main">
                <div class="qa-article">
                    <div class="qa-profile">
                        <img class="avatar"
                             src="${bt:url(postsInfos.userAvatar, 'files/', 'assets/images/default_avatar.png')}"
                             alt=" ${fn:escapeXml(postsInfos.userNickname)}"/>
                    </div>
                    <div class="qa-info">
                        <h1 class="qa-title">
                            <div class="qa-title">
                                <c:if test="${postsInfos.postsReward.state == 1 && postsInfos.postsReward.type == 1}">
                                    <div class="qa-reward qa-icon-offer">${postsInfos.postsReward.amount} 牛人币</div>
                                </c:if>
                                <c:if test="${postsInfos.postsReward.state == 1 && postsInfos.postsReward.type == 2}">
                                    <div class="qa-reward qa-icon-offer">${postsInfos.postsReward.amount} 积分</div>
                                </c:if>
                                <c:if test="${postsInfos.postsReward.state == 2}">
                                    <div class="qa-reward qa-icon-end">已悬赏</div>
                                </c:if>
                                ${fn:escapeXml(postsInfos.title)}
                            </div>
                        </h1>
                        <div class="qa-meta">
                            <span><i class="icon-user"></i> ${fn:escapeXml(postsInfos.userNickname)}</span>
                            <span><i class="icon-calendar"></i>
                                <fmt:formatDate value="${postsInfos.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                            <span><i class="icon-eye"></i> ${postsInfos.views}</span>
                            <span><i class="icon-bubble"></i> ${comments.totalElements}</span>
                        </div>
                    </div>

                    <div class="qa-content">
                        <p>${postsInfos.content}</p>
                    </div>

                    <c:if test="${postsInfos.postsReward.state == 1}">
                        <div class="qa-countdown">
                            悬赏截止时间： <span class="countdown-day fix"></span>
                        </div>
                    </c:if>
                </div>

                <div class="qa-comment">
                    <div class="comment-header">评论（${comments.totalElements}）</div>
                    <ul class="comment-list">
                        <c:if test="${postsInfos.postsReward.commentId != null}">
                            <li class="clearfix">
                                <div class="comment-avatar">
                                    <img src="${bt:url(bestComment.userAvatar, 'files/', 'assets/images/default_avatar.png')}" alt="${fn:escapeXml(bestComment.userNickname)}">
                                </div>
                                <div class="comment-info">
                                    <div class="comment-title clearfix">
                                        <span class="name">${fn:escapeXml(bestComment.userNickname)}</span>
                                            <span class="date">
                                                <fmt:formatDate value="${bestComment.createTime}"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </span>
                                        <div class="check-btn"><i class="fa fa-thumbs-up"></i> 最佳答案</div>
                                    </div>
                                    <div class="comment-msg">${fn:escapeXml(bestComment.content)}</div>
                                </div>
                            </li>
                        </c:if>
                        <c:forEach items="${comments.content}" var="comment">
                            <li class="clearfix">
                                <div class="comment-avatar">
                                    <img src="${bt:url(comment.userAvatar, 'files/', 'assets/images/default_avatar.png')}" alt="${fn:escapeXml(comment.userNickname)}">
                                </div>
                                <div class="comment-info">
                                    <div class="comment-title clearfix">
                                        <div class="name">${fn:escapeXml(comment.userNickname)}</div>
                                            <%--<div class="date">--%>
                                            <%--<fmt:formatDate value="${comment.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
                                            <%--</div>--%>
                                        <c:if test="${sessionScope.user.id == postsInfos.userId && sessionScope.user.id != comment.userId && postsInfos.postsReward != null && postsInfos.postsReward.state == 1}">
                                            <div class="check-btn" data-commentId="${comment.id}" onclick="choseBest(${comment.id})"><i class="fa fa-thumbs-up"></i> 选为最佳
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="comment-msg">${fn:escapeXml(comment.content)}</div>
                                    <div class="comment-meta">
                                        <div class="meta-item"><fmt:formatDate value="${comment.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                        <div class="meta-item">
                                            <c:if test="${comment.hasLike != null && comment.hasLike == true}">
                                                <a class="linkline linkcolor" href="javascript:;">已赞 (${comment.likeNum})</a>
                                            </c:if>
                                            <c:if test="${comment.hasLike == null || comment.hasLike == false}">
                                                <a class="linkline linkcolor nolike" href="javascript:;" data-id="${comment.id}">
                                                    <span class="like${comment.id}">赞</span>
                                                    (<span class="likeNum${comment.id}">${comment.likeNum}</span>)
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>

                    <!-- 分页 -->
                    <div class="clearfix">
                        <div class="comment-pagination fl clearfix">
                            <bt:paginationSite styleClass="comment-pagination" data="${comments}"
                                               basePath="${ctx}/post/${postsInfos.id}"/>
                        </div>
                    </div>
                </div>

                <div class="comment-bar">
                    <div class="comment-bar-wrapper">
                        <form id="commentForm" action="${ctx}/post/addComment" method="post">
                            <div class="comment-add">
                                <div class="comment-avatar">
                                    <img src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}"
                                         alt="大牛数控">
                                </div>
                                <div class="comment-inputbox">
                                    <div class="comment-reply">
                                        <textarea placeholder="回复：" name="reply" class="ui-textarea-default"
                                                  onkeydown="keySend(event);" required></textarea>
                                    </div>
                                    <div class="comment-captcha">
                                        <input type="text" name="postsId" value="${postsInfos.id}"
                                               style="display: none"/>
                                        <input name="captcha" type="text" class="ui-input-default"
                                               onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" style="width: 170px;"
                                               placeholder="验证码(看不清?点击图片)" maxlength="10">
                                        <img class="captcha-img" src="${assets}/images/captcha.png"
                                             style="margin-left: 4px;width: 123px;height: 32px;">
                                    </div>
                                    <div class="comment-operate">
                                        <input id="commentSubmit" class="button btn-primary" type="button" value="提交"
                                               onclick="OBJ_loginBox.checkLogin(operate.publishComments)">
                                        <span class="tips">按ctrl+enter快速回复，</span>
                                        <span class="count-tips">剩余字符：<em>999</em></span>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="_editor.jsp"/>

<script type="text/javascript">
    $('.nav .menu li a[data-nav="qa"]').addClass('current');

    $(function () {
        var $countEl = $('.comment-reply textarea');
        var $countTips = $('.count-tips');
        var allowed = 999;
        var warning = 25;
        var cssWarning = 'warning';
        var cssExceeded = 'exceeded';
        var counterText = '剩余字符：';
        var counterExceeded = '超出字符：';

        function calculate() {
            var count = $countEl.val().length;
            var available = allowed - count;
            if (available <= warning && available >= 0) {
                $countTips.addClass(cssWarning);
            } else {
                $countTips.removeClass(cssWarning);
            }
            if (available < 0) {
                $countTips.addClass(cssExceeded).html(counterExceeded + '<em>' + -available + '</em>');
            } else {
                $countTips.removeClass(cssExceeded).html(counterText + '<em>' + available + '</em>');
            }
        }

        calculate();
        $countEl.keyup(function () {
            calculate()
        });
        $countEl.change(function () {
            calculate()
        });

    });

    function keySend(event) {
        if (event.ctrlKey && event.keyCode == 13) {
            $("#commentSubmit").click();
        }
    }

    <c:if test="${postsInfos.postsReward.state == 1}">
    var finishTime = new Date('${postsInfos.postsReward.finishTime}');
    if ($(".countdown-day").length > 0) {
        $(".countdown-day").countdown({
            date: finishTime.Format("yyyy/MM/dd h:m:s"),
            dataAttr: "cdate"
        });
    }
    </c:if>

    var postId = ${postsInfos.id};

    function choseBest(commentId) {
        if (postId && commentId) {
            layer.confirm('确认选择该答案为最佳答案并将下发悬赏？', function () {
                $.ajax({
                    url: ctx + '/post/choseBestComment',
                    type: 'POST',
                    data: {postsId: postId, commentId: commentId},
                    success: function (data) {
                        layer.alert("操作成功!", {icon: 1}, function () {
                            location.reload();
                        });
                    },
                    error: function (r) {
                        layer.alert('操作失败', {icon: 2});
                        console.log(r);
                    }
                });
            });
        }
    }

    $('.nolike').click(function (e) {
        var commentId = $(this).attr('data-id');
//        console.log(commentId);
        e.stopPropagation();
        if (!isLogin) {
            var pagei = layer.open({
                type: 1,   //0-4的选择,
                title: "登录",
                border: [0],
                shadeClose: true,
                area: ['auto', '360px'],
                content: $('#loginDialog')
            });
        } else {
            if (isBind) {
                doLike(commentId)
            } else {
                bindUrl = window.location.href;
                var pagei = layer.open({
                    type: 1,   //0-4的选择,
                    title: "绑定账号",
                    border: [0],
                    shadeClose: true,
                    area: ['auto', '360px'],
                    content: $("#accountDialog")
                });
            }
        }
    });

    function doLike(commentId) {
        var $like = $('.like' + commentId);
        var $scoreNum = $('.likeNum' + commentId);
        if ($like.html() == '赞') {
//            console.log('感觉自己萌萌哒O(∩_∩)O~');
            $.ajax({
                url: '${ctx}/post/doLike',
                type: 'post',
                data: {'commentId': commentId},
                success: function () {
                    $like.text('已赞');
                    var left = parseInt($scoreNum.position().left) + 10, top = parseInt($scoreNum.position().top) - 10;
                    $like.append('<div class="sign-score color-blue"><b>+' + 1 + '<\/b></\div>');
                    $('.sign-score').css({
                        'position': 'absolute',
                        'z-index': '1',
                        'left': left + 'px',
                        'top': top + 'px'
                    }).animate({
                        top: top - 10,
                        left: left + 10
                    }, 'slow', function () {
                        $(this).fadeIn('fast').remove();
                        var Num = parseInt($scoreNum.text());
                        Num += 1;
                        $scoreNum.text(Num);
                    });
                    return false;
                },
                error: function (r) {
                    try {
                        var rt = JSON.parse(r.responseText);
                        layer.alert(rt.message || '点赞失败!');
                    } catch (e) {
                        layer.alert("审核操作失败");
                    }
                }
            });
        } else {

        }
    }

</script>

<jsp:include page="../footer.jsp"/>
