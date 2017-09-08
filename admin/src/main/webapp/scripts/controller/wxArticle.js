define(['app', 'directive/ng-file'], function (app) {
    'use strict';

    app.controller('wxArticleCtrl', function ($scope, $http) {
        $http.get('admin/wx/articles').success(function (data) {
            $scope.articles = data;
        }).error(function (req) {
            $scope.articles = [];
            if (req['responseJSON']) {
                alert(req['responseJSON'].message);
            } else {
                alert('获取微信文章设置失败！');
            }
        });

        $scope.fileChanged = function (file, item, index) {
            var form = new FormData();
            form.append('file', file);
            $http({
                url: 'admin/wx/articleIcon/' + index,
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                item.picUrl = data[index].picUrl;
            }).error(function (data) {
                alert(data.message || '上传图标失败');
            });
        };

        $scope.saveArticles = function () {
            $scope.saving = '保存中……';
            $http.post('admin/wx/articles', $scope.articles).success(function (data) {
                alert('保存成功');
                delete $scope.saving;
            }).error(function (data) {
                alert(data.message || '保存失败');
                delete $scope.saving;
            });
        };
    });
});