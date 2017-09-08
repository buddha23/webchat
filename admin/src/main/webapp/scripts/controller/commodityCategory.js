define(['app', 'directive/ng-file'], function (app) {
    'use strict';

    app.controller('commodityCategoryCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.getData = function () {
            $http.get('admin/commodity/categories').success(function (data) {
                $scope.data = data;
                if (data && data.length) {
                    $scope.cg = data[0];
                    $scope.t = data[0];
                    $scope.edit(data[0]);
                }
            });
        };

        $scope.fileChanged = function (file) {
            if (file && FileReader) {
                if (file.size > 1024 * 1024) {
                    delete $scope.t.file;
                    return alert('图片大小不能大于1M');
                }
                var reader = new FileReader();
                reader.onload = function (e) {
                    angular.element('#iconImg').attr('src', e.target.result);
                };
                reader.readAsDataURL(file);
            }
        };

        $scope.add = function () {
            delete $scope.cg;
            $scope.t = {};
        };

        $scope.edit = function (c) {
            $scope.cg = c;
            $scope.t = $.extend({}, c);
            delete $scope.t.file;
        };

        $scope.save = function () {
            var form = new FormData();
            form.append('name', $scope.t.name);
            if ($scope.t.file)
                form.append('file', $scope.t.file);
            if ($scope.t.id)
                form.append('id', $scope.t.id);
            if ($scope.t.icon)
                form.append('icon', $scope.t.icon);
            if ($scope.t.description)
                form.append('description', $scope.t.description);
            $http({
                url: 'admin/commodity/saveCategory',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                alert('保存成功');
                $scope.getData();
            }).error(function (r) {
                alert('保存失败');
            });
        };

        $scope.delete = function (c) {
            if (c.id) {
                if (confirm('删除类型将导致该类型下所有的帖子处于无类型状态，确认删除吗？')) {
                    $http.delete('admin/postsCategory/delCategory/' + c.id).success(function (data) {
                        alert('删除成功！');
                        $scope.getData();
                    }).error(function (r) {
                        console.log(r);
                        alert('删除失败！');
                    });
                }
            }
        };

        $scope.getData();

    }]);

});