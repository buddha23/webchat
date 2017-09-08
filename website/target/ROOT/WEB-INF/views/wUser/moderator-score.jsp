<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="moderator">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>我的贡献值</h1>
                </div>
                <div class="box-content">
                    <div class="box-section">
                        <div class="score-box clearfix">
                            <div class="score-count">
                                <div class="">我的贡献值：</div>
                                <div><span class="" style="color: #ea3c1a; font-size: 22px;">${bt:out(moderatorScore.moderatorScore, 0)}</span></div>
                            </div>
                        </div>
                        <div class="section-header margintop20">贡献值历史</div>
                        <div class="section-content">
                            <c:if test="${!isModerator}">
                                您还不是版主,暂无数据。
                            </c:if>
                            <c:if test="${isModerator}">
                                <table class="table-style2 dyntable">
                                    <thead>
                                    <tr>
                                        <th>时间</th>
                                        <th>贡献值变化</th>
                                        <th>详情</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${monthScores}" var="s">
                                        <tr>
                                            <td><fmt:formatDate value="${s.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                                            <td>
                                                <c:if test="${s.scoreChange > 0}">
                                                    <span class="color-green">+${s.scoreChange}</span></c:if>
                                                <c:if test="${s.scoreChange < 0}">
                                                    <span class="color-vermeil">${s.scoreChange}</span></c:if>
                                            </td>
                                            <td>${fn:escapeXml(s.description)}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>
<jsp:include page="../footer.jsp"/>
