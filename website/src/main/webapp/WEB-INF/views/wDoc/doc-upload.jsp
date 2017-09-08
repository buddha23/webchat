<jsp:include page="../header.jsp"/>

<div class="content management pd20">
    <div class="container clearfix">
        <jsp:include page="../left.jsp"/>

        <div class="main" data-menu="doc">
            <div class="box clearfix">
                <div class="box-header">
                    <h1><a href="doc/myUpload">文档管理</a> <i class="fa fa-angle-right breadcrumbs"></i> 文档上传</h1>
                </div>
                <div class="box-content">
                    <div class="doc-upload">
                        <div class="doc-upload-btn">
                            <label for="file" style="display: block; width: 100%; height: 100%; cursor:pointer;">
                                <i class="fa fa-upload"></i> 上传文档
                            </label>
                        </div>
                        <div class="doc-upload-tips">
                            从我的电脑选择要上传的文档，通过审核后便可供他人下载(最大100M)
                        </div>
                        <input id="file" name="file" class="doc-file" style="display: none;" type="file">
                    </div>

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

                    <form id="docForm" style="display:none;" class="page-form doc-form margintop15" action=""
                          method="post">
                        <div class="row">
                            <label for=""></label>

                            <div class="field">
                                <i class="fa fa-check-circle color-blue"></i> 文档上传成功，请完善文档信息！
                            </div>
                        </div>
                        <div class="row">
                            <label for="">标题：</label>

                            <div class="field">
                                <input class="ui-input-default input-full" type="text" id="title" name="title"
                                       maxlength="80" value="${fn:escapeXml(param.title)}" required/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="">下载花费(牛人币)：</label>

                            <div class="field">
                                <input class="ui-input-default input-full" type="number" name="costScore" value="0"
                                       maxlength="3"
                                       min="0" max="999" style="ime-mode:disabled"
                                       onkeypress="return event.keyCode>=48&&event.keyCode<=57"
                                       onpaste="return !clipboardData.getData('text').match(/\d/)"
                                       ondragenter="return false"/>
                            </div>
                        </div>
                        <div class="row">
                            <label for="cg1">上传到分类：</label>

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
                                          maxlength="500">${fn:escapeXml(param.summary)}</textarea>
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
            if (data[0]) {
                setCg2Options(data[0]);
            }
        });

        function setProgress(p) {
            var v = parseInt(p.loaded / p.total * 100);

            $('.upload-progress .progress-bar .progress').css({'width': v + '%'});
            $('.upload-progress .progress-value').text(v + '%');
        }

        var doc = null;

        $('#file').change(function () {
            var file = $(this).get(0).files[0];
            if (!file) {
                return false;
            }
            if (file.size > 1024 * 1024 * 100) {
                return alert('文件不能超过100M！');
            }
            $('.doc-upload').hide();
            $('.doc-progress').show();

            var form = new FormData();
            form.append('file', file);

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
                    doc = data;
                    $('#title').val(doc.title);
                    $('.doc-progress .doc-upload-tips').text('上传成功！');
                    setTimeout(function () {
                        $('.doc-progress').hide();
                        $('.doc-form').show();
                    }, 750);
                },
                error: function (r) {
                    console.log(r);
                    try {
                        var result = JSON.parse(r['responseText']);
                        $('.doc-upload-tips').html('上传失败：' + (result.message || '') + '，请重新<a href="${ctx}/doc/upload">上传</a>');
                    } catch (e) {
                        $('.doc-upload-tips').html('上传失败,请重新<a href="${ctx}/doc/upload">上传</a>');
                    }
                }
            });
        });

        $('#docForm').submit(function (e) {
            e.preventDefault();
            if (doc && doc.id) {
                $(this).ajaxSubmit({
                    url: ctx + '/doc/' + doc.id,
                    success: function (data) {
                        location.href = ctx + '/doc/myUpload';
                    },
                    error: function (r) {
                        console.log(r);
                        alert('保存失败！');
                    }
                });
            } else {
                location.reload();
            }
            return false;
        });
    })
</script>
<jsp:include page="../footer.jsp"/>