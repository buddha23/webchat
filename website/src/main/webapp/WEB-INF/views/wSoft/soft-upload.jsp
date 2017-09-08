<jsp:include page="../header.jsp"/>
<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="doc">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="${ctx}/soft/myUpload">软件管理</a> <i class="fa fa-angle-right breadcrumbs"></i> 文件上传</h1>
                </div>
                <div class="box-content">
                    <div class="doc-progress">
                        <div class="upload-progress">
                            <div class="progress-value"></div>
                            <div class="progress-bar">
                                <div class="progress green"></div>
                            </div>
                        </div>
                        <div class="doc-upload-tips">
                            文件上传中，请耐心等待...
                        </div>
                    </div>

                    <form style="display:none;" class="page-form doc-form margintop15" action="#" method="post">
                        <div class="row">
                            <label for=""></label>
                            <div class="field">
                                <i class="fa fa-check-circle color-blue"></i> 文档上传成功，请完善文档信息！
                            </div>
                        </div>
                        <div class="row">
                            <label for="">标题：</label>
                            <div class="field">
                                <input class="ui-input-default input-full" type="text"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">牛人币售价：</label>
                            <div class="field">
                                <input class="ui-input-default input-full" type="text"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">上传到分类：</label>
                            <div class="field">
                                <select class="ui-input-default input-full">
                                    <option>西门子</option>
                                    <option>发那科</option>
                                    <option>三菱数控</option>
                                    <option>山崎马扎克</option>
                                    <option>北京精雕</option>
                                    <option>新代数控</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">简介：</label>
                            <div class="field">
                                <textarea class="ui-textarea-default input-full"></textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label></label>
                            <div class="field">
                                <button class="button btn-green btn-large" type="submit">确定</button>
                            </div>
                        </div>
                    </form>
                    <%--<form style="" class="page-form doc-form margintop15" action="#" method="post">--%>
                    <div class="row">
                        <label for="">标题：</label>.

                        <div class="field">
                            <input class="ui-input-default input-full" type="text" id="title" name="title"
                                   maxlength="80" value="${fn:escapeXml(param.title)}" required/>
                        </div>
                    </div>
                    <div class="row margintop10">
                        <label for="">下载牛人币：</label>

                        <div class="field">
                            <input class="ui-input-default input-full" type="number" name="costScore" value="0"
                                   id="costScore" maxlength="3" min="0" max="999" style="ime-mode:disabled"
                                   onkeypress="return event.keyCode>=48&&event.keyCode<=57"
                                   onpaste="return !clipboardData.getData('text').match(/\d/)"
                                   ondragenter="return false"/>
                        </div>
                    </div>
                    <div class="row margintop10">
                        <label for="cg1">上传到分类：</label>

                        <div class="field">
                            <select id="cg1" class="ui-input-default"></select>
                            <label for="cg2">-</label>
                            <select id="cg2" class="ui-input-default" name="categoryId"></select>
                        </div>
                    </div>
                    <div id="container" class="row margintop10">
                        <label for="">附件：</label>
                        <div id="ossfile"></div>
                        <div class="field">
                            <a class="button btn-primary" id="selectfiles" href="javascript:void(0);">
                                <i class="fa fa-upload"></i> 选择文件</a>
                        </div>
                    </div>
                    <div class="row margintop10">
                        <label for="">描述：</label>
                        <div class="field">
                            <textarea class="ui-textarea-default input-full" id="description"></textarea>
                        </div>
                    </div>
                    <div class="row margintop10">
                        <label for="">文件说明：</label>
                        <div class="field">
                            <textarea class="ui-textarea-default input-full" id='specification'
                                      name="content"></textarea>
                        </div>
                    </div>
                    <div class="row margintop10">
                        <label></label>
                        <div class="field">
                            <a id="postfiles" href="javascript:void(0);" class="button btn-primary">开始上传</a>
                        </div>
                    </div>
                    <%--</form>--%>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<link rel="stylesheet" href="${assets}/plugins/froalaEditor/css/froala_editor.pkgd.min.css" type="text/css">
<script type="text/javascript" src="${assets}/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript" src="${assets}/js/crypto1/crypto/crypto.js"></script>
<script type="text/javascript" src="${assets}/js/crypto1/hmac/hmac.js"></script>
<script type="text/javascript" src="${assets}/js/crypto1/sha1/sha1.js"></script>
<script type="text/javascript" src="${assets}/js/base64.js"></script>
<script type="text/javascript" src="${assets}/js/plupload-2.1.2/js/plupload.full.min.js"></script>
<script type="text/javascript" src="${assets}/js/upload.js"></script>
<!-- froala editor -->
<script src="${assets}/plugins/froalaEditor/js/froala_editor.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/languages/zh_cn.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/align.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/colors.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/draggable.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/code_view.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/code_beautifier.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/font_size.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/font_family.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/fullscreen.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/image.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/image_manager.min.js"></script>
<script src="${assets}/plugins/froalaEditor/js/plugins/table.min.js"></script>
<script type="text/javascript">
    $(function () {
        $('#specification').froalaEditor({
            height: 300,
            language: "zh_cn",
            imageUploadParam: 'file',
            imageUploadParams: {id: 'my_editor'},
            imageUploadURL: "${ctx}/post/upload_image",
            imageMaxSize: 5 * 1024 * 1024,
            imageAllowedTypes: ['jpeg', 'jpg', 'png', 'gif']
        });
    })
</script>
<jsp:include page="../footer.jsp"/>