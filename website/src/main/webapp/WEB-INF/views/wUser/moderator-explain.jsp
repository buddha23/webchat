<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="moderator">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>贡献值说明</h1>
                </div>
                <div class="box-content">
                    <div class="box-section">
                        <div class="section-content">
                            ${explain.content}
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
