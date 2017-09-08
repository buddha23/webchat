<div class="sd leftside">
    <div class="sd leftside">
        <div class="menu-header"><i class="fa fa-navicon"></i> 菜单</div>
        <div class="profile clearfix">
            <div class="profile-top">
                <div class="profile-info clearfix">
                    <div class="profile-avatar">
                        <img src="${bt:url(sessionScope.user.avatar, 'files/', 'assets/images/default_avatar.png')}" alt="${fn:escapeXml(sessionScope.user.nickname)}">
                    </div>
                    <div class="profile-meta">
                        <div class="profile-name"><i class=""></i>
                        ${bt:out(sessionScope.user.nickname, sessionScope.user.name)}</div>
                        <div class="">我的牛人币：<span class="score">0</span></div>
                    </div>
                </div>
            </div>
        </div>

        <ul class="menu">
            <li class="menu-user">
                <a href="javascript:void(0);"><i class="icon-user"></i> 个人管理</a>
                <ul class="menu-sub">
                    <li><a href="${ctx}/user/center" onclick="return OBJ_bindAccount.checkBind(this)">个人中心</a></li>
                    <li><a href="${ctx}/user/profile" onclick="return OBJ_bindAccount.checkBind(this)">资料编辑</a></li>
                    <li><a href="${ctx}/user/accountManage">账号管理</a></li>
                    <c:if test="${sessionScope.user.mobile != null}"><li><a href="${ctx}/user/pwd" onclick="return OBJ_bindAccount.checkBind(this)">修改密码</a></li></c:if>

                </ul>
            </li>
            <li class="menu-enterprise">
              <a href="javascript:void(0);"><i class="icon-home"></i> 企业管理</a>
              <ul class="menu-sub">
              <li><a href="${ctx}/enterprise/settled" onclick="return OBJ_bindAccount.checkBind(this)">入驻资料</a></li>
              <li><a href="${ctx}/enterprise/info" onclick="return OBJ_bindAccount.checkBind(this)">企业信息</a></li>
              </ul>
            </li>
            <li class="menu-doc">
                <a href="javascript:void(0);"><i class="icon-book-open"></i> 文档管理</a>
                <ul class="menu-sub">
                    <li><a href="${ctx}/doc/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">我的上传</a></li>
                    <li><a href="${ctx}/doc/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">我的下载</a></li>
                    <li><a href="${ctx}/doc/myFav" onclick="return OBJ_bindAccount.checkBind(this)">我的收藏</a></li>
                </ul>
            </li>
            <li class="menu-file">
                <a href="javascript:void(0);"><i class="icon-drawer"></i> 软件管理</a>
                <ul class="menu-sub">
                   <li><a href="${ctx}/soft/myUpload" onclick="return OBJ_bindAccount.checkBind(this)">我的上传</a></li>
                   <li><a href="${ctx}/soft/myDownload" onclick="return OBJ_bindAccount.checkBind(this)">我的下载</a></li>
                   <li><a href="${ctx}/soft/myFav" onclick="return OBJ_bindAccount.checkBind(this)">我的收藏</a></li>
                </ul>
            </li>
            <c:if test="${sessionScope.user.mobile != null}">
                <li class="menu-moderator">
                    <a href="javascript:void(0);"><i class="icon-trophy"></i> 版主管理</a>
                    <ul class="menu-sub">
                        <li><a href="${ctx}/user/moderators" onclick="return OBJ_bindAccount.checkBind(this)">版块管理</a></li>
                        <li><a href="${ctx}/user/moderators/score">贡献值历史</a></li>
                        <li><a href="${ctx}/user/moderators/duty">版主职责</a></li>
                        <li><a href="${ctx}/user/moderators/explain">版主贡献值说明</a></li>
                    </ul>
                </li>
            </c:if>
            <li class="menu-finance">
                <a href="javascript:void(0);"><i class="icon-credit-card"></i> 财务管理</a>
                <ul class="menu-sub">
                    <li><a href="${ctx}/userAccount/vip">购买会员</a></li>
                    <%--<li><a href="${ctx}/withdrawals/list">充值 & 提现</a></li>--%>
                    <li><a href="${ctx}/user/myscore" onclick="return OBJ_bindAccount.checkBind(this)">充值 & 提现</a></li>
                    <li><a href="${ctx}/finance/accountList">提现账户管理</a></li>
                </ul>
            </li>
            <li class="">
                <a href="${ctx}/auth/logout"><i class="icon-logout"></i> 退出登录</a>
            </li>
        </ul>
    </div>
</div>

