<jsp:include page="../header.jsp">
    <jsp:param name="title" value="问题反馈"/>
    <jsp:param name="css" value="plugins/froalaEditor/css/froala_editor.pkgd.min.css"/>
</jsp:include>

<div class="content bg-white pd20">
    <div class="container">
        <div class="feedback">
            <div class="feedback-header">感谢您的意见反馈，我们会努力进步！</div>
            <ul class="feedback-tab margintop10 clearfix">
                <a class="current" href="${ctx}/user/mySuggests">我的反馈</a>
                <a href="${ctx}/userSuggests">看看别人的反馈</a>
            </ul>
            <form action="${ctx}/user/userSuggest" method="post" id="suggestform">
                <div class="margintop15">问题与建议描述:</div>
                <div class="margintop10">
                    <textarea class="input-full ui-input-default" id='edit' name="content"></textarea>
                </div>
                <div class="margintop15">
                    <button class="button btn-primary" type="button" onclick="OBJ_loginBox.checkLogin(operate.formSubmit)">发表</button>
                    <button id="dialog-close" class="button btn-light marginleft5">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>

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
    var operate = {
        formSubmit: function () {
            $('#suggestform').submit();
        }
    }
    $(function () {
        <%--$('#edit').froalaEditor({--%>
        <%--height: 300,--%>
        <%--language: "zh_cn"--%>
        <%--//imageUploadURL: "${ctx}/upload/uploadImage"--%>
        <%--});--%>
        $('#edit').froalaEditor({
            height: 300,
            language: "zh_cn",
            imageUploadParam: 'file',
            imageUploadParams: {id: 'my_editor'},
            imageUploadURL: "${ctx}/post/upload_image",
            imageMaxSize: 5 * 1024 * 1024,
            imageAllowedTypes: ['jpeg', 'jpg', 'png', 'gif']
        }).on('froalaEditor.image.error', function (e, editor, error, response) {
            console.log(e);
            console.log(error);
        });

        $('#suggestform').submit(function (e) {
            var $form = $(this);
            e.preventDefault();
            var content = $('#edit').froalaEditor('html.get');
            if (content) {
                $.ajax({
                    url: $form.attr('action'),
                    type: $form.attr('method'),
                    data: {content: content},
                    success: function (data) {
                        location.href = '${ctx}/user/mySuggests';
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
                layer.alert('提问内容不能为空哦');
            }
            return false;
        });
    })
</script>

<jsp:include page="../footer.jsp"/>