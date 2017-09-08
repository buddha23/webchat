<jsp:include page="../header.jsp"/>
<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="doc">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="doc/myUpload">我的上传</a> <i class="fa fa-angle-right breadcrumbs"></i> 文档编辑</h1>
                </div>
                <div class="box-content">
                    <div class="doc-upload-edit clearfix">
                        <div class="wrapper">
                            <div class="doc-title pdf">${fn:escapeXml(doc.fileName)}</div>
                        </div>
                        <div class="doc-upload-btn">
                            <label for="file" style="display: block; width: 100%; height: 100%; cursor:pointer;">
                                <i class="fa fa-upload"></i> 重新上传
                            </label>
                        </div>
                        <input id="file" class="doc-file" style="display: none;" type="file">
                    </div>

                    <div class="dialog-progress">
                        <div class="upload-progress">
                            <div class="progress-value"></div>
                            <div class="progress-bar">
                                <div class="progress green"></div>
                            </div>
                        </div>
                        <div class="dialog-upload-tips">
                            文件上传中，请耐心等待...
                        </div>
                    </div>

                    <form id="docForm" class="page-form doc-form margintop15" action="${ctx}/doc/${doc.id}" method="post">
                        <div class="row">
                            <label for="">文档标题：</label>

                            <div class="field">
                                <input class="ui-input-default input-full" type="text" id="title" name="title"
                                       maxlength="80" value="${fn:escapeXml(doc.title)}" required/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">下载积分：</label>

                            <div class="field">
                                <input class="ui-input-default input-full" type="number" name="costScore"
                                       value="${doc.costScore}"
                                       maxlength="3" min="0" max="999" style="ime-mode:disabled"
                                       onkeypress="return event.keyCode>=48&&event.keyCode<=57"
                                       onpaste="return !clipboardData.getData('text').match(/\d/)"
                                       ondragenter="return false"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">上传到分类：</label>

                            <div class="field">
                                <select id="cg1" class="ui-input-default"></select>
                                <label for="cg2">-</label>
                                <select id="cg2" class="ui-input-default" name="categoryId"></select>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">简介：</label>

                            <div class="field">
                                <textarea class="ui-textarea-default input-full" name="summary" placeholder="不超过500个字符"
                                          maxlength="500">${fn:escapeXml(doc.summary)}</textarea>
                            </div>
                        </div>
                        <div class="row">
                            <label></label>

                            <div class="field">
                                <button class="button btn-green btn-large" type="submit">确定</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="sd rightside">

        </div>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script>
    $(function () {
        var categories = [];
        var c1 = 0, c2 = 0;
        <c:if test="${!empty doc.docCategory}">
        c1 = ${doc.docCategory.fid};
        c2 = ${doc.docCategory.id};
        </c:if>

        var setCg2Options = function (cg1) {
            $('#cg2').empty();
            if (cg1.children) {
                cg1.children.forEach(function (c) {
                    $("#cg2").append('<option value="' + c.id + '">' + c.name + '</option>');
                });
            }
        };

        $('#cg1').change(function () {
            var val = parseInt($(this).val());
            categories.forEach(function (c) {
                if (c.id === val) {
                    setCg2Options(c);
                    return false;
                }
            });
        });

        $.get(ctx + '/i/doc_categories', function (data) {
            categories = data;
            data.forEach(function (c) {
                if (c.children && c.children.length > 0)
                    $("#cg1").append('<option value="' + c.id + '">' + c.name + '</option>');
            });
            if(c1 && c2) {
                $('#cg1').val(c1).change();
                $('#cg2').val(c2);
            } else if (data[0]) {
                setCg2Options(data[0]);
            }
        });

        function setProgress(p) {
            var v = parseInt(p.loaded / p.total * 100);

            $('.upload-progress .progress-bar .progress').css({'width': v + '%'});
            $('.upload-progress .progress-value').text(v + '%');
        }

        $('#file').change(function () {
            var file = $(this).get(0).files[0];
            if (!file) {
                return false;
            }
            if (file.size > 1024 * 1024 * 100) {
                layer.alert('文件不能超过100M！');
            }

            var id = layer.open({
                type: 1,
                skin: 'layui-layer-demo', //样式类名
                closeBtn: 0, //不显示关闭按钮
                shift: 2,
                shadeClose: false, //开启遮罩关闭
                content: $('.dialog-progress'),
                area: ['auto', 'auto'],
                success: function () {
                    var form = new FormData();
                    form.append('file', file);
                    form.append('id', ${doc.id});

                    $.ajax({
                        url: ctx + '/doc/upload',
                        type: 'POST',
                        data: form,
                        processData: false,
                        contentType: false,
                        xhr: function () {
                            var xhr = $.ajaxSettings.xhr();
                            xhr.upload.addEventListener('progress', setProgress, false);
                            return xhr;
                        },
                        success: function (data) {
                            $('.doc-progress .doc-upload-tips').text('上传成功！');
                            setTimeout(function () {
                                location.reload();
                            }, 750);
                        },
                        error: function (r) {
                            console.log(r);
                            try {
                                var result = JSON.parse(r['responseText']);
                                $('.dialog-upload-tips').html('上传失败：' + (result.message || '') + '，请重新<a href="${ctx}/doc/edit/${doc.id}">上传</a>');
                            } catch (e) {
                                $('.dialog-upload-tips').html('上传失败,请重新<a href="${ctx}/doc/edit/${doc.id}">上传</a>');
                            }
                        }
                    });
                }
            });

        });

        $('#docForm').submit(function (e) {
            e.preventDefault();
            $(this).ajaxSubmit({
                success: function (data) {
                    layer.msg('修改完成', {
                        icon: 1,
                        time: 1000
                    }, function(){
                        location.href = ctx + '/doc/myUpload';
                    });
                },
                error: function (r) {
                    console.log(r);
                    layer.alert('保存失败！');
                }
            });
            return false;
        });
    })
</script>
<jsp:include page="../footer.jsp"/>