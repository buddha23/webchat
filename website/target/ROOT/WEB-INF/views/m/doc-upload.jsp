<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-3.0.0.min.js"></script>
    <script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <title></title>
</head>

<body>
<div class="container">
    <header>
        <div class="menu-left">
            <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
        </div>

        <div class="menu-right">
            <a class="menu-item menu-user" href="${ctx}/m/user/center"></a>
        </div>

        <div class="title">文档上传</div>
    </header>

    <div class="content clearfix">
        <div class="page-doc-upload">
            <div class="doc-upload">
                <div class="doc-upload-btn">
                    <i class="fa fa-upload"></i> 上传文档
                </div>
                <div class="doc-upload-tips">
                    从我的手机中选择要上传的文档，通过审核后便可供他人下载
                </div>
                <input class="doc-file" style="display: none;" type="file" id="file" name="file"
                       accept="application/msword,application/vnd.ms-excel,application/pdf,text/plain,application/vnd.ms-powerpoint">
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

            <form style="display:none;" class="page-form-wap doc-form" action="#" id="docForm" method="post">
                <div class="row margintop0">
                    <label for=""></label>
                    <div class="field">
                        <i class="fa fa-check-circle color-blue"></i> 文档上传成功，请完善文档信息！
                    </div>
                </div>
                <div class="row">
                    <label for="">标题：</label>
                    <div class="field">
                        <input class="ui-input-default input-full" type="text" id="title" name="title" maxlength="80"
                               value="${fn:escapeXml(param.title)}"/>
                    </div>
                </div>
                <div class="row">
                    <label for="">牛人币售价：</label>
                    <div class="field">
                        <input class="ui-input-default input-full" type="number" min="0" max="999"
                               style="ime-mode:disabled" name="costScore" value="0"
                               onkeypress="return event.keyCode>=48&&event.keyCode<=57"
                               onpaste="return !clipboardData.getData('text').match(/\d/)"
                               ondragenter="return false"/>
                    </div>
                </div>
                <div class="row">
                    <label for="">上传到分类：</label>
                    <div class="field">
                        <select class="ui-input-default input-full" id="cg1"></select>
                        <label>-</label>
                        <select class="ui-input-default input-full" id="cg2" name="categoryId"></select>
                    </div>
                </div>
                <div class="row">
                    <label for="">简介：</label>
                    <div class="field">
                        <textarea class="ui-textarea-default input-full" name="summary"
                                  maxlength="500">${fn:escapeXml(param.summary)}</textarea>
                    </div>
                </div>
                <div class="row">
                    <label></label>
                    <div class="field">
                        <button class="button btn-green btn-large input-full" type="submit">确定</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <footer>

    </footer>
</div>
<script>
    $(function () {
        var categories = [];
        var doc = null;

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

        $.get('${ctx}/i/doc_categories', function (data) {
            categories = data;
            data.forEach(function (c) {
                if (c.children && c.children.length > 0)
                    $("#cg1").append('<option value="' + c.id + '">' + c.name + '</option>');
            });
            if (data[0]) {
                setCg2Options(data[0]);
            }
        });

        $('.doc-upload .doc-upload-btn').click(function () {
            $('.doc-upload .doc-file').click();
        });

        //测试
        function setProgress(p) {
            var v = parseInt(p.loaded / p.total * 100);

            $('.upload-progress .progress-bar .progress').css({'width': v + '%'});
            $('.upload-progress .progress-value').text(v + '%');
        }

        var progress = (function () {
            var width = 0;
            return {
                getProWidth: function () {
                    return width;
                },
                addWidth: function () {
                    (width < 100) ? width++ : width;
                }
            }
        }());

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
                url: '${ctx}/doc/upload',
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
                        $('.doc-upload-tips').html('上传失败：' + (result.message || '') + '，请重新<a href="${ctx}/m/doc/upload">上传</a>');
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
                    url: '${ctx}/doc/' + doc.id,
                    success: function (data) {
                        alert('保存成功！');
                        location.href = '${ctx}/m/#usercenter';
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
</body>
</html>
