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

$.get(ctx + '/i/soft_categories', function (data) {
    categories = data;
    data.forEach(function (c) {
        if (c.children && c.children.length > 0)
            $("#cg1").append('<option value="' + c.id + '">' + c.name + '</option>');
    });
    if (data[0]) {
        setCg2Options(data[0]);
    }
});
var credential;
var policyText = {
    "expiration": "2020-01-01T12:00:00.000Z", //设置该Policy的失效时间，超过这个失效时间之后，就没有办法通过这个policy上传文件了
    "conditions": [
        ["content-length-range", 0, 1048576000] // 设置上传文件的大小限制
    ]
};
g_object_name = '';
now = timestamp = Date.parse(new Date()) / 1000;
policyBase64 = '';
signature = '';
function getCredentialAndUp(up, filename, flag) {
    if (!credential || flag) {
        $.get(ctx + "/soft/credential", function (result) {
            credential = result;
            set_upload_param(up, filename, flag)
        });
    } else {
        set_upload_param(up, filename, flag)
    }
}

function get_signature() {
    policyBase64 = Base64.encode(JSON.stringify(policyText));
    message = policyBase64;
    var bytes = Crypto.HMAC(Crypto.SHA1, message, credential.accessKeySecret, {asBytes: true});
    signature = Crypto.util.bytesToBase64(bytes);
}

function random_string(len) {
    len = len || 32;
    var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
    var maxPos = chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}


function get_suffix(filename) {
    pos = filename.lastIndexOf('.');
    suffix = '';
    if (pos != -1) {
        suffix = filename.substring(pos)
    }
    return suffix;
}

function calculate_object_name(filename) {
    suffix = get_suffix(filename);
    g_object_name = credential.fileDir + random_string(10) + suffix
}

function set_upload_param(up, filename, flag) {
    if (filename != '') {
        suffix = get_suffix(filename);
        calculate_object_name(filename);
    }
    get_signature();
    var value = {
        uuId: credential.uuId,
        userId: credential.userId,
        fileName: filename,
        fileType: filename.substring(filename.length - 3),
        title: document.getElementById('title').value,
        costScore: document.getElementById('costScore').value,
        softCategoryId: document.getElementById('cg2').value,
        description: document.getElementById('description').value,
        specification: document.getElementById('specification').value
    };

    var base64val = Base64.encode(JSON.stringify(value));

    new_multipart_params = {
        'key': g_object_name,
        'policy': policyBase64,
        'OSSAccessKeyId': credential.accessKeyId,
        'success_action_status': '200', //让服务端返回200,不然，默认会返回204
        'signature': signature,
        'x-oss-security-token': credential.securityToken,
        'callback': credential.callBack,
        'x:value': base64val
    };

    up.setOption({
        'url': credential.host,
        'multipart_params': new_multipart_params
    });

    up.start();

}

$(document).on('click', '.file_delete', function () {
    $(this).parent().remove();
    var toremove = '';
    var id = $(this).attr("data-val");
    for (var i in uploader.files) {
        if (uploader.files[i].id === id) {
            toremove = i;
        }
    }
    uploader.files.splice(toremove, 1);
    //console.log("XXX"+$(this).attr("data-val"));
});
var uploader = new plupload.Uploader({
    multi_selection: 'false',
    runtimes: 'html5,flash,silverlight,html4',
    browse_button: 'selectfiles',
    container: document.getElementById('container'),
    flash_swf_url: 'lib/plupload-2.1.2/js/Moxie.swf',
    silverlight_xap_url: 'lib/plupload-2.1.2/js/Moxie.xap',
    url: 'http://oss.aliyuncs.com',
    filters: [
        {title: "Zip files", extensions: "zip,7z,rar"}
    ],

    init: {
        PostInit: function () {
            document.getElementById('ossfile').innerHTML = '';
            document.getElementById('postfiles').onclick = function () {
                getCredentialAndUp(uploader, '', false);
                return false;
            };
        },

        FilesAdded: function (up, files) {
            plupload.each(files, function (file) {
                if (!document.getElementById('title').value) {
                    document.getElementById('title').value = file.name;
                }
                document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b class="file_delete" data-val=' + file.id + '>删除</b>'
                    + '<div class="progress"><div class="progress-bar" style="width: 0"></div></div>'
                    + '</div>';
            });
        },

        BeforeUpload: function (up, file) {
            set_upload_param(up, file.name, false);
        },
        UploadProgress: function (up, file) {
            var d = document.getElementById(file.id);
            d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
            var prog = d.getElementsByTagName('div')[0];
            var progBar = prog.getElementsByTagName('div')[0];
            progBar.style.width = 2 * file.percent + 'px';
            progBar.setAttribute('aria-valuenow', file.percent);
        },

        FileUploaded: function (up, file, info) {
            if (info.status == 200) {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '上传成功!';
            }
            else if (info.status == 403) {
                getCredentialAndUp(up, flie, true);
            }
            else {
                document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
            }
        },
        UploadComplete: function (uploader, files) {
            window.location.href = ctx + "/soft/myUpload";
        }
    }
});

uploader.init();
