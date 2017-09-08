<div id="question-dialog" class="question-dialog" style="padding: 20px; width: 800px; height: 560px; display: none;">
    <div class="question-write">
        <form id="postsform" action="${ctx}/post/publishpost" method="post">
            <div>
                <label>标题:</label>
                <input id="postTitle" class="ui-input-default" type="text" placeholder="提问标题" name="title" maxlength="100" required>

                <label class="marginleft20">分类:</label>
                <select class="ui-input-default" name="categoryId" id="postCategory"></select>
            </div>
            <div class="margintop15">
                <textarea class="input-full ui-input-default" id='edit' name="content"></textarea>
            </div>
            <div class="margintop15">
                <label>问答悬赏:</label>
                <select class="ui-input-default" name="type" id="rewardType">
                    <option value="1" selected>牛人币</option>
                    <%--<option value="2">积分</option>--%>
                    <option value="0">不悬赏</option>
                </select>
                <input class="ui-input-default" type="number" name="amount" id="amount" placeholder="输入牛人币数额" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            </div>
            <div class="margintop15">
                <input class="ui-input-default" id="captcha" onkeyup="value=value.replace(/[^0-9A-Za-z_]/g,'')" style="width: 170px;" placeholder="验证码(看不清？请点击图片)" maxlength="10" required>
                <img class="captcha-img" src="${assets}/images/captcha.png" style="margin-left: 4px;width: 123px;height: 32px;">
            </div>
            <div class="margintop15">
                <button id="postssubmit" class="button btn-primary" type="submit">发表</button>
                <input type="button" id="dialog-close" class="button btn-light marginleft5" value="取消"/>
            </div>
        </form>
    </div>
</div>

<script type="text/javascript" src="${assets}/js/min/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="${assets}/js/lrtk.js"></script>
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
<script src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript" src="${assets}/js/jquery.jcountdown.js"></script>
<script type="text/javascript">
    var operate = {
        publishPosts: function () {
            if (isLogin) {
                var pagei = layer.open({
                    type: 1,   //0-4的选择,
                    title: "发布问题",
                    border: [0],
                    shadeClose: true,
                    area: ['auto', 'auto'],
                    content: $('#question-dialog')
                });
            }
        },
        publishComments: function () {
            var comment = $("textarea[name='reply']").val();
            var captcha = $("input[name='captcha']").val();
            if (comment == '' || captcha == '') {
                layer.alert("回复和验证码不能为空！", {icon: 2});
            } else if (comment.length > 999) {
                layer.alert("回复内容过长~", {icon: 2});
            } else {
                $("#commentForm").ajaxSubmit({
                    success: function () {
                        layer.alert("评论成功", {icon: 1, time: 2000});
                        setTimeout(function () {
                            location.reload();
                        }, 2000);
                    },
                    error: function (r) {
                        layer.alert(JSON.parse(r.responseText).message || "评论失败,回复内容错误", {icon: 2});
                    }
                })
            }
        },
        getMyPosts: function () {
            location.href = "${ctx}/post/mypost";
        }
    };

    $(function () {
        $('.captcha-img').click();
        $('#dialog-close').on('click', function () {
            layer.closeAll();
        });

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

        $('#rewardType').change(function () {
            var rewardType = $("#rewardType option:selected").val();
            if(rewardType === '0'){
                $('#amount').hide();
            }else {
                $('#amount').show();
            }
        });

        $.ajax({
            url: '${ctx}/post/categories',
            type: 'get',
            success: function (data) {
                var $postCategory = $('#postCategory');
                if (data && data.length > 0) {
                    data.forEach(function (c) {
                        $postCategory.append('<option value="' + c.id + '">' + c.name + '</option>');
                    });
                }
            },
            error: function (r) {
                console.log()
            }
        });

        $('#postsform').submit(function (e) {
            var $form = $(this);
            e.preventDefault();
            var title = $('#postTitle').val();
            var content = $('#edit').froalaEditor('html.get');
            var captcha = $('#captcha').val();
            var rewardType = $("#rewardType option:selected").val();
            var categoryId = $("#postCategory option:selected").val();
            var amount = parseInt($('#amount').val());

            if (title && content && (rewardType == '0' || (amount && amount > 0))) {
//                layer.msg('GOOD');
                if(!amount) amount = 0;
                $.ajax({
                    url: $form.attr('action'),
                    type: $form.attr('method'),
                    data: {title: title, content: content, captcha: captcha, type: rewardType, amount: amount, categoryId: categoryId},
                    success: function (data) {
                        layer.alert('发布成功!', {icon: 1}, function () {
                            location.href = '${ctx}/post/' + data.id;
                        });
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
            } else if (!amount  || (amount && amount < 1)) {
                layer.alert('悬赏金额必须大于零')
            } else {
                layer.alert('标题和内容都必须要填写哟！')
            }
            return false;
        });

    });

    $('.captcha-img').click(function () {
        $(this).attr('src', '${ctx}/assets/captcha.jpg?t=' + new Date().getTime());
    });
</script>