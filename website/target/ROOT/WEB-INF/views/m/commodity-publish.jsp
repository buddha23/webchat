<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"/>
    <link rel="stylesheet" type="text/css" href="${assets}/css/public.css">
    <link rel="stylesheet" type="text/css" href="${assets}/css/style_mobile.css">
    <script type="text/javascript" src="${assets}/js/min/jquery-1.11.2.min.js"></script>
    <script type="text/javascript" src="${assets}/js/app.js"></script>
    <script type="text/javascript" src="${assets}/js/mobile/layer.js"></script>
    <title>商品信息发布</title>
</head>

<body>
<div class="container">
    <header>
        <div class="top">
            <div class="menu-left">
                <a id="prevPage" class="menu-item" href="javascript:history.back()"><i class="fa fa-chevron-left"></i></a>
            </div>

            <div class="menu-right">
                <a class="menu-item menu-user" href="${ctx}/m/#usercenter"></a>
            </div>

            <div class="title">
                <c:if test="${param.type == 1}">发布商品</c:if>
                <c:if test="${param.type == 2}">求购商品</c:if>
            </div>
        </div>
    </header>

    <div class="content clearfix">
        <div class="page market-publish">
            <div class="publish-img">
                <input type="file" id="choose" accept="image/*" multiple>
                <ul class="img-list clearfix">
                    <li id="upload">
                        <i class="icon-picture"></i>
                        <em>上传图片</em>
                    </li>
                </ul>
            </div>
            <div class="">
                <form class="page-form-wap js-commod-form" action="#" method="post">
                    <div class="row" style="display: none;">
                        <label for="name">图片路径：</label>
                        <div class="item">
                            <div><input class="ui-input-default input-full" type="text" id="imgPath" name="imgPath" placeholder=""></div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="name">标题：</label>
                        <div class="item">
                            <div><input class="ui-input-default input-full" type="text" id="name" name="name" maxlength="20" placeholder="一句话描述下宝贝吧"></div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="information">宝贝详情：</label>
                        <div class="item">
                            <div><textarea class="ui-input-default input-full" style="height: 80px;" id="information" name="information" maxlength="500" placeholder="描述一下宝贝的成色、亮点、详细信息"></textarea></div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="newDegree">宝贝成色：</label>
                        <div class="item">
                            <div>
                                <select name="newDegree" id="newDegree" class="ui-input-default input-full">
                                    <option value="0">不限</option>
                                    <option value="1">全新</option>
                                    <option value="2">二手</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="price">售价：</label>
                        <div class="item">
                            <div><input class="ui-input-default input-full" id="price" name="price" type="number" max="999999" placeholder="单位：元 (不填默认为面议)" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"></div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="area">地区：</label>
                        <div class="item">
                            <div><input class="ui-input-default input-full" id="area" name="area" type="text" placeholder="如：四川省成都市(不要求详细地址)" maxlength="100"></div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="phone">商家电话：</label>
                        <div class="item">
                            <div><input class="ui-input-default input-full" id="phone" name="phone" type="text" placeholder="您的联系电话号码" maxlength="11"></div>
                        </div>
                    </div>

                    <div class="row">
                        <label for="weixin">商家微信：</label>
                        <div class="item">
                            <div><input class="ui-input-default input-full" id="weixin" name="weixin" type="text" placeholder="您的微信号" maxlength="30" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')"></div>
                        </div>
                    </div>

                    <div class="row">
                        <label></label>
                        <div class="item">
                            <input name="type" value="${param.type}" hidden>
                            <button class="button btn-blue input-full btn-large" type="button" onclick="doSubmit()">确定</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <footer></footer>
    </div>
</div>
<script type="text/javascript" src="${assets}/js/min/jquery.form.min.js"></script>
<script type="text/javascript" src="${assets}/js/formcheck.js"></script>
<script>
    $(function () {
        var filechooser = document.getElementById("choose");
        //    用于压缩图片的canvas
        var canvas = document.createElement("canvas");
        var ctx = canvas.getContext('2d');
        //    瓦片canvas
        var tCanvas = document.createElement("canvas");
        var tctx = tCanvas.getContext("2d");
        var maxsize = 1000 * 1024;
        $("#upload").on("click", function () {
            filechooser.click();
        });
        filechooser.onchange = function () {
            if (!this.files.length) return;
            var files = Array.prototype.slice.call(this.files);
            if (files.length > 3) {
                alert("最多同时只可上传3张图片");
                return;
            }
            files.forEach(function (file, i) {
                if (!/\/(?:jpeg|png|gif)/i.test(file.type)) return;
                var reader = new FileReader();
                var li = document.createElement("li");
//          获取图片大小
                var size = file.size / 1024 > 1024 ? (~~(10 * file.size / 1024 / 1024)) / 10 + "MB" : ~~(file.size / 1024) + "KB";
                li.innerHTML = '<div class="progress"><span></span></div><div class="size">' + size + '</div>';
                $(".img-list").append($(li));
                reader.onload = function () {
                    var result = this.result;
                    var img = new Image();
                    img.src = result;
                    $(li).css("background-image", "url(" + result + ")");
                    //如果图片大小小于1000kb，则直接上传
                    if (result.length <= maxsize) {
                        img = null;
                        upload(result, file.type, $(li));
                        return;
                    }
//      图片加载完毕之后进行压缩，然后上传
                    if (img.complete) {
                        callback();
                    } else {
                        img.onload = callback;
                    }
                    function callback() {
                        var data = compress(img);
                        upload(data, file.type, $(li));
                        img = null;
                    }
                };
                reader.readAsDataURL(file);
            })
        };
        //    使用canvas对大图片进行压缩
        function compress(img) {
            var initSize = img.src.length;
            var width = img.width;
            var height = img.height;
            //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
            var ratio;
            if ((ratio = width * height / 4000000) > 1) {
                ratio = Math.sqrt(ratio);
                width /= ratio;
                height /= ratio;
            } else {
                ratio = 1;
            }
            canvas.width = width;
            canvas.height = height;
//        铺底色
            ctx.fillStyle = "#fff";
            ctx.fillRect(0, 0, canvas.width, canvas.height);
            //如果图片像素大于100万则使用瓦片绘制
            var count;
            if ((count = width * height / 1000000) > 1) {
                count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
//            计算每块瓦片的宽和高
                var nw = ~~(width / count);
                var nh = ~~(height / count);
                tCanvas.width = nw;
                tCanvas.height = nh;
                for (var i = 0; i < count; i++) {
                    for (var j = 0; j < count; j++) {
                        tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
                        ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
                    }
                }
            } else {
                ctx.drawImage(img, 0, 0, width, height);
            }
            //进行最小压缩
            var ndata = canvas.toDataURL('image/jpeg', 0.1);
            console.log('压缩前：' + initSize);
            console.log('压缩后：' + ndata.length);
            console.log('压缩率：' + ~~(100 * (initSize - ndata.length) / initSize) + "%");
            tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
            return ndata;
        }

        //    图片上传，将base64的图片转成二进制对象，塞进formdata上传
        function upload(basestr, type, $li) {
            var text = window.atob(basestr.split(",")[1]);
            var buffer = new Uint8Array(text.length);
            var pecent = 0, loop = null;
            for (var i = 0; i < text.length; i++) {
                buffer[i] = text.charCodeAt(i);
            }
            var blob = getBlob([buffer], type);
            var xhr = new XMLHttpRequest();
            var formdata = getFormData();
            formdata.append('imagefile', blob);
            //上传url
            xhr.open('post', '/m/commodity/imgUpload');
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    console.log(xhr.responseText);
                    var jsonData = JSON.parse(xhr.responseText);
                    var imagedata = jsonData || {};
                    var text = imagedata.path ? '上传成功' : '上传失败';
                    //将上传成功后的路径保存在input中
                    var pathInput = document.getElementById("imgPath");
                    if (pathInput.value == "") {
                        pathInput.value = pathInput.value + imagedata.path;
                    } else {
                        pathInput.value = pathInput.value + "," + imagedata.path;
                    }
                    console.log('图片路径： ' + pathInput.value);
                    console.log(text + '：' + imagedata.path);
                    clearInterval(loop);
                    //当收到该消息时上传完毕
                    $li.find(".progress span").animate({'width': "100%"}, pecent < 95 ? 200 : 0, function () {
                        $(this).html(text);
                    });
                    if (!imagedata.path) return;
                    $(".pic-list").append('<a href="' + imagedata.path + '">' + imagedata.name + '（' + imagedata.size + '）<img src="' + imagedata.path + '" /></a>');
                }
            };
            //数据发送进度，前50%展示该进度
            xhr.upload.addEventListener('progress', function (e) {
                if (loop) return;
                pecent = ~~(100 * e.loaded / e.total) / 2;
                $li.find(".progress span").css('width', pecent + "%");
                if (pecent == 50) {
                    mockProgress();
                }
            }, false);
            //数据后50%用模拟进度
            function mockProgress() {
                if (loop) return;
                loop = setInterval(function () {
                    pecent++;
                    $li.find(".progress span").css('width', pecent + "%");
                    if (pecent == 99) {
                        clearInterval(loop);
                    }
                }, 100)
            }

            xhr.send(formdata);
        }

        /**
         * 获取blob对象的兼容性写法
         * @param buffer
         * @param format
         * @returns {*}
         */
        function getBlob(buffer, format) {
            try {
                return new Blob(buffer, {type: format});
            } catch (e) {
                var bb = new (window.BlobBuilder || window.WebKitBlobBuilder || window.MSBlobBuilder);
                buffer.forEach(function (buf) {
                    bb.append(buf);
                });
                return bb.getBlob(format);
            }
        }

        /**
         * 获取formdata
         */
        function getFormData() {
            var isNeedShim = ~navigator.userAgent.indexOf('Android')
                    && ~navigator.vendor.indexOf('Google')
                    && !~navigator.userAgent.indexOf('Chrome')
                    && navigator.userAgent.match(/AppleWebKit\/(\d+)/).pop() <= 534;
            return isNeedShim ? new FormDataShim() : new FormData()
        }

        /**
         * formdata 补丁, 给不支持formdata上传blob的android机打补丁
         * @constructor
         */
        function FormDataShim() {
            console.warn('using formdata shim');
            var o = this,
                    parts = [],
                    boundary = Array(21).join('-') + (+new Date() * (1e16 * Math.random())).toString(36),
                    oldSend = XMLHttpRequest.prototype.send;
            this.append = function (name, value, filename) {
                parts.push('--' + boundary + '\r\nContent-Disposition: form-data; name="' + name + '"');
                if (value instanceof Blob) {
                    parts.push('; filename="' + (filename || 'blob') + '"\r\nContent-Type: ' + value.type + '\r\n\r\n');
                    parts.push(value);
                }
                else {
                    parts.push('\r\n\r\n' + value);
                }
                parts.push('\r\n');
            };
            // Override XHR send()
            XMLHttpRequest.prototype.send = function (val) {
                var fr,
                        data,
                        oXHR = this;
                if (val === o) {
                    // Append the final boundary string
                    parts.push('--' + boundary + '--\r\n');
                    // Create the blob
                    data = getBlob(parts);
                    // Set up and read the blob into an array to be sent
                    fr = new FileReader();
                    fr.onload = function () {
                        oldSend.call(oXHR, fr.result);
                    };
                    fr.onerror = function (err) {
                        throw err;
                    };
                    fr.readAsArrayBuffer(data);
                    // Set the multipart content type and boudary
                    this.setRequestHeader('Content-Type', 'multipart/form-data; boundary=' + boundary);
                    XMLHttpRequest.prototype.send = oldSend;
                }
                else {
                    oldSend.call(this, val);
                }
            };
        }
    });
</script>

<style>
    #choose {
        display: none;
    }

    canvas {
        width: 100%;
        border: 1px solid #000000;
    }

    .img-list {
        margin: 0 -5px;
    }

    .img-list li {
        float: left;
        position: relative;
        display: block;
        width: 72px;
        height: 72px;
        margin: 5px 5px 20px;
        color: #999;
        background: #fff no-repeat center;
        background-size: cover;
        text-align: center;
        box-sizing: border-box;
    }

    .img-list li#upload {
        border: 1px solid #e8e8e8;
        font-size: 13px;
        background-color: #f8f8f8;
    }

    .img-list li#upload i {
        display: block;
        margin: 14px auto 0;
        font-size: 24px;
        color: #bbb;
    }

    .img-list li#upload em {
        margin-top: 2px;
        display: block;
    }

    .img-list li#upload:active {
        background-color: #fff;
    }

    .progress {
        position: absolute;
        width: 100%;
        height: 12px;
        line-height: 12px;
        bottom: 0;
        left: 0;
        background-color: rgba(23, 165, 255, .5);
    }

    .progress span {
        display: block;
        width: 0;
        height: 100%;
        background-color: rgb(23, 165, 255);
        text-align: center;
        color: #FFF;
        font-size: 12px;
    }

    .size {
        position: absolute;
        width: 100%;
        height: 15px;
        line-height: 15px;
        bottom: -18px;
        text-align: center;
        font-size: 13px;
        color: #666;
    }

    .tips {
        display: block;
        text-align: center;
        font-size: 13px;
        margin: 10px;
        color: #999;
    }

    .pic-list {
        margin: 10px;
        line-height: 18px;
        font-size: 13px;
    }

    .pic-list a {
        display: block;
        margin: 10px 0;
    }

    .pic-list a img {
        vertical-align: middle;
        max-width: 30px;
        max-height: 30px;
        margin: -4px 0 0 10px;
    }
</style>

<script>
    function doSubmit() {
        if (checkDate() == true) {
            $('.js-commod-form').ajaxSubmit({
                url: '${ctx}/m/commodity/publish',
                success: function (data) {
                    layer.open({
                        content: '发布成功！',
                        btn: '我知道了'
                    });
                    location.href = '${ctx}/m/commodity/list';
                },
                error: function (r) {
                    console.log(r);
                    layer.open({
                        content: '发布失败！',
                        btn: '我知道了'
                    });
                }
            });
        }
    }

    function checkDate() {
        var name = $('#name').val();
        var price = $('#price').val();
        var newDegree = $('#newDegree').val();
        var area = $('#area').val();
        var phone = $('#phone').val();
        var weixin = $('#weixin').val();
        var information = $('#information').val();
        var type = '${param.type}';
        if (name == null || name.length < 1) {
            layer.open({
                content: '商品标题不能为空！',
                btn: '我知道了'
            });
            return false;
        } else if(type == null || type == ''){
            layer.open({
                content: '类型错误！请重新选择发布',
                btn: '我知道了'
            });
            return false;
        } else if(area == null || area.length < 1){
            layer.open({
                content: '地区不能为空',
                btn: '我知道了'
            });
            return false;
        }else if(type == 1 && newDegree < 1){
            layer.open({
                content: '请选择商品成色',
                btn: '我知道了'
            });
            return false;
        } else if (phone == null || phone.length < 1 || !OBJ_formcheck.checkPhone(phone)) {
            layer.open({
                content: '电话号码填写错误！',
                btn: '我知道了'
            });
            return false;
        } else if (weixin == null || weixin.length < 1) {
            layer.open({
                content: '微信不能为空！',
                btn: '我知道了'
            });
            return false;
        }else {
            return true;
        }
    }
</script>
</body>
</html>
