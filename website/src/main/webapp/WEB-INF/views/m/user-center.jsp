<%--suppress ALL --%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.min.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/animate.delay.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title>用户中心</title>
    <base href="${ctx}/">
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()">
                    <i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">大牛数控</div>
        </div>
        <%--<div class="tabs clearfix">--%>
            <%--<a href="${ctx}/m/">首页</a>--%>
            <%--<a href="${ctx}/m/doc/category">文库</a>--%>
            <%--<a href="${ctx}/m/video/">课堂</a>--%>
            <%--<a href="${ctx}/m/posts/category">社区</a>--%>
        <%--</div>--%>
    </header>
    <div class="content bg-white clearfix">
        <div class="page">
            <c:if test="${empty sessionScope.user}">
                <div class="profile clearfix fadeIn animate0">
                    <div class="profile-background"></div>
                    <div class="profile-content">
                        <div class="profile-photo fadeInUp animate1">
                            <img src="${assets}/images/default_avatar.png"/>
                        </div>
                        <div class="profile-info fadeInUp animate1">
                            <div class="profile-title">游客</div>
                            <a class="btn-login" href="${ctx}/m/auth/login?backurl=${ctx}/m/#usercenter">登录</a>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${!empty sessionScope.user}">
                <div class="profile clearfix fadeIn animate0">
                    <div class="profile-background"></div>
                    <div class="profile-content">
                        <a class="profile-photo fadeInUp animate1" href="${ctx}/m/user/message">
                            <img src="${bt:url(user.avatar, 'files/', 'assets/images/default_avatar.png')}"/>
                            <div class="new-message" style="display: none;"></div>
                        </a>
                        <ul class="profile-info fadeInUp animate1">
                            <li class="profile-title">
                                <i class=""></i>
                                <c:out value="${user.nickname}">${user.name}</c:out>
                            </li>
                                <%--<li class="profile-account">${user.mobile}</li>--%>
                            <li class="profile-score">积分：
                                <span id="totalScore" class="color-red">
                                    <c:if test="${userPoint.totalPoint == null}">0</c:if>
                                        ${userPoint.totalPoint}</span></li>
                            <c:if test="${sessionScope.user.id != null}">
                                <li>
                                    <button id="userSignin" class="sign-btn" type="button">点击签到</button>
                                </li>
                            </c:if>
                        </ul>
                        <a class="profile-goto fadeInRight animate1" href="${ctx}/m/user/list"></a>
                    </div>
                </div>
                <div class="section">
                    <c:if test="${empty sessionScope.user.id}">
                        <div class="section tabs">
                            <ul class="section-tab tabs-nav clearfix">
                                <li class="current" style="width: 100%; height: 50px;">
                                    <center>
                                        <i class="fa fa-star"></i>尚未绑定网站账号,前往
                                        <a style="color: #17a5ff;" href="${ctx}/m/auth/register">注册</a> 或
                                        <a style="color: #17a5ff;" onclick="OBJ_mLoginBox.checkLogin(event,operate.myDownload)">绑定账号</a>
                                    </center>
                                </li>
                            </ul>
                        </div>
                    </c:if>
                    <c:if test="${!empty sessionScope.user.id}">
                        <div class="section tabs">
                            <ul class="section-tab tabs-nav clearfix">
                                <li class="tab-menu tabs-nav-item current"><i class="fa fa-star"></i> 我的收藏</li>
                                <li class="tab-menu tabs-nav-item"><i class="fa fa-download"></i> 我的下载</li>
                                    <%--<li class="tab-menu tabs-nav-item"><i class="fa fa-comment"></i> 我的提问</li>--%>
                                <li class="tab-menu tabs-nav-item"><i class="fa fa-upload"></i> 我的上传</li>
                            </ul>
                            <div class="section-content tabs-panel">
                                <div class="tab">
                                    <ul class="doc-favlist">
                                        <div id="favlist">
                                            <c:forEach items="${collects}" var="c">
                                                <li class="${c.fileType} clearfix" data-docid= ${c.id}>
                                                    <a class="doc-btn" href="javascript:;">
                                                        <div class="btn-unfav"><i class="fa fa-star-o"></i></div>
                                                        <div class="btn-txt">取消</div>
                                                    </a>
                                                    <a class="doc-content" href="${ctx}/m/doc/${c.id}">
                                                        <div class="doc-icon"></div>
                                                        <div class="doc-info">
                                                            <div class="doc-title">${c.title}</div>
                                                            <div class="doc-meta">
                                                                <span><bt:fileSize size="${c.fileSize}"/></span>
                                                                <span>上传：${fn:escapeXml(c.uploaderName)}</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </li>
                                            </c:forEach>
                                        </div>
                                        <c:if test="${collects.size()>0}">
                                            <a href="javascript:;" id="favA" onclick="getMoreFav()" class="more-btn clearfix text-center">加载更多</a>
                                        </c:if>
                                        <c:if test="${collects.size()== 0}">
                                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                                        </c:if>
                                    </ul>
                                </div>

                                <div class="tab">
                                    <ul class="doc-favlist">
                                        <div id="downlist">
                                            <c:forEach items="${downloads}" var="c">
                                                <li class="${c.fileType} clearfix">
                                                    <a class="doc-content" href="${ctx}/m/doc/${c.id}">
                                                        <div class="doc-icon"></div>
                                                        <div class="doc-info">
                                                            <div class="doc-title">${c.title}</div>
                                                            <div class="doc-meta">
                                                                <span><bt:fileSize size="${c.fileSize}"/></span>
                                                                <span>上传：${fn:escapeXml(c.uploaderName)}</span>
                                                            </div>
                                                        </div>
                                                    </a>
                                                </li>
                                            </c:forEach>
                                        </div>
                                        <c:if test="${downloads.size()>0}">
                                            <a href="javascript:;" id="downA" onclick="getMoreDowns()" class="more-btn clearfix text-center">加载更多</a>
                                        </c:if>
                                        <c:if test="${downloads.size()==0}">
                                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                                        </c:if>
                                    </ul>
                                </div>

                                <div class="tab">
                                    <ul class="doc-list">
                                        <div class="doc-toolbar" >
                                            <a class="upload-btn" href="${ctx}/m/doc/upload"><i class="fa fa-plus"></i> 上传文档</a>
                                        </div>
                                        <div id="uplist">
                                            <c:forEach items="${uploads}" var="c">
                                                <a class="${c.fileType} clearfix" href="${ctx}/m/doc/${c.id}">
                                                    <div class="doc-icon ${c.fileType}"></div>
                                                    <div class="doc-content">
                                                        <div class="doc-title">${fn:escapeXml(c.title)}</div>
                                                        <div class="doc-meta">
                                                            <span><bt:fileSize size="${c.fileSize}"/></span>
                                                            <span>阅读：${c.views}</span>
                                                        </div>
                                                    </div>
                                                </a>
                                            </c:forEach>
                                        </div>
                                        <c:if test="${uploads.size()>0}">
                                            <a href="javascript:;" id="upA" onclick="getMoreUploads()" class="more-btn clearfix text-center">加载更多</a>
                                        </c:if>
                                        <c:if test="${uploads.size()==0}">
                                            <a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>
                                        </c:if>
                                    </ul>
                                </div>

                                    <%--<div class="tab">--%>
                                    <%--<div class="qa-list">--%>
                                    <%--<div id="qalist">--%>
                                    <%--<c:forEach items="${posts.content}" var="p">--%>
                                    <%--<a class="qa-list-item" href="${ctx}/m/posts/${p.id}">--%>
                                    <%--<div class="qa-item-profile">--%>
                                    <%--<img class="avatar"--%>
                                    <%--src="${bt:url(p.userAvatar,'/files/', '/assets/images/default_avatar.png')}"--%>
                                    <%--alt=""/>--%>
                                    <%--</div>--%>
                                    <%--<div class="qa-item-info">--%>
                                    <%--<div class="qa-title">${fn:escapeXml(p.title)}</div>--%>
                                    <%--<div class="qa-meta">--%>
                                    <%--<span><i class="icon-calendar"></i>--%>
                                    <%--<fmt:formatDate value="${p.createTime}"--%>
                                    <%--pattern="yyyy-MM-dd HH:mm"/>--%>
                                    <%--</span>--%>
                                    <%--<span><i class="icon-eye"></i> ${p.views}</span>--%>
                                    <%--<span><i class="icon-bubble"></i> ${p.commentsNum}</span>--%>
                                    <%--</div>--%>
                                    <%--</div>--%>
                                    <%--</a>--%>
                                    <%--</c:forEach>--%>
                                    <%--</div>--%>
                                    <%--<c:if test="${posts.totalElements>0}">--%>
                                    <%--<a id="postsA" onclick="getMorePosts()"--%>
                                    <%--class="more-btn clearfix text-center" href="javascript:;">加载更多</a>--%>
                                    <%--</c:if>--%>
                                    <%--<c:if test="${posts.totalElements==0}">--%>
                                    <%--<a href="javascript:;" class="more-btn clearfix text-center">没有更多了</a>--%>
                                    <%--</c:if>--%>
                                    <%--</div>--%>
                                    <%--</div>--%>
                            </div>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
    <footer></footer>
    <div class="page-mask" style="display: none;"></div>
</div>

<div class="container-user">
    <div class="userbar">
        <div class="userbar-menu">
            <a class="item home" href="${ctx}/m/#home">
                <div class="item-icon"></div>
                <div class="item-name">首页</div>
            </a>
            <a class="item publish" href="${ctx}/m/#publish">
                <div class="item-icon"></div>
                <div class="item-name">发布</div>
            </a>
            <a class="item usercenter act" href="${ctx}/m#usercenter">
                <div class="item-icon"></div>
                <div class="item-name">我</div>
            </a>
        </div>
    </div>
</div>

<%--绑定--%>
<div class="dialog" id="dialogBind">
    <div class="container">
        <div class="dialog-box">
            <div class="dialog-header">账号绑定 <i class="fa fa-close close"></i></div>
            <div class="dialog-content">
                <form action="#" method="post">
                    <ul class="dialog-group">
                        <c:if test="${sessionScope!=null&&sessionScope.user!=null}">
                            <input id="type" value="${sessionScope.user.loginType}" hidden/>
                            <input id="openid" value="${sessionScope.user.openid}" hidden/>
                        </c:if>
                        <li class="clearfix">
                            <label>手机号</label>
                            <input id="mobileBind" class="dialog-input" type="text" name="mobile"
                                   onkeyup="value=value.replace(/[^0-9_]/g,'')" maxlength="11"
                                   onchange="OBJ_formcheck.checkFormatM('mobileBind','bindMgs','phone','手机号码格式不对');">
                        </li>
                        <li class="clearfix">
                            <label>验证码</label>
                            <button class="captcha-btn" type="button" id="bind_sendmsg_button"
                                    onclick="OBJ_mBindAccount.sendMsg('mobileBind');">获取验证码
                            </button>
                            <button class="captcha-btn" id="bind_msg_end" type="button" style="display:none;"><span
                                    id="bind_sendmsg_time_text">60</span>秒后重发
                            </button>
                            <input class="dialog-input captcha" id="code" type="text"
                                   onchange="return OBJ_formcheck.checkFormatM('code','bindMgs','code','请输入有效的验证码')"
                                   onkeyup="value=value.replace(/[^0-9]/g,'')" maxlength="6">
                        </li>
                        <li class="clearfix">
                            <label>密&nbsp;&nbsp;&nbsp;码</label>
                            <input class="dialog-input" type="password" id="password"
                                   onchange="return OBJ_formcheck.checkFormatM('password','bindMgs','password','密码为空或不符合规则');"
                                   onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" placeholder="请输入6-20位有效密码">
                        </li>
                        <li class="clearfix" id="bindMgs" hidden>
                            <label>提示</label>
                            <input class="dialog-input" id="bindMgs_tips" type="text" value="">
                        </li>
                    </ul>

                    <div class="dialog-btns clearfix">
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-confirm" onclick="OBJ_mBindAccount.bind()">绑定</button>
                        </div>
                        <div class="dialog-btns-col">
                            <button type="button" class="btn-cancel" id="bindcancel">取消</button>
                        </div>
                    </div>

                    <a class="dialog-tips" href="javascript:;">
                        <i class="fa fa-exclamation-circle"></i> 您需要绑定建站内账号，才能够进行该操作
                    </a>
                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/layer.js"></script>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script type="text/javascript" src="${assets}/js/mLoginBox.js"></script>
<script type="text/javascript" src="${assets}/js/mBindAccount.js"></script>
<script type="text/javascript">
    var ctx = '${ctx}';
    function fileSize(size) {
        if (size < 1024) {
            return size + "KB";
        } else if (size < 1024 * 1024) {
            return (size / 1024).toFixed(2) + 'MB';
        } else {
            return (size / (1024 * 1024)).toFixed(2) + 'GB';
        }
    }

    //绑定组
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
        myDownload: function () {
            location.reload();
        }
    }
    //    取消收藏
    $('body').on('click', '.doc-btn', function () {
        var $this = $(this);
        var docid = $this.parent().attr('data-docid');
        $.ajax({
            url: ctx + '/doc/uncollect?id=' + docid,
            type: 'get',
            success: function () {
                $this.parent().remove();
            },
            error: function (r) {
                console.log(r);
            }
        });
    });
    //用户积分,是否签到
    $(function () {
        if (isBind) {
            $.get(ctx + '/user/infos', function (data) {
                if (data) {
                    $('#totalScore').html(data['point']['totalPoint']);
                    if (data['isSigns'])
                        $('.profile .sign-btn').text('已签到').attr("disabled", 'disable');
                    if (data['messages']) {
                        $('.new-message').html(data['messages']).show();
                    }
                    if(data['isMember']) {
                        $('.profile-title').children('i').attr('class','vip-icon-hd');
                    }
                } else {
                    $('#totalScore').html(0);
                }
            });
        }

    });
    //签到
    $("#userSignin").click(function () {
        $.ajax({
            url: '${ctx}/user/signin',
            type: 'get',
            success: function (data) {
                if (data == 'OK') {
                    $('.profile .sign-btn').text('已签到').attr("disabled", 'disable');
                    var $scoreNum = $('#totalScore');
                    var left = parseInt($scoreNum.position().left) + 10,
                            top = parseInt($scoreNum.position().top) - 10;
                    $('.profile-score').append('<div class="sign-score color-red"><b>+' + 10 + '<\/b></\div>');
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
                        Num += 10;
                        $scoreNum.text(Num);
                    });
                    return false;
                }
                else {
                    alert(data.message);
//                    $('.profile .sign-btn').text('已签到').attr("disabled", 'disable');
                }
            },
            error: function (r) {
                layer.alert(JSON.parse(r.responseText).message || '签到失败');
            }
        })
    });
    var favpage = 1, downpage = 1, postspage = 1, uppage = 1;
    //更多收藏
    function getMoreFav() {
        var i;
        favpage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'collects', page: favpage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        $("#favlist").append(
                                "<li class='" + docData.fileType + " clearfix' data-docid= ''>" +
                                "<a class='doc-btn' href='javascript:;'>" +
                                "<div class='btn-unfav'><i class='fa fa-star-o'></i></div>" +
                                "<div class='btn-txt'>取消</div>" +
                                "</a>" +
                                "<a class='doc-content' href='" + ctx + "/m/doc/" + docData.id + "'>" +
                                "<div class='doc-icon'></div>" +
                                "<div class='doc-info'>" +
                                "<div class='doc-title'>" + docData.title + "</div>" +
                                "<div class='doc-meta'>" +
                                "<span>" + fileSize(docData.fileSize) + "</span>" +
                                "<span>上传：" + docData.uploaderName + "</span>" +
                                "</div></div></a></li>"
                        )
                    }
                } else {
                    $("#favA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //更多下载
    function getMoreDowns() {
        var i;
        downpage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'downloads', page: downpage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        $("#downlist").append(
                                "<li class='" + docData.fileType + " clearfix' data-docid= ''>" +
                                "<a class='doc-content' href='" + ctx + "/m/doc/" + docData.id + "'>" +
                                "<div class='doc-icon'></div>" +
                                "<div class='doc-info'>" +
                                "<div class='doc-title'>" + docData.title + "</div>" +
                                "<div class='doc-meta'>" +
                                "<span>" + fileSize(docData.fileSize) + "</span>" +
                                "<span>上传：" + docData.uploaderName + "</span>" +
                                "</div></div></a></li>"
                        )
                    }
                } else {
                    $("#downA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //更多上传
    function getMoreUploads() {
        var i;
        uppage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'uploads', page: uppage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        $("#uplist").append(
                                "<li class='" + docData.fileType + " clearfix' data-docid= ''>" +
                                "<a class='doc-content' href='" + ctx + "/m/doc/" + docData.id + "'>" +
                                "<div class='doc-icon'></div>" +
                                "<div class='doc-info'>" +
                                "<div class='doc-title'>" + docData.title + "</div>" +
                                "<div class='doc-meta'>" +
                                "<span>" + fileSize(docData.fileSize) + "</span>" +
                                "<span>阅读：" + docData.views + "</span>" +
                                "</div></div></a></li>"
                        )
                    }
                } else {
                    $("#upA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //更多提问
    function getMorePosts() {
        var i;
        postspage += 1;
        var page;
        $.ajax({
            url: ctx + '/m/user/getmore',
            data: {getType: 'posts', page: postspage},
            success: function (getData) {
                if (getData.length > 0) {
                    for (i in getData) {
                        var docData = getData[i];
                        var postsAvatar = '/files/' + docData.userAvatar;
                        if (docData.userAvatar == null)
                            postsAvatar = '/assets/images/default_avatar.png';
                        $("#qalist").append(
                                "<a class='qa-list-item' href='" + ctx + "/m/posts/" + docData.id + "'>" +
                                "<div class='qa-item-profile'> " +
                                "<img class='avatar' src='" + postsAvatar + "' alt=''/></div>" +
                                "<div class='qa-item-info'> " +
                                "<div class='qa-title'> " + docData.title + "</div>" +
                                "<div class='qa-meta'><span><i class='icon-calendar'></i> " + longToDate(new Date(docData.createTime)) + "</span>" +
                                " <span><i class='icon-eye'></i> " + docData.views + "</span>" +
                                " <span><i class='icon-bubble'></i> " + docData.commentsNum +
                                "</span></div></div></a>"
                        )
                    }
                } else {
                    $("#postsA").text("没有更多了").removeAttr("onclick");
                }
            }
        })
    }
    //时间格式转换
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
</script>

</body>
</html>
