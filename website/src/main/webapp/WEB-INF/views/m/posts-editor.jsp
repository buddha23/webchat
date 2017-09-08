<%--发布帖子--%>
<div class="window">
    <div class="container bg-white">
        <form class="question-write" id="postsform" action="${ctx}/post/publishpost" method="post">
            <div>
                <input id="postTitle" class="input-full ui-input-mobile" type="text" placeholder="提问标题" name="title" maxlength="100" required>
            </div>

            <div class="margintop15">
                <label>问答分类:</label>
                <select class="ui-input-mobile" name="categoryId" id="postCategory"></select>
            </div>

            <div class="margintop15">
                <textarea class="input-full ui-input-mobile" id='edit' name="content" placeholder="提问内容"></textarea>
            </div>
            <div class="margintop15">
                <label>问答悬赏:</label>
                <select class="ui-input-mobile" name="type" id="rewardType">
                    <option value="1" selected>牛人币</option>
                    <%--<option value="2">积分</option>--%>
                    <option value="0">不悬赏</option>
                </select>
                <input class="ui-input-mobile" type="number" name="amount" id="amount" placeholder="输入牛人币数额" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')">
            </div>
            <div class="margintop15">
                <lable style="color: red" id="err"></lable>
            </div>
            <div class="margintop15">
                <button class="button btn-primary" type="submit">发表</button>
                <button class="button btn-light window-close marginleft5" type="button">取消</button>
            </div>
        </form>
    </div>
</div>

<script>
    $(function () {
        layer.closeAll();
        $('.window .window-close').click(function () {
            $('.window').hide(0, function () {
                enable_scroll();
                jQuery('body').css('overflow-y', 'visible');
            });
        });

        $('#edit').froalaEditor({
            toolbarButtonsXS: ['bold', 'italic', 'underline', 'insertImage'],
            height: 200,
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
            var rewardType = $("#rewardType option:selected").val();
            var categoryId = $("#postCategory option:selected").val();
            var amount = $('#amount').val();
            if (title && content && (rewardType == '0' || (amount && amount > 0))) {
//                layer.msg('GOOD');
                if (!amount) amount = 0;
                $.ajax({
                    url: $form.attr('action'),
                    type: $form.attr('method'),
                    data: {title: title, content: content, type: rewardType, amount: amount, categoryId: categoryId},
                    success: function (data) {
                        location.href = '${ctx}/m/posts/' + data.id;
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
                $("#err").html("悬赏金额必须大于0");
            } else {
                $("#err").html("发布失败,标题和内容不能为空哦");
            }
            return false;
        });
    });
</script>