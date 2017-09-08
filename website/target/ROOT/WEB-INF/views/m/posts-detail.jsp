<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <title>大牛数控社区</title>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" href="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css" type="text/css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/layer.js"></script>
</head>

<body>
<div class="container">
    <header>
        <a id="prevPage" class="prev" href="javascript:history.back()">
            <i class="fa fa-chevron-left"></i>
        </a>

        <div class="menu"></div>

        <div class="title">${fn:escapeXml(category.name)}</div>
    </header>

    <div class="content clearfix">
        <div class="page-qa">
            <div class="qa-article">
                <div class="qa-item-profile">
                    <img class="avatar"
                         src="${bt:url(postsInfos.userAvatar, '/files/', '/assets/images/default_avatar.png')}" alt=""/>
                </div>
                <div class="qa-item-info">
                    <div class="qa-author">${fn:escapeXml(postsInfos.userNickname)}</div>
                    <div class="qa-meta">
                        <span><i class="icon-clock"></i>
                         <fmt:formatDate value="${postsInfos.createTime}" pattern="yyyy-MM-dd"/></span>
                        <span><i class="icon-eye"></i> ${postsInfos.views}</span>
                        <span><i class="icon-bubble"></i> ${comments.totalElements}</span>
                    </div>
                </div>

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

                <div class="qa-content"><p>${postsInfos.content}</p></div>

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
                                <img src="${bt:url(bestComment.userAvatar, '/files/', '/assets/images/default_avatar.png')}"
                                     alt="${fn:escapeXml(bestComment.userNickname)}">
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

                    <c:forEach items="${comments.content}" var="comment" varStatus="commentStatus">
                        <li class="clearfix">
                            <div class="comment-avatar">
                                <img class="avatar"
                                     src="${bt:url(comment.userAvatar, '/files/', '/assets/images/default_avatar.png')}"
                                     alt=""/>
                            </div>
                            <div class="comment-info">
                                <div class="comment-title clearfix">
                                    <span class="name">${fn:escapeXml(comment.userNickname)}</span>
                                    <c:if test="${sessionScope.user.id == postsInfos.userId && sessionScope.user.id != comment.userId && postsInfos.postsReward != null && postsInfos.postsReward.state == 1}">
                                        <div class="check-btn" data-commentId="${comment.id}"
                                             onclick="choseBest(${comment.id})"><i class="fa fa-thumbs-up"></i> 选为最佳
                                        </div>
                                    </c:if>
                                </div>
                                <div class="comment-msg">${fn:escapeXml(comment.content)}</div>
                                <div class="comment-meta">
                                    <c:if test="${comment.hasLike == null || comment.hasLike == false}">
                                        <div class="meta-item like nolike" data-id="${comment.id}">
                                            <span class="like${comment.id}">赞</span>(<span
                                                class="likeNum${comment.id}">${comment.likeNum}</span>)
                                        </div>
                                    </c:if>
                                    <c:if test="${comment.hasLike != null && comment.hasLike == true}">
                                        <div class="meta-item like">
                                            已赞(${comment.likeNum})<%--<div class="meta-item like">回复</div>--%>
                                        </div>
                                    </c:if>
                                    <span class="meta-item date">
                                        <fmt:formatDate value="${comment.createTime}" pattern="MM月dd日 HH:mm"/>
                                    </span>
                                </div>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <c:if test="${comments.totalElements<7}">
                <div class="comment-more nomore">
                    没有更多了
                </div>
            </c:if>
            <c:if test="${comments.totalElements>6}">
                <div class="comment-more getmore">
                    查看更多
                </div>
            </c:if>
            <div class="comment-toolbar">
                <div class="comment-btn" onclick="OBJ_mLoginBox.checkLogin(event,operate.publishComments)">回复</div>
            </div>
        </div>
    </div>

    <footer>
    </footer>

    <div class="page-mask" style="display: none;"></div>
</div>

<%--回复评论--%>
<div class="dialog" id="commentDiv">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">回复 <i class="fa fa-close close"></i></div>
            <div class="dialog-content">
                <form action="${ctx}/post/addComment" id="commentForm" method="post">
                    <div>
                        <textarea name="reply" class="ui-textarea-mobile" placeholder="我的回复"></textarea>
                    </div>

                    <div class="dialog-btns clearfix">
                        <div class="dialog-btns-col">
                            <button type="submit" class="btn-confirm">确定</button>
                        </div>
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-cancel">取消</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%@include file="login-div.jsp" %>

<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/mLoginBox.js"></script>
<script type="text/javascript" src="${assets}/js/mBindAccount.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.jcountdown.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.leoDialog.js"></script>

<script>
    var isLogin = false;
    var isBind = false;
    <c:if test="${!empty sessionScope.user}">
    isLogin = true;
    if (${sessionScope.user.loginType == null}) {
        isBind = true;
    } else if (${sessionScope.user.isBind == true}) {
        isBind = true;
    }
    </c:if>

    var operate = {
        publishComments: function () {
            if (!$('#commentDiv').is(':visible')) {
                $('#commentDiv').show();
                $('.page-mask').show();
                $('.page-mask').on('click', function () {
                    $('#commentDiv').hide();
                    $('.page-mask').hide();
                })
            } else {
                $('#commentDiv').hide();
                $('.page-mask').hide();
            }
        }
    };

    $('.nolike').click(function (e) {
        var commentId = $(this).attr('data-id');
//        console.log(commentId);
        e.stopPropagation();
        if (!isLogin) {
            loginEvents();
        } else {
            if (isBind) {
                doLike(commentId)
            } else {
                bindUrl = window.location.href;
                bindEvents();
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
                    $like.parents().attr("disabled", 'disable');
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
            })
        } else {
        }
    }

    $(function () {
        $('.dialog').click(function (event) {
            event.stopPropagation();
        });

        $('.dialog .close,.dialog .btn-cancel').click(function () {
            $('.dialog').hide();
            $('.page-mask').hide();
        })
    });


    $("#commentForm").submit(function (e) {
        var $form = $(this);
        e.preventDefault();
        var postsId = ${postsInfos.id};
        var comment = $("textarea[name='reply']").val();
        if (comment != '' && comment.length < 1000) {
            $.ajax({
                url: $form.attr('action'),
                type: $form.attr('method'),
                data: {reply: comment, postsId: postsId},
                success: function () {
                    $('#commentDiv').hide();
                    var ld2 = leoDialog.alert({
                        title: '提示',
                        modal: true,
                        content: '评论成功',
                        buttons: {
                            //确认按钮
                            confirm: {
                                text: '确定',
                                action: function () {
                                    location.reload();
                                }
                            }
                        }
                    });
                },
                error: function (r) {
                    try {
                        var result = JSON.parse(r.responseText);
                        layer.alert(result.message || '提交失败！');
                    } catch (e) {
                        layer.alert('提交失败！');
                    }
                }
            });
        } else {
            $('#commentDiv').hide();
            var ld2 = leoDialog.alert({
                title: '提示',
                modal: true,
                content: '评论内容不能为空且不大于1000字',
                buttons: {
                    //确认按钮
                    confirm: {
                        text: '确定',
                        action: function () {
                            leoDialog.closeAll();
                        }
                    }
                }
            });
        }
    });

    var page = 1;
    $('.getmore').click(function () {
        var i;
        page = page + 1;
        if (6 * page - 6 >${comments.totalElements}) {
            $('.getmore').html("没有更多了");
        } else {
            $.ajax({
                url: '${ctx}/m/posts/getMoreComments',
                type: 'get',
                data: {page: page, postsId:${postsInfos.id}},
                success: function (data) {
                    for (i in data) {
                        var comData = data[i];
                        var comTime = new Date(comData.createTime);
                        var time = longToDate(comTime);
                        var comAvatar = '/files/' + comData.userAvatar;
                        if (comData.userAvatar == null)
                            comAvatar = '/assets/images/default_avatar.png';
                        $('.comment-list').append(
                                "<li class='clearfix'>" +
                                "<div class='comment-avatar'>" + "<img class='avatar' src='" + comAvatar + "' alt=''/>" + "</div>" +
                                "<div class='comment-info'>" +
                                "<div class='comment-title clearfix'>" +
                                "<span class='name'>" + comData.userNickname + "</span>" +
                                "<span class='date'>" + time +
                                "</span>" +
                                "</div>" +
                                "<div class='comment-msg'>" + comData.content + "</div>" +
                                "</div>" +
                                "</li>"
                        );
                    }
                },
                error: function () {
                    layer.alert("查看更多失败");
                }
            })
        }
    });

    function longToDate(time) {
        var year = time.getFullYear();	   //年</span>
        var month = time.getMonth() + 1;  //月
        var day = time.getDate();		 //日
        var hh = time.getHours();	   //时
        var mm = time.getMinutes();	//分

        var str = year + "-";
        if (month < 10)
            str += "0";
        str = str + month + "-";
        if (day < 10)
            str += "0";
        str = str + day + " ";
        if (hh < 10)
            str += "0";
        str = str + hh + ":";
        if (mm < 10)
            str += "0";
        str = str + mm;
        return (str);
    }

    //QQ第三方登录
    $('#qqLogin').click(function () {
        window.location.href = "https://graph.qq.com/oauth2.0/authorize?client_id=${configer.qqAccountAppid}&response_type=code&state=${sessionScope.qqState}&scope=all&redirect_uri=" + location.href, "TencentLogin",
                "width=450,height=320,menubar=0,scrollbars=1,resizable = 1, status = 1, titlebar = 0, toolbar = 0, location = 1";
    });
    //微信第三方登录
    $(function () {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            $('.weixin').show();
            $('.weixin').click(function () {
                window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize' +
                        '?appid=${configer.wxAccountAppid}' +
                        '&redirect_uri=' + location.href + '&response_type=code' +
                        '&scope=snsapi_login&state=${sessionScope.weixinState}#wechat_redirect&connect_redirect=1';
            });
        } else {
            $('.weixin').hide();
            return false;
        }
    });

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
                    url: '${ctx}/post/choseBestComment',
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

    // 微信分享
    var title = '${fn:escapeXml(postsInfos.title)}';
    if (title == '') title = '大牛数控社区';
    var imgUrl = 'http://www.d6sk.com/files/${category.icon}';
    if (imgUrl == 'http://www.d6sk.com/files/') imgUrl = 'http://www.d6sk.com/assets/images/logo_posts.png';
    var desc = '${fn:escapeXml(category.description)}';
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
