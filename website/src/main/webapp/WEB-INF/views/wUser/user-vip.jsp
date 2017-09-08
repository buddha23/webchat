<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="finance">
            <div class="box clearfix">
                <div class="box-header">
                    <h1>购买会员</h1>
                </div>
                <div class="box-content">
                    <div class="vip-board">
                        <div class="vip-icon-xl"></div>
                        <div class="vip-info">
                            <h6>专属会员</h6>
                            <div>购买大牛数控专属会员，可享受多种特权</div>
                            <div>会员特价：<strong class="color-vermeil">100</strong> 元/ 年</div>
                            <div class="margintop10"><a class="button btn-orange"
                                                        href="${ctx}/userAccount/buyVip">立即购买</a></div>
                        </div>
                        <table class="table-style1 margintop20" width="0" border="0">
                            <thead>
                            <tr style="background: #f8f8f8; ">
                                <td width="16%" style="border: none"><strong>权益分类</strong></td>
                                <td width="51%" style="border: none"><strong>权益说明</strong></td>
                                <td width="14%" style="border: none"><strong>VIP会员</strong></td>
                                <td width="19%" style="border: none"><strong>普通用户</strong></td>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><strong>平台功能</strong></td>
                                <td>文档、软件上传<br>
                                    文档、软件下载<br>
                                    在线视频教学<br>
                                    数控社区
                                </td>
                                <td style="text-align: center;"><i class="fa fa-check f20 color-green"></i></td>
                                <td style="text-align: center;"><i class="el-icon-ok f20 color-a0c12d"><i
                                        class="fa fa-check f20 color-green"></i></i></td>
                            </tr>
                            <tr>
                                <td rowspan="2"><strong>特别权益</strong></td>

                            </tr>
                            <tr>
                                <td>文档下载，软件下载，视频教学享受5折优惠</td>
                                <td style="text-align: center;"><i class="fa fa-check f20 color-green"></i></td>
                                <td style="text-align: center;"><i class="fa fa-close f20 color-vermeil"></i></td>
                            </tr>
                            <tr>
                                <td rowspan="3"><strong>其他</strong></td>

                            </tr>
                            <tr>
                                <td>免费课件</td>
                                <td style="text-align: center;"><i class="fa fa-check f20 color-green"></i></td>
                                <td style="text-align: center;"><i class="fa fa-close f20 color-vermeil"></i></td>
                            </tr>
                            <tr>
                                <td>人才推荐</td>
                                <td style="text-align: center;"><i class="fa fa-check f20 color-green"></i></td>
                                <td style="text-align: center;"><i class="fa fa-close f20 color-vermeil"></i></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<jsp:include page="../footer.jsp"/>
