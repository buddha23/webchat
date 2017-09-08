define(['app', 'layer', 'directive/ng-file', 'directive/jquery.dragsort-0.5.2'], function (app, layer) {
    'use strict';

    app.controller('indexImgCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.getData = function () {
            $http.get('admin/imgFiles/browse').success(function (imgs) {
                $scope.imgs = imgs;
                $scope.vars = {};
            }).error(function (req) {
                $scope.buttons = [];
                if (req['responseJSON']) {
                    layer.alert(req['responseJSON'].message);
                } else {
                    layer.alert('获取信息失败！');
                }
            });
        };

        $scope.add = function () {
            $(".upload-select").click();
            $(".upload-select").change(function (e) {
                var file = this.files[0];
                if (!file) {
                    return false;
                }
                //这里我们判断下类型如果不是图片就返回 去掉就可以上传任意文件
                if (!/image\/\w+/.test(file.type)) {
                    layer.msg('请确保文件为图像类型！', {
                        shift: 2,
                        icon: 0
                    });
                    return false;
                }
                var reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = function (e) {
                    var imgSrc = this.result;
                    $scope.imgs.push({
                        name: file.name,
                        path: imgSrc,
                        link: '',
                        src: imgSrc
                    });
                    $scope.$apply();
                };
                $(this).val('');
            })
        };

        $scope.save = function (c) {
            var form = new FormData();
            if (c.id) form.append("id", c.id);
            if (c.path) form.append("path", c.path);
            form.append("link", c.link);
            form.append("type", 1);
            $http({
                url: 'admin/imgFiles/indeximgupdate',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                $scope.imgs = data;
                layer.alert('更新成功');
                console.log(data);
            }).error(function (r) {
                layer.alert('保存失败');
            });
            console.log(form);
        };

        $scope.delete = function (c) {
            if (c.id) {
                if (confirm('确认删除该图片吗？')) {
                    $http.delete('admin/imgFiles/' + c.id).success(function (data) {
                        layer.alert('删除成功');
                        $scope.getData();
                    }).error(function (r) {
                        console.log(r);
                        layer.alert('删除失败！');
                    });
                }
            } else {
                var i = $scope.imgs.indexOf(c);
                console.log(i);
                if (i > -1) {
                    $scope.imgs.splice(i, 1);
                }
            }
        };

        $('.page-photomanage').dragsort({
            dragSelector: ".photomanage-bg .photomanage-img",
            placeHolderTemplate: "<li class='placeHolder'><div></div></li>",
            //dragBetween: true,
            dragEnd: saveOrder
        });

        $scope.getData();
    }]);

    function saveOrder() {
        var names = [];
        var ids = [];
        $('.page-photomanage li').each(function (index, element) {
            var name = $(this).attr('data-sort');
            var id = $(this).attr('data-id');
            names.push(name);
            ids.push(id);
        });
        $.get("admin/imgFiles/updatename?names=" + names + "&ids=" + ids, function (data) {
            layer.alert('排序成功');
        });
    }

});
