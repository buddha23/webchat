<%--<jsp:include page="../header.jsp"/>--%>

<%--<div class="content pd20 management">--%>
    <%--<div class="container clearfix">--%>
        <%--<jsp:include page="../left.jsp"/>--%>
        <%--<div class="main" data-menu="user">--%>
            <%--<div class="box clearfix" style="min-height:440px;">--%>
                <%--<div class="box-header">--%>
                    <%--<h1>积分历史</h1>--%>
                <%--</div>--%>
                <%--<div class="box-header">--%>
                    <%--<ul class="subnav clearfix">--%>
                        <%--<li style="margin-left: 30px;">--%>
                            <%--当前总积分：<span class="f16" style="color: red;">${userScore.totalScore}</span></li>--%>
                        <%--<li class="marginleft15">--%>
                            <%--总收入：<span class="f16" style="color: #ea3c1a;">${userScore.totalIn}</span></li>--%>
                        <%--<li class="marginleft15">--%>
                            <%--总支出：<span class="f16" style="color: #00ba30;">${userScore.totalOut}</span></li>--%>
                    <%--</ul>--%>
                <%--</div>--%>

                <%--<div class="box clearfix">--%>
                    <%--<div class="box-content">--%>
                        <%--<table class="table-style2 dyntable">--%>
                            <%--<thead>--%>
                            <%--<tr>--%>
                                <%--<th></th>--%>
                                <%--<th>积分变动</th>--%>
                                <%--<th>変动时间</th>--%>
                                <%--<th>增减途径</th>--%>
                                <%--&lt;%&ndash;<th>相关文档</th>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<th>说明</th>&ndash;%&gt;--%>
                            <%--</tr>--%>
                            <%--</thead>--%>
                            <%--<tbody>--%>
                            <%--<c:forEach items="${scoreHistories}" var="s">--%>
                                <%--<tr>--%>
                                    <%--<td></td>--%>
                                    <%--<td><c:if test="${s.scoreChange > 0}">+</c:if>${s.scoreChange}</td>--%>
                                    <%--<td><fmt:formatDate value="${s.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>--%>
                                    <%--<td>--%>
                                        <%--<c:if test="${!empty s.description}">${s.description}</c:if>--%>
                                        <%--<c:if test="${empty s.description}">--%>
                                            <%--<c:choose>--%>
                                                <%--<c:when test="${s.type eq 1}">上传文档</c:when>--%>
                                                <%--<c:when test="${s.type eq 2}">下载文档</c:when>--%>
                                                <%--<c:when test="${s.type eq 3}">文档被下载</c:when>--%>
                                                <%--<c:when test="${s.type eq 4}">注册加分</c:when>--%>
                                                <%--<c:when test="${s.type eq 5}">第三份绑定加分</c:when>--%>
                                                <%--<c:when test="${s.type eq 6}">购买积分</c:when>--%>
                                                <%--<c:when test="${s.type eq 7}">附赠积分</c:when>--%>
                                                <%--<c:when test="${s.type eq 8}">${s.description}</c:when>--%>
                                                <%--<c:when test="${s.type eq 9}">签到积分</c:when>--%>
                                            <%--</c:choose>--%>
                                        <%--</c:if>--%>
                                    <%--</td>--%>
                                <%--</tr>--%>
                            <%--</c:forEach>--%>
                            <%--</tbody>--%>
                        <%--</table>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="sd rightside">--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<script type="text/javascript" src="${assets}/js/jquery.dataTables.js"></script>--%>
<%--<%@include file="../footer.jsp" %>--%>